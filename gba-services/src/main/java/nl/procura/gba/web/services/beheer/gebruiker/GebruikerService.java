/*
 * Copyright 2024 - 2025 Procura B.V.
 *
 * In licentie gegeven krachtens de EUPL, versie 1.2
 * U mag dit werk niet gebruiken, behalve onder de voorwaarden van de licentie.
 * U kunt een kopie van de licentie vinden op:
 *
 *   https://github.com/vrijBRP/vrijBRP/blob/master/LICENSE.md
 *
 * Deze bevat zowel de Nederlandse als de Engelse tekst
 *
 * Tenzij dit op grond van toepasselijk recht vereist is of schriftelijk
 * is overeengekomen, wordt software krachtens deze licentie verspreid
 * "zoals deze is", ZONDER ENIGE GARANTIES OF VOORWAARDEN, noch expliciet
 * noch impliciet.
 * Zie de licentie voor de specifieke bepalingen voor toestemmingen en
 * beperkingen op grond van de licentie.
 */

package nl.procura.gba.web.services.beheer.gebruiker;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.AUTHENTICATION;
import static nl.procura.commons.core.exceptions.ProExceptionType.DATABASE;
import static nl.procura.commons.core.exceptions.ProExceptionType.ENTRY;
import static nl.procura.gba.common.MiscUtils.copy;
import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.common.MiscUtils.copySet;
import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.jpa.personen.dao.UsrDao.findByEmail;
import static nl.procura.gba.jpa.personen.dao.UsrDao.findByName;
import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory.SYSTEM;
import static nl.procura.gba.web.services.interfaces.GeldigheidStatus.BEEINDIGD;
import static nl.procura.gba.web.services.interfaces.GeldigheidStatus.NOG_NIET_ACTUEEL;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.dao.UsrDao;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.jpa.personen.db.UsrPwHist;
import nl.procura.gba.web.common.misc.Cache;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.rest.client.GbaRestClientException;
import nl.procura.gba.web.rest.v1_0.gebruiker.sync.GbaRestGebruikerSyncVraag;
import nl.procura.gba.web.rest.v1_0.gebruiker.sync.GbaRestGebruikerToevoegenSyncVraag;
import nl.procura.gba.web.rest.v1_0.gebruiker.sync.GbaRestGebruikerVerwijderenSyncVraag;
import nl.procura.gba.web.rest.v1_0.gebruiker.sync.GbaRestGebruikerWachtwoordSyncVraag;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.applicatie.meldingen.ServiceMelding;
import nl.procura.gba.web.services.applicatie.onderhoud.Application;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.gebruiker.info.GebruikerInfoType;
import nl.procura.gba.web.services.beheer.gebruiker.ww.GebruikerWachtwoord;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.theme.Credentials;

public class GebruikerService extends AbstractService {

  private static final String           INLOGERROR           = "Foutieve inloggegevens.";
  private static final String           USER_NOT_FOUND_ERROR = "Fout bij opvragen van gebruiker.";
  private static final int              MAX_RANDOM_NUMBER    = 1024;
  private static final int              MAX_RANDOM_LETTER    = 26;
  private static final int              LETTER_A             = 65;
  private static final int              MAX_PASSWORD_LEN     = 6;
  private static final Cache<Gebruiker> cache                = new Cache<>(30000);

  @Inject
  private PasswordService passwordService;

  public GebruikerService() {
    super("Gebruikers");
  }

  @Override
  public void check() {
    checkVerloopAccount();
  }

