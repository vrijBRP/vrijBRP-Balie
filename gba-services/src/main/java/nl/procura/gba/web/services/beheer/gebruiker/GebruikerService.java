/*
 * Copyright 2021 - 2022 Procura B.V.
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

import static java.util.Arrays.asList;
import static nl.procura.gba.common.MiscUtils.*;
import static nl.procura.gba.jpa.personen.dao.UsrDao.findByEmail;
import static nl.procura.gba.jpa.personen.dao.UsrDao.findByName;
import static nl.procura.gba.web.services.interfaces.GeldigheidStatus.BEEINDIGD;
import static nl.procura.gba.web.services.interfaces.GeldigheidStatus.NOG_NIET_ACTUEEL;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.*;
import static nl.procura.standard.exceptions.ProExceptionType.*;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

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
import nl.procura.standard.exceptions.ProException;
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

  public void checkEmail(Gebruiker gebruiker, String email) {

    if (!isUniekeEmail(gebruiker, email)) {
      StringBuilder msg = new StringBuilder();
      msg.append("Het ingevoerde e-mailadres komt reeds voor bij gebruiker ");
      msg.append(gebruiker.getGebruikersnaam());
      msg.append(".<br/> Voer een uniek e-mailadres in a.u.b.");

      throw new ProException(ENTRY, WARNING, msg.toString());
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

  public Gebruiker getGebruikerByCredentials(String userAgent, String remoteAddress, Credentials credentials,
      boolean useCache) {

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

    throw new ProException(AUTHENTICATION, INFO,
        INLOGERROR); // als for-loop leeg is, dus als gebruiker niet bestaat
  }

  @ThrowException(USER_NOT_FOUND_ERROR)
  public Gebruiker getGebruikerByGebruiker(Gebruiker gebruiker) {
    Gebruiker Gebruiker = getGebruikerByNaam(gebruiker.getGebruikersnaam(), false);
    return Gebruiker != null ? Gebruiker : gebruiker;
  }

  @ThrowException(USER_NOT_FOUND_ERROR)
  public Gebruiker getGebruikerByNaam(String gebruikersnaam, boolean useCache) {

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
    for (Usr u : findByName(gebruikersnaam)) {
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
  public Gebruiker getGebruikerByEmail(String gebruikersnaam) {
    List<Gebruiker> gebruikers = getGebruikersByEmail(gebruikersnaam);
    return gebruikers.size() == 1 ? gebruikers.get(0) : null;
  }

  @ThrowException(USER_NOT_FOUND_ERROR)
  public List<Gebruiker> getGebruikersByEmail(String email) {
    List<Gebruiker> gebruikers = new ArrayList<>();
    for (Usr u : findByEmail(email)) {
      gebruikers.add(laadAttributen(copy(u, Gebruiker.class)));
    }

    return gebruikers;
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
    save(asList(gebruiker));
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
    return setInformatie(asList(gebruiker)).get(0);
  }

  public List<Gebruiker> setInformatie(List<Gebruiker> gebruikers) {
    for (Gebruiker gebruiker : gebruikers) {
      Gebruiker impl = gebruiker;
      impl.setInformatie(getServices().getGebruikerInfoService().getGebruikerInfo(gebruiker));
      impl.setEmail(gebruiker.getInformatie().getInfo(GebruikerInfoType.email).getGebruikerWaarde());
      impl.setTelefoonnummer(gebruiker.getInformatie().getInfo(GebruikerInfoType.telefoon).getGebruikerWaarde());
      impl.setAfdeling(gebruiker.getInformatie().getInfo(GebruikerInfoType.afdelingsnaam).getGebruikerWaarde());
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
  public void setWachtwoord(Gebruiker gebruiker, String wachtwoord, boolean reset) {
    gebruiker.setGeblokkeerd(false);
    GebruikerWachtwoord newGebruikerWw = new GebruikerWachtwoord(gebruiker, passwordService.encryptPassword(wachtwoord),
        reset);

    // Save new Password
    saveEntity(newGebruikerWw);
    saveEntity(gebruiker);

    syncChangeRemotePassword(gebruiker, reset, newGebruikerWw);
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
    StringBuilder message = new StringBuilder();
    message.append(random.nextInt(MAX_RANDOM_NUMBER));
    message.append(randomLetter(random));
    message.append(randomLetter(random));
    message.append(random.nextInt(MAX_RANDOM_NUMBER));
    message.append(randomLetter(random));
    message.append(randomLetter(random));
    message.append(random.nextInt(MAX_RANDOM_NUMBER));
    message.append(randomLetter(random));
    message.append(randomLetter(random));
    message.append(random.nextInt(MAX_RANDOM_NUMBER));
    message.append(randomLetter(random));
    message.append(randomLetter(random));

    return message.toString().toLowerCase(Locale.ENGLISH).substring(0, MAX_PASSWORD_LEN);
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
        GebruikerWachtwoord ww = new GebruikerWachtwoord(gebruiker, passwordService.encryptPassword(password), true);
        ww.setDIn(date);
        ww.setTIn(time);
        ww.setResetPw(toBigDecimal(resetPw ? 1 : 0));
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
  private void syncChangeRemotePassword(Gebruiker gebruiker, boolean reset, GebruikerWachtwoord newGebruikerWw) {
    for (Application syncInstance : getServices().getOnderhoudService().getActiveApps(false)) {
      try {
        if (syncInstance.getAttributes().isSyncUsers()) {
          GbaRestGebruikerWachtwoordSyncVraag vraag = new GbaRestGebruikerWachtwoordSyncVraag();
          vraag.setGebruikersnaam(gebruiker.getGebruikersnaam());
          vraag.setDatum(newGebruikerWw.getDIn());
          vraag.setTijd(newGebruikerWw.getTIn());
          vraag.setWachtwoord(passwordService.getPassword(newGebruikerWw.getPw()));
          vraag.setResetPassword(reset);

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
  private void addToLog(String userAgent, String remoteAddress, Credentials credentials, Gebruiker gebruiker) {
    getServices().getLogService().addLoginAttempt(userAgent, remoteAddress, credentials, gebruiker);
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