  private void checkVerloopAccount() {
    int verloopAccountInDagen = getVerloopAccountInDagen();
    if (verloopAccountInDagen >= 0) {
      DateTime datumEinde = getServices().getGebruiker().getDatumEinde();
      if (datumEinde != null && datumEinde.getDate() != null) {
        int dagen = new ProcuraDate().diffInDays(new ProcuraDate(datumEinde.getDate()));
        if (dagen <= verloopAccountInDagen) {
          ServiceMelding melding = new ServiceMelding();
          melding.setId("Verlopen account");
          melding.setGebruiker(getServices().getGebruiker());
          melding.setAdminOnly(false);
          melding.setCategory(SYSTEM);
          melding.setSeverity(WARNING);
          melding.setMelding("Uw account is nog maar " + dagen + " dag(en) actief.");
          getServices().getMeldingService().add(melding);
        }
      }
    }
  }

  public void checkEmail(Gebruiker gebruiker, String email) {
    if (!isUniekeEmail(gebruiker, email)) {
      throw new ProException(ENTRY, WARNING,
          "Het ingevoerde e-mailadres komt reeds voor bij een andere gebruiker." +
              "<br/>Voer een uniek e-mailadres in.");
    }
  }

  public void deblokkeer(Gebruiker gebruiker) {
    to(gebruiker, Gebruiker.class).setBlok(toBigDecimal(0));
    save(gebruiker);
  }

  @ThrowException(USER_NOT_FOUND_ERROR)
  public Gebruiker getGebruikerByCode(long code, boolean isAttributen) {
    Usr u = findEntity(Usr.class, code);
    if (u != null) {
      Gebruiker gebruiker = copy(u, Gebruiker.class);
      if (isAttributen) {
        laadAttributen(gebruiker);
      }
      return gebruiker;
    }

    return Gebruiker.getDefault();
  }

  @ThrowException(USER_NOT_FOUND_ERROR)
  public Gebruiker getGebruikerByGebruiker(Gebruiker gebruiker) {
    Gebruiker Gebruiker = getGebruikerByNaam(gebruiker.getGebruikersnaam(), false);
    return Gebruiker != null ? Gebruiker : gebruiker;
  }

  public Gebruiker getGebruikerByCredentials(String userAgent, String remoteAddress,
      Credentials credentials, boolean useCache) {

    if (cache.is(useCache, credentials)) {
      return cache.get(credentials);
    }

    Gebruiker gebruiker = null;

    try {
      List<Usr> users = new ArrayList<>(findByName(credentials.getUsername()));
      users.addAll(findByEmail(credentials.getUsername()));
      for (Usr u : users) {
        Gebruiker g = copy(u, Gebruiker.class);
        laadAttributen(g);
        gebruiker = g;

        if (!isCorrectWachtwoord(g, credentials)) {
          updateBlokkering(u);
        }

        if (g.getGeldigheidStatus().is(BEEINDIGD)) {
          throw new ProException(AUTHENTICATION, INFO,
              "Account is gedeactiveerd per " + g.getDatumEinde().getFormatDate() + ".");
        }

        if (g.getGeldigheidStatus().is(NOG_NIET_ACTUEEL)) {
          throw new ProException(AUTHENTICATION, INFO,
              "Account wordt pas actief per " + g.getDatumIngang().getFormatDate() + ".");
        }

        if (g.isGeblokkeerd()) {
          throw new ProException(AUTHENTICATION, INFO,
              "Account is automatisch geblokkeerd na 3 foutieve inlogpogingen.");
        }

        g.setIpAdres(remoteAddress);
        getServices().setGebruiker(gebruiker); // Geef door aan serviceContainer

        resetBlokkering(u); // returns the reset to 0
        cache.put(credentials, gebruiker); // put in cache
        return gebruiker;
      }
    } catch (ProException e) {
      throw e;
    } catch (Exception e) {
      throw new ProException(DATABASE, ERROR, USER_NOT_FOUND_ERROR, e);
    } finally {
      addToLog(userAgent, remoteAddress, credentials, gebruiker);
    }

    // als for-loop leeg is, dus als gebruiker niet bestaat
    throw new ProException(AUTHENTICATION, INFO, INLOGERROR);
  }

  public Gebruiker getGebruikerByNaam(String gebruikersnaam) {
    return getGebruikerByNaam(gebruikersnaam, false);
  }

  public Gebruiker getGebruikerByNaamWithCache(String gebruikersnaam) {
    return getGebruikerByNaam(gebruikersnaam, true);
  }

  @ThrowException(USER_NOT_FOUND_ERROR)
  private Gebruiker getGebruikerByNaam(String gebruikersnaam, boolean useCache) {
    if (StringUtils.isBlank(gebruikersnaam)) {
      return null;
    }

    // Laad gebruiker uit de cache
    if (cache.is(useCache, gebruikersnaam)) {
      return cache.get(gebruikersnaam);
    }

    // Kijk of de huidige gebruiker niet dezelfde is
    if (useCache) {
      Gebruiker gebruiker = getServices().getGebruiker();
      if (gebruiker != null && gebruiker.getGebruikersnaam().equals(gebruikersnaam)) {
        return gebruiker;
      }
    }

    // Zoek de gebruiker
    List<Usr> usrs = findByName(gebruikersnaam);
    if (usrs.isEmpty()) {
      usrs = findByEmail(gebruikersnaam);
    }
    for (Usr u : usrs) {
      Gebruiker gebruiker = laadAttributen(copy(u, Gebruiker.class));
      cache.put(gebruikersnaam, gebruiker);
      return gebruiker;
    }
    return null;
  }

  /**
   * retourneert de documenten gekoppeld aan de meegegeven gebruiker
   *
   * @return verzameling van gekoppelde documenten
   */

  public Set<DocumentRecord> getGebruikerDocumenten(Gebruiker gebruiker) {
    return copySet(to(gebruiker, Gebruiker.class).getDocuments(), DocumentRecord.class);
  }

  /**
   * retourneert de locaties gekoppeld aan de meegegeven gebruiker
   *
   * @return verzameling van gekoppelde locaties
   */
  public Set<Locatie> getGebruikerLocaties(Gebruiker gebruiker) {
    return copySet(to(gebruiker, Gebruiker.class).getLocations(), Locatie.class);
  }

  /**
   * retourneert de profielen gekoppeld aan de meegegeven gebruiker
   *
   * @return verzameling van gekoppelde profielen
   */
  public Set<Profiel> getGebruikerProfielen(Gebruiker gebruiker) {
    return copySet(to(gebruiker, Gebruiker.class).getProfiles(), Profiel.class);
  }

  public List<Gebruiker> getGebruikers(boolean isAttributen) {
    return getGebruikers(GeldigheidStatus.ACTUEEL, isAttributen);
  }

  @ThrowException("Fout bij ophalen van gebruikers")
  public List<Gebruiker> getGebruikers(GeldigheidStatus status, boolean isAttributen) {
    return getGebruikers(copyList(UsrDao.find(), Gebruiker.class), status, isAttributen);
  }

  @ThrowException("Fout bij ophalen van gebruikers")
  public List<Gebruiker> getGebruikers(List<Gebruiker> gebruikers, GeldigheidStatus status, boolean isAttributen) {
    List<Gebruiker> newList = new ArrayList<>();
    for (Gebruiker ng : gebruikers) {
      if (status.is(ng.getGeldigheidStatus())) {
        if (isAttributen) {
          laadAttributen(ng);
        }
        newList.add(ng);
      }
    }

    return newList;
  }

  @ThrowException(USER_NOT_FOUND_ERROR)
  public UsrFieldValue getGebruikerWaarde(long code) {
    Usr u = findEntity(Usr.class, code);
    return u != null ? new UsrFieldValue(copy(u, Gebruiker.class)) : new UsrFieldValue(0, "");
  }

  public List<String> getGebruikerWachtwoorden(Gebruiker gebruiker, int max) {
    return getGebruikerWachtwoorden(gebruiker).stream()
        .limit(max)
        .map(UsrPwHist::getPw)
        .map(passwordService::getPassword)
        .collect(Collectors.toList());
  }

  public Optional<String> getCurrentPassword(Gebruiker gebruiker) {
    return getCurrentEncryptedPassword(gebruiker)
        .map(passwordService::getPassword);
  }

  private Optional<byte[]> getCurrentEncryptedPassword(Gebruiker gebruiker) {
    // must use getGebruikerWachtwoorden otherwise streaming API doesn't work
    return getGebruikerWachtwoorden(gebruiker)
        .stream()
        .findFirst()
        .map(UsrPwHist::getPw);
  }

  public int getVerlooptermijn(Gebruiker gebruiker) {
    return aval(gebruiker.getParameters().get(ParameterConstant.WACHTWOORD_VERLOOP).getValue());
  }

  /**
   * Geeft de datum terug waarop het huidige ww van kracht geworden is.
   */
  @ThrowException("Fout bij het opvragen van de wachtwoorden")
  public ProcuraDate getWachtwoordMutatiedatum(Gebruiker gebruiker) {
    return getGebruikerWachtwoorden(gebruiker)
        .stream()
        .findFirst()
        .map(ww -> new ProcuraDate(astr(ww.getDIn())))
        .orElse(null);
  }

  /**
   * Geeft de datum terug waarop het ww verloopt.
   *
   * @return ProcuraDate
   */
  public ProcuraDate getWachtwoordVerloopdatum(Gebruiker gebruiker) {
    ProcuraDate mutatieDatum = getWachtwoordMutatiedatum(gebruiker);
    String aantalDagen = gebruiker.getParameters().get(ParameterConstant.WACHTWOORD_VERLOOP).getValue();
    ProcuraDate date = mutatieDatum == null ? new ProcuraDate() : getWachtwoordMutatiedatum(gebruiker);
    return date.addDays(aval(aantalDagen));
  }

  public int getVerloopAccountInDagen() {
    return aval(getServices().getGebruikerService().getSysteemParm(ParameterConstant.ACCOUNT_VERLOOP, false));
  }

  public boolean isWachtwoordTijdelijk() {
    return pos(getServices().getGebruikerService().getSysteemParm(ParameterConstant.WACHTWOORD_TIJDELIJK, false));
  }

  public boolean isCorrectWachtwoord(Gebruiker gebruiker, Credentials credentials) {
    return getCurrentEncryptedPassword(gebruiker)
        .filter(encrypted -> passwordService.match(credentials.getPassword(), encrypted))
        .isPresent();
  }

  @ThrowException("Fout bij het opvragen van de wachtwoorden")
  public boolean isResetWachtwoord(Gebruiker gebruiker) {
    return getGebruikerWachtwoorden(gebruiker)
        .stream()
        .findFirst()
        .map(ww -> pos(ww.getResetPw()))
        .orElse(false);
  }

  public boolean isWachtwoordKanVerlopen(Gebruiker gebruiker) {
    return pos(getVerlooptermijn(gebruiker));
  }

  @ThrowException("Fout bij het koppelen van record")
  @Transactional
  public void koppelActie(List<? extends KoppelbaarAanGebruiker> koppelList,
      List<Gebruiker> gebruikers,
      KoppelActie koppelActie) {

    for (KoppelbaarAanGebruiker koppelObject : koppelList) {
      for (Gebruiker gebruiker : gebruikers) {
        if (koppelActie.isPossible(koppelObject.isGekoppeld(gebruiker))) {
          gebruiker.koppelActie(koppelObject, koppelActie);
          koppelObject.koppelActie(gebruiker, koppelActie);
          saveEntity(gebruiker);
        }
      }

      saveKoppelObject(koppelObject);
    }
  }

  @ThrowException("Fout bij het opslaan van de gebruiker")
  @Transactional
  public void save(Gebruiker gebruiker) {
    save(Collections.singletonList(gebruiker));
    syncRemoteUser(gebruiker);
  }

  @ThrowException("Fout bij het opslaan van de gebruiker")
  @Transactional
  public void save(List<Gebruiker> gebruikers) {
    gebruikers.forEach(this::saveEntity);
  }

  @ThrowException("Fout bij het laden van de gebruiker.")
  public void reload(Gebruiker gebruiker) {
    laadAttributen(gebruiker);
  }

  public Gebruiker setInformatie(Gebruiker gebruiker) {
    return setInformatie(Collections.singletonList(gebruiker)).get(0);
  }

  public List<Gebruiker> setInformatie(List<Gebruiker> gebruikers) {
    for (Gebruiker gebruiker : gebruikers) {
      gebruiker.setInformatie(getServices().getGebruikerInfoService().getGebruikerInfo(gebruiker));
      gebruiker.setEmail(gebruiker.getInformatie().getInfo(GebruikerInfoType.email).getGebruikerWaarde());
      gebruiker.setTelefoonnummer(gebruiker.getInformatie().getInfo(GebruikerInfoType.telefoon).getGebruikerWaarde());
      gebruiker.setAfdeling(gebruiker.getInformatie().getInfo(GebruikerInfoType.afdelingsnaam).getGebruikerWaarde());
    }

    return gebruikers;
  }

  public void setProfielen(Gebruiker g) {
    g.setProfielen(getServices().getProfielService().getGebruikerProfielen(g));
  }

  public void setParameters(Gebruiker g) {
    g.setParameters(getServices().getParameterService().getGebruikerParameters(g));
  }

  @Transactional
  @ThrowException("Fout bij het opslaan van het wachtwoord")
  public boolean setWachtwoord(Gebruiker gebruiker, String wachtwoord, boolean resetPw) {
    gebruiker.setGeblokkeerd(false);
    byte[] encryptedPw = passwordService.encryptPassword(wachtwoord);
    boolean isTemporary = isWachtwoordTijdelijk();
    resetPw = resetPw && isTemporary;
    GebruikerWachtwoord newGebruikerWw = new GebruikerWachtwoord(gebruiker, encryptedPw, resetPw);

    // Save new Password
    saveEntity(newGebruikerWw);
    saveEntity(gebruiker);

    syncChangeRemotePassword(gebruiker, newGebruikerWw);
    return resetPw;
  }

  public void setWachtwoordVerloop(Gebruiker gebruiker) {
    boolean verlopen = false;
    if (isResetWachtwoord(gebruiker)) { // een gereset ww moet onmiddelijk gewijzigd worden en wordt daarom als verlopen beschouwd.
      verlopen = true;
    } else if (isWachtwoordKanVerlopen(gebruiker)) {
      verlopen = getWachtwoordVerloopdatum(gebruiker).isExpired();
    }

    gebruiker.setWachtwoordVerlopen(verlopen);
  }

  @Transactional
  @ThrowException("Fout bij het verwijderen van gebruiker")
  public void delete(Gebruiker gebruiker) {
    removeEntity(gebruiker);
    syncRemoveRemoteUser(gebruiker);
  }

  /**
   * Generate a new password
   */
  public String generateWachtwoord() {
    SecureRandom random = new SecureRandom();
    String message = String.valueOf(random.nextInt(MAX_RANDOM_NUMBER))
        + randomLetter(random)
        + randomLetter(random)
        + random.nextInt(MAX_RANDOM_NUMBER)
        + randomLetter(random)
        + randomLetter(random)
        + random.nextInt(MAX_RANDOM_NUMBER)
        + randomLetter(random)
        + randomLetter(random)
        + random.nextInt(MAX_RANDOM_NUMBER)
        + randomLetter(random)
        + randomLetter(random);

    return message.toLowerCase(Locale.ENGLISH).substring(0, MAX_PASSWORD_LEN);
  }

  @Transactional
  @ThrowException("Fout bij bijwerken wachtwoord")
  public void syncChangeLocalPassword(String username, long date, long time, String password, boolean resetPw) {
    List<Usr> usrs = findByName(username);
    for (Usr u : usrs) {
      Gebruiker gebruiker = copy(u, Gebruiker.class);
      gebruiker.setGeblokkeerd(false);
      laadAttributen(gebruiker);
      String currentPassword = getCurrentPassword(gebruiker).orElse(null);
      if (currentPassword == null || !StringUtils.equals(currentPassword, password)) {
        GebruikerWachtwoord ww = new GebruikerWachtwoord(gebruiker, passwordService.encryptPassword(password), resetPw);
        ww.setDIn(date);
        ww.setTIn(time);
        saveEntity(ww);
        saveEntity(gebruiker);
        cache.cleanAll();
      }
    }
  }

  @Transactional
  @ThrowException("Fout bij toevoegen gebruiker")
  public void syncAddLocalUser(String username, String name, boolean admin,
      boolean block, long dateStart, long dateEnd) {
    List<Usr> usrs = findByName(username);
    Gebruiker gebruiker = new Gebruiker();
    if (!usrs.isEmpty()) {
      gebruiker = copy(usrs.get(0), Gebruiker.class);
    }
    gebruiker.setGebruikersnaam(username);
    gebruiker.setNaam(name);
    gebruiker.setAdmin(toBigDecimal(admin ? 1 : 0));
    gebruiker.setGeblokkeerd(block);
    gebruiker.setDatumIngang(new DateTime(dateStart));
    gebruiker.setDatumEinde(new DateTime(dateEnd));
    saveEntity(gebruiker);
    cache.cleanAll();
  }

  @Transactional
  @ThrowException("Fout bij toevoegen gebruiker")
  public void syncRemoveLocalUser(String username) {
    List<Usr> usrs = findByName(username);
    if (!usrs.isEmpty()) {
      Gebruiker gebruiker = copy(usrs.get(0), Gebruiker.class);
      removeEntity(gebruiker);
      cache.cleanAll();
    }
  }

  private static char randomLetter(SecureRandom random) {
    return (char) (random.nextInt(MAX_RANDOM_LETTER) + LETTER_A);
  }

  /**
   * Sync with remote app instance
   */
  private void syncRemoteUser(Gebruiker gebruiker) {

    for (Application syncInstance : getServices().getOnderhoudService().getActiveApps(false)) {
      try {
        if (syncInstance.getAttributes().isSyncUsers()) {
          GbaRestGebruikerToevoegenSyncVraag gebrVraag = new GbaRestGebruikerToevoegenSyncVraag();
          gebrVraag.setGebruikersnaam(gebruiker.getGebruikersnaam());
          gebrVraag.setNaam(gebruiker.getNaam());
          gebrVraag.setDatumIngang(gebruiker.getDatumIngang().getLongDate());
          gebrVraag.setDatumEinde(gebruiker.getDatumEinde().getLongDate());
          gebrVraag.setAdmin(gebruiker.isAdministrator());
          gebrVraag.setGeblokkeerd(gebruiker.isGeblokkeerd());

          GbaRestGebruikerSyncVraag syncVraag = new GbaRestGebruikerSyncVraag();
          syncVraag.setGebruikerToevoegen(gebrVraag);
          syncInstance.getClient().getGebruiker().synchronize(syncVraag).check().getEntity();
        }
      } catch (ProException | GbaRestClientException e) {
        throw new ProException("Fout bij wijzigen wachtwoord.", e);
      }
    }
  }

  /**
   * Sync with remote app instance
   */
  private void syncChangeRemotePassword(Gebruiker gebruiker, GebruikerWachtwoord newGebruikerWw) {
    for (Application syncInstance : getServices().getOnderhoudService().getActiveApps(false)) {
      try {
        if (syncInstance.getAttributes().isSyncUsers()) {
          GbaRestGebruikerWachtwoordSyncVraag vraag = new GbaRestGebruikerWachtwoordSyncVraag();
          vraag.setGebruikersnaam(gebruiker.getGebruikersnaam());
          vraag.setDatum(newGebruikerWw.getDIn());
          vraag.setTijd(newGebruikerWw.getTIn());
          vraag.setResetPassword(newGebruikerWw.getResetPw().intValue() > 0);
          vraag.setWachtwoord(passwordService.getPassword(newGebruikerWw.getPw()));

          GbaRestGebruikerSyncVraag syncVraag = new GbaRestGebruikerSyncVraag();
          syncVraag.setWachtwoord(vraag);
          syncInstance.getClient().getGebruiker().synchronize(syncVraag).check().getEntity();
        }
      } catch (ProException | GbaRestClientException e) {
        throw new ProException("Fout bij wijzigen wachtwoord.", e);
      }
    }
  }

  /**
   * Sync with remove app instance
   */
  private void syncRemoveRemoteUser(Gebruiker gebruiker) {
    for (Application syncInstance : getServices().getOnderhoudService().getActiveApps(false)) {
      try {
        if (syncInstance.getAttributes().isSyncUsers()) {
          GbaRestGebruikerVerwijderenSyncVraag vraag = new GbaRestGebruikerVerwijderenSyncVraag();
          vraag.setGebruikersnaam(gebruiker.getGebruikersnaam());

          GbaRestGebruikerSyncVraag syncVraag = new GbaRestGebruikerSyncVraag();
          syncVraag.setGebruikerVerwijderen(vraag);
          syncInstance.getClient().getGebruiker().synchronize(syncVraag).check().getEntity();
        }
      } catch (ProException | GbaRestClientException e) {
        throw new ProException("Fout bij verwijderen gebruiker.", e);
      }
    }
  }

  /**
   * Voeg inlogpoging toe aan de log
   */
  @ThrowException("Fout bij opslaan inlogpoging")
  public Gebruiker addToLog(String userAgent, String remoteAddress, Credentials credentials, Gebruiker gebruiker) {
    getServices().getLogService().addLoginAttempt(userAgent, remoteAddress, credentials.getUsername(), gebruiker);
    return gebruiker;
  }

  private List<GebruikerWachtwoord> getGebruikerWachtwoorden(Gebruiker gebruiker) {
    return copyList(gebruiker.getUsrPwHists(), GebruikerWachtwoord.class);
  }

  private boolean isUniekeEmail(Gebruiker gebruiker, String email) {
    if (fil(email)) {
      return findByEmail(email).stream()
          .allMatch(usr -> usr.getCUsr()
              .equals(gebruiker.getCUsr()));
    }
    return true;
  }

  private Gebruiker laadAttributen(Gebruiker gebruiker) {
    setProfielen(gebruiker);
    setParameters(gebruiker);
    setInformatie(gebruiker);
    setWachtwoordVerloop(gebruiker);

    return gebruiker;
  }

  private void resetBlokkering(Usr u) {
    if (u.getBlok().intValue() > 0) {
      u.setBlok(toBigDecimal(0));
      saveEntity(u);
    }
  }

  private void saveKoppelObject(KoppelbaarAanGebruiker koppelObject) {
    if (koppelObject instanceof Locatie) {
      Locatie locatie = (Locatie) koppelObject;
      if (locatie.getCLocation() == 0) { // Zorg ervoor dat de locatie met c_location == 0
        // niet als nieuwe entiteit in de database opgeslagen wordt.
        saveEntity(locatie);
      }
    } else {
      saveEntity(koppelObject);
    }
  }

  private void updateBlokkering(Usr u) {
    u.setBlok(toBigDecimal(aval(u.getBlok()) + 1));
    saveEntity(u);
    throw new ProException(AUTHENTICATION, INFO, INLOGERROR);
  }
}
