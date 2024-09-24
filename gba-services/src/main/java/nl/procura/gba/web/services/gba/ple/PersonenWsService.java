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

package nl.procura.gba.web.services.gba.ple;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.ENTRY;
import static nl.procura.commons.core.exceptions.ProExceptionType.NO_RESULTS;
import static nl.procura.commons.core.exceptions.ProExceptionType.WEBSERVICE;
import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory.FAULT;
import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingIds.GBA_V_BLOK;
import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingIds.GBA_V_VERLOOP;
import static nl.procura.gba.web.services.gba.templates.ZoekProfielType.PROFIEL_GBAV_PLUS;
import static nl.procura.gba.web.services.gba.templates.ZoekProfielType.PROFIEL_STANDAARD;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.isTru;
import static nl.procura.standard.Globalfunctions.pos;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.base.PLEMessage;
import nl.procura.diensten.gba.ple.base.PLEResult;
import nl.procura.diensten.gba.ple.client.PLEHTTPClient;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.PLExtSorter;
import nl.procura.diensten.gba.ple.extensions.PLResultComposite;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.gba.ple.procura.arguments.PLELoginArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLNumber;
import nl.procura.diensten.gba.wk.baseWK.BaseWKMessage;
import nl.procura.diensten.gba.wk.client.WKHTTPClient;
import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.diensten.gba.wk.extensions.WKPersonWrapperSorter;
import nl.procura.diensten.gba.wk.extensions.WKResultWrapper;
import nl.procura.diensten.gba.wk.extensions.WKWrapperSorter;
import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.services.applicatie.meldingen.types.GbavMelding;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.gba.ple.opslag.PersonenWsOpslag;
import nl.procura.gba.web.services.gba.ple.opslag.PersoonslijstOpslagEntry;
import nl.procura.gba.web.services.gba.ple.opslag.WoningkaartOpslagEntry;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieLijst;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieLijstHandler;
import nl.procura.gba.web.services.gba.templates.GbaTemplateService;
import nl.procura.gba.web.services.gba.templates.ZoekProfielType;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.GbaWsRestGbavAccount;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.deblokkeren.GbaWsRestGbavDeblokkerenVraag;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.update.GbaWsRestGbavAccountUpdatenAntwoord;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.update.GbaWsRestGbavAccountUpdatenVraag;
import nl.procura.gbaws.web.rest.v1_0.gbav.wachtwoord.versturen.GbaWsRestGbavWachtwoordVersturenAntwoord;
import nl.procura.gbaws.web.rest.v1_0.gbav.wachtwoord.versturen.GbaWsRestGbavWachtwoordVersturenVraag;
import nl.procura.vaadin.theme.Credentials;
import nl.procura.validation.Anr;
import nl.procura.validation.Bsn;

public class PersonenWsService extends GbaTemplateService {

  private static final int INCORRECTE_PERSOON_ID_2   = 19;
  private static final int INCORRECTE_PERSOON_ID_1   = 20;
  private static final int GBAV_INFO                 = 30;
  private static final int MAX_AANTAL_ZOEKRESULTATEN = 201;

  private static final Logger LOGGER = LoggerFactory.getLogger(PersonenWsService.class);

  private PersonenWsOpslag opslag = new PersonenWsOpslag();

  public PersonenWsService() {
    super("Personen-ws");
  }

  @Override
  public void check() {
    checkGbav();
  }

  public void checkGbav() {

    try {
      List<GbaWsRestGbavAccount> accounts = getGbavAccounts(PROFIEL_STANDAARD);

      if (accounts != null) {

        for (GbaWsRestGbavAccount account : accounts) {
          if (fil(account.getDatumVerloop())) {
            int dagen = account.getDagenGeldig();

            String melding = "";
            if (dagen <= 0) {
              melding = "Een GBA-V wachtwoord is verlopen.";
            } else if (dagen < 10) {
              melding = "Een GBA-V wachtwoord verloopt over " +
                  (dagen == 1 ? "1 dag." : (dagen + " dagen."));
            }

            if (fil(melding)) {
              String id = GBA_V_VERLOOP + account.getCode();
              getServices().getMeldingService().add(new GbavMelding(id, account, melding));
            }
          }

          if (account.isGeblokkeerd()) {
            String melding = "Een GBA-V account is geblokkeerd. Waarschijnlijk vanwege een foutief wachtwoord.";
            String id = GBA_V_BLOK + account.getCode();
            getServices().getMeldingService().add(new GbavMelding(id, account, melding));
          }
        }
      }
    } catch (Exception e) {
      if (!(e instanceof ConnectException) && !(e instanceof SocketTimeoutException) &&
          !(e.getCause() instanceof ConnectException) && !(e.getCause() instanceof SocketTimeoutException)) {
        e.printStackTrace();
      }

      addMessage(false, FAULT, ERROR, "Kan geen verbinding met de BRP webservice maken: " + getURL(), getReden(e), e);
    }
  }

  public WKResultWrapper getAdres(BaseWKExt wk) {

    String codeObject = wk.getBasisWk().getCode_object().getCode();
    String dEnd = wk.getBasisWk().getDatum_einde().getCode();
    String vEnd = wk.getBasisWk().getVolgcode_einde().getCode();

    ZoekArgumenten argumenten = new ZoekArgumenten();
    argumenten.setCode_object(codeObject);
    argumenten.setDatum_einde(dEnd);
    argumenten.setVolgcode_einde(vEnd);

    return getAdres(argumenten, false);
  }

  public WKResultWrapper getAdres(ZoekArgumenten searchArgumenten, boolean isZoekSpecifiek) {

    if (searchArgumenten == null || !searchArgumenten.isGevuld()) {
      throw new ProException(ENTRY, ERROR, "Geen zoekargumenten voor zoeken adres");
    }

    if (isZoekSpecifiek) {
      if (emp(searchArgumenten.getHuisletter())) {
        searchArgumenten.setHuisletter(" ");
      }

      if (emp(searchArgumenten.getHuisnummertoevoeging())) {
        searchArgumenten.setHuisnummertoevoeging(" ");
      }

      if (emp(searchArgumenten.getHuisnummeraanduiding())) {
        searchArgumenten.setHuisnummeraanduiding(" ");
      }
    }

    searchArgumenten.setExtra_pl_gegevens(false);

    WKResultWrapper resultaat;
    Credentials credentials = getCredentials(ZoekProfielType.PROFIEL_STANDAARD);
    String username = credentials.getUsername();
    String password = credentials.getPassword();
    WKHTTPClient client = new WKHTTPClient(getURL() + "/gba");

    WoningkaartOpslagEntry entry = getOpslag().get(new WoningkaartOpslagEntry(searchArgumenten));

    if (entry == null) {
      resultaat = new WKResultWrapper(client.find(searchArgumenten, new PLELoginArgs(username, password)));
      getOpslag().set(new WoningkaartOpslagEntry(searchArgumenten, resultaat));
    } else {
      resultaat = entry.getWk();
    }

    if (isZoekSpecifiek && resultaat.getBasisWkWrappers().size() > 1) {
      ZoekArgumenten nz = MiscUtils.copy(searchArgumenten, ZoekArgumenten.class);
      BaseWKExt rwk = specificeerAdres(resultaat);
      nz.setCode_object(rwk.getBasisWk().getCode_object().getValue());
      nz.setVolgcode_einde(rwk.getBasisWk().getVolgcode_einde().getValue());
      nz.setDatum_einde(rwk.getBasisWk().getDatum_einde().getValue());

      WoningkaartOpslagEntry historyEntry = getOpslag().get(new WoningkaartOpslagEntry(searchArgumenten));
      if (historyEntry == null) {
        resultaat = new WKResultWrapper(
            client.find(searchArgumenten, new PLELoginArgs(username, password)));
        getOpslag().set(new WoningkaartOpslagEntry(searchArgumenten, resultaat));
      } else {
        resultaat = historyEntry.getWk();
      }
    }

    sort(resultaat);

    for (BaseWKMessage melding : resultaat.getResultaat().getMessages()) {
      throw new ProException(WEBSERVICE, ERROR, melding.getCode() + " = " + melding.getWaarde());
    }

    return resultaat;
  }

  public List<GbaWsRestGbavAccount> getGbavAccounts(ZoekProfielType type) {
    try {
      return getPersonenWsClient(type).getGbav().getAccounts().getAccounts();
    } catch (RuntimeException e) {
      throw new ProException("Fout bij opvragen GBA-V account", e);
    }
  }

  public BasePLExt getHuidige() {
    BasePLExt pl = getOpslag().getHuidige();
    if (pl == null) {
      throw new ProException(NO_RESULTS, ERROR, "Geen persoonslijst gevonden.");
    }
    return pl;
  }

  /**
   * Zoek alleen de identificatienummers op
   */
  public List<BasePLValue> getNummers(String nr) {
    List<BasePLValue> nummers = new ArrayList<>();

    if (Bsn.isCorrect(nr)) {
      Bsn format = new Bsn(nr);
      nummers.add(new BasePLValue(format.getDefaultBsn(), format.getFormatBsn()));

    } else if (Anr.isCorrect(nr)) {
      Anr format = new Anr(nr);
      nummers.add(new BasePLValue(format.getAnummer(), format.getFormatAnummer()));
    }

    PLEArgs args = new PLEArgs();
    args.addNummer(nr);
    args.addCat(GBACat.PERSOON, GBACat.VERW);
    args.setShowHistory(false);

    List<BasePLValue> nrs = new ArrayList<>();

    for (BasePLExt basispl : getPersoonslijsten(args, false).getBasisPLWrappers()) {

      nrs.add(basispl.getPersoon().getAnr());
      nrs.add(basispl.getVerwijzing().getAnr());

      nrs.add(basispl.getPersoon().getBsn());
      nrs.add(basispl.getVerwijzing().getBsn());
    }

    for (BasePLValue w : nrs) {
      if (Anr.isCorrect(w.getVal()) || Bsn.isCorrect(w.getVal())) {
        if (!nummers.contains(w)) {
          nummers.add(w);
        }
      }
    }

    return nummers;
  }

  public PersonenWsOpslag getOpslag() {
    return opslag;
  }

  public void setOpslag(PersonenWsOpslag opslag) {
    this.opslag = opslag;
  }

  public PersonenWsClient getPersonenWsClient(ZoekProfielType type) {
    return new PersonenWsClient(getURL(), "Personen-ws", getCredentials(type));
  }

  public BasePLExt getPersoonslijst(BasePLExt pl) {
    return getPersoonslijst(pl, false, PLEDatasource.STANDAARD);
  }

  public BasePLExt getPersoonslijst(BasePLExt pl, boolean isSpecifiek) {
    return getPersoonslijst(pl, isSpecifiek, PLEDatasource.STANDAARD);
  }

  public BasePLExt getPersoonslijst(boolean isSpecifiek, String... nummers) {
    return getPersoonslijst(isSpecifiek, PLEDatasource.STANDAARD, nummers);
  }

  @Override
  public BasePLExt getPersoonslijst(String... nummers) {
    return getPersoonslijst(false, PLEDatasource.STANDAARD, nummers);
  }

  public BasePLExt getPersoonslijst(boolean isSpecifiek, PLEDatasource databron, String... nummers) {

    PLEArgs argumenten = new PLEArgs();
    argumenten.setDatasource(databron);

    if (nummers != null) {
      for (String nummer : nummers) {
        if (fil(nummer)) {
          if (Bsn.isCorrect(nummer)) {
            argumenten.addNummer(nummer);
          } else if (Anr.isCorrect(nummer)) {
            argumenten.addNummer(nummer);
          }
        }
      }
    }

    if (argumenten.getNumbers().isEmpty()) {
      throw new ProException(ENTRY, WARNING, "Geen BSN of A-nummer om op te zoeken.");
    }

    PLResultComposite resultaat = getPersoonslijsten(argumenten, isSpecifiek);
    if (resultaat.getBasisPLWrappers().size() > 0) {
      return resultaat.getBasisPLWrappers().get(0);
    }

    return new BasePLExt();
  }

  /**
   * Specifiek gezocht houdt in dat de gebruik bewust heeft gezocht naar deze persoon
   */
  public PLResultComposite getPersoonslijsten(PLEArgs args, boolean isSpecifiek) {

    // Alleen voornamen, omdraaien naam en voornamen dus.
    if ("-".equals(args.getGeslachtsnaam()) && fil(args.getVoornaam())) {
      args.setGeslachtsnaam(args.getVoornaam());
      args.setVoornaam("");
    }

    ZoekProfielType profielType = getZoekProfielType(args);
    List<BasePLExt> cachedEntries = new ArrayList<>();
    List<PLNumber> cachedNumbers = new ArrayList<>();
    PLEDatasource databron = args.getDatasource();

    // Alle nummers die al zijn geladen niet zoeken
    // Tenzij er ook andere zoekgegevens zijn.
    if (args.getNumbers().size() > 0 && !args.isNawGevuld()) {
      for (PLNumber nr : args.getNumbers()) {
        PersoonslijstOpslagEntry entry = getOpslag().get(
            new PersoonslijstOpslagEntry(profielType, databron, args.isSearchIndications(), nr.getNummer()),
            isSpecifiek);

        if (entry != null) {
          cachedEntries.add(entry.getPl());
          cachedNumbers.add(nr);
        }
      }

      // Nummers gevonden in cache uit ZoekNawArgumenten verwijderen.
      for (PLNumber nummer : cachedNumbers) {
        args.getNumbers().remove(nummer);
      }
    }

    PLResultComposite resultaat = new PLResultComposite(new PLEResult());

    // Alleen zoeken als er nummers zijn of er andere velden gevuld zijn.
    if ((args.getNumbers().size() > 0) || args.isNawGevuld()) {
      resultaat = getPersoonslijstWrapper(args);
    }

    // Als er gezocht is op nummers, dan aanmerken als huidig
    if (isAlleenNummers(args)) {
      for (BasePLExt wrapper : resultaat.getBasisPLWrappers()) {
        getOpslag().set(new PersoonslijstOpslagEntry(profielType, databron,
            args.isSearchIndications(), wrapper), isSpecifiek);
      }
    }

    resultaat.getBasisPLWrappers().addAll(cachedEntries);
    sort(resultaat);
    return resultaat;
  }

  public RelatieLijst getRelatieLijst(BasePLExt pl, boolean toonOverleden) {
    return RelatieLijstHandler.zoek(pl, toonOverleden, this);
  }

  /**
   * Is GBA-V aangezet in de parameters?
   */
  public boolean isGbavZoeken() {
    return isZoeken(ParameterConstant.ZOEK_PLE_BRON_GBAV, ProfielActie.SELECT_HOOFD_GBAV);
  }

  /**
   * Is gemeentelijk zoeken aangezet in de parameters?
   */
  public boolean isGemeentelijkZoeken() {
    return isZoeken(ParameterConstant.ZOEK_PLE_BRON_GEMEENTE, ProfielActie.SELECT_HOOFD_GEMEENTELIJK);
  }

  public void gbavAccountUnblock(GbaWsRestGbavAccount account) {
    GbaWsRestGbavDeblokkerenVraag vraag = new GbaWsRestGbavDeblokkerenVraag(account.getCode());

    try {
      getPersonenWsClient(PROFIEL_STANDAARD).getGbav().accountDeblokkeren(vraag);
    } catch (RuntimeException e) {
      throw new ProException("Fout bij deblokkeren", e);
    }
  }

  /**
   * Update GBA-V account with current password.
   *
   * @param account GBA-V account
   * @param date date in string format: yyyymmdd
   * @param password current password
   * @return true when successful otherwise false
   */
  public boolean gbavAccountUpdatePassword(GbaWsRestGbavAccount account, String date, String password) {
    GbaWsRestGbavAccountUpdatenVraag vraag = new GbaWsRestGbavAccountUpdatenVraag();
    vraag.setCode(account.getCode());
    vraag.setDatum(aval(date));
    vraag.setWachtwoord(password);

    GbaWsRestGbavAccountUpdatenAntwoord response;
    try {
      response = getPersonenWsClient(PROFIEL_STANDAARD).getGbav().accountUpdaten(vraag);
    } catch (RuntimeException e) {
      throw new ProException("Fout bij updaten account", e);
    }

    return response.getMeldingen().isEmpty();
  }

  public String gbavGeneratePassword() {
    try {
      return getPersonenWsClient(PROFIEL_STANDAARD).getGbav().wachtwoordGenereren().getWachtwoord();
    } catch (RuntimeException e) {
      throw new ProException("Fout bij genereren wachtwoord", e);
    }
  }

  public boolean gbavAccountSetPassword(GbaWsRestGbavAccount account, String password) {
    GbaWsRestGbavWachtwoordVersturenVraag vraag = new GbaWsRestGbavWachtwoordVersturenVraag(account.getCode(),
        password);
    GbaWsRestGbavWachtwoordVersturenAntwoord response;
    try {
      response = getPersonenWsClient(PROFIEL_STANDAARD).getGbav().wachtwoordVersturen(vraag);
    } catch (RuntimeException e) {
      throw new ProException("Fout bij wijzigen wachtwoord", e);
    }

    return response.getMeldingen().isEmpty();
  }

  protected PLEResult findPersonData(PLEArgs commandArgs) {
    ZoekProfielType profielType = getZoekProfielType(commandArgs);
    Credentials credentials = getCredentials(profielType);
    String username = credentials.getUsername();
    String password = credentials.getPassword();
    PLEHTTPClient client = new PLEHTTPClient(getURL() + "/gba");
    return client.find(commandArgs, new PLELoginArgs(username, password));
  }

  private BasePLExt getPersoonslijst(BasePLExt pl, boolean isSpecifiek, PLEDatasource databron) {
    String nummer = pl.getPersoon().getNummer().getCode();
    return getPersoonslijst(isSpecifiek, databron, nummer);
  }

  /**
   * Zoek op basis van PleArgumenten
   */
  private PLResultComposite getPersoonslijstWrapper(PLEArgs args) {
    // Als standaard gezocht wordt dan controle op aanstaan BRP.
    if (args.getDatasource() == PLEDatasource.STANDAARD) {
      if (isGemeentelijkZoeken() && !isGbavZoeken()) {
        args.setDatasource(PLEDatasource.PROCURA);
      }
    }

    PLEResult resultaat;

    try {
      LOGGER.debug("PersonenWS - NEW - PL wordt opgevraagd " + args.getNumbers());
      resultaat = findPersonData(args);
    } catch (Exception e) {
      throw new ProException(WEBSERVICE, ERROR, "Fout bij aanroepen van de GBA webservice", e);
    }

    for (PLEMessage message : resultaat.getMessages()) {
      if (message.getCode() != MAX_AANTAL_ZOEKRESULTATEN) {
        if (message.getCode() >= GBAV_INFO) {
          throw new ProException(WEBSERVICE, INFO, parseMelding(message, ""));
        }
        if (message.isCode(INCORRECTE_PERSOON_ID_1, INCORRECTE_PERSOON_ID_2)) {
          throw new ProException(WEBSERVICE, WARNING, parseMelding(message,
              "Waarschijnlijk dient u meer zoekargumenten in te geven."));
        }

        throw new ProException(WEBSERVICE, ERROR, parseMelding(message, ""));
      }
    }

    // filteren en sorteren
    return new PLEResultFilter().filter(getServices(), sort(new PLResultComposite(resultaat)));
  }

  private ZoekProfielType getZoekProfielType(PLEArgs args) {
    boolean isNummers = !args.getNumbers().isEmpty();
    boolean isOverigeArgs = args.isNawGevuld();

    if (!isNummers && !isOverigeArgs) {
      throw new ProException(ENTRY, WARNING, "Geen zoekargumenten ingegeven.");
    }

    return (isNummers && !isOverigeArgs) ? PROFIEL_GBAV_PLUS : PROFIEL_STANDAARD;
  }

  private boolean isAlleenNummers(PLEArgs args) {
    boolean isNummers = args.getNumbers().size() > 0;
    boolean isTemplate = fil(args.getCustomTemplate());
    boolean isAlleCategorieen = args.getCategories().isEmpty();

    return isNummers && !isTemplate && isAlleCategorieen;
  }

  private boolean isZoeken(ParameterConstant parm, ProfielActie profielActie) {
    boolean isParm = isTru(getServices().getParameterService().getParm(parm));
    boolean isProf = getServices().getGebruiker().getProfielen().isProfielActie(profielActie);
    return isParm && isProf;
  }

  private String parseMelding(PLEMessage message, String extra) {
    return message.getDescr() + " (code: " + message.getCode() + ")" + (fil(extra) ? ("</br>" + extra) : "");
  }

  private PLResultComposite sort(PLResultComposite wrapper) {
    wrapper.getBasisPLWrappers().sort(new PLExtSorter());
    return wrapper;
  }

  private WKResultWrapper sort(WKResultWrapper wrapper) {
    wrapper.getBasisWkWrappers().sort(new WKWrapperSorter());
    for (BaseWKExt wk : wrapper.getBasisWkWrappers()) {
      wk.getBasisWk().getPersonen().sort(new WKPersonWrapperSorter());
    }

    return wrapper;
  }

  private BaseWKExt specificeerAdres(WKResultWrapper result) {

    BaseWKExt adres = null;

    int cObj = 0;
    int vObj = 0;

    for (BaseWKExt a : result.getBasisWkWrappers()) {

      int dEnd = aval(a.getBasisWk().getDatum_einde().getValue());
      int volg = aval(a.getBasisWk().getVolgcode_einde().getValue());
      int obj = aval(a.getBasisWk().getCode_object().getValue());

      if (cObj > 0 && pos(dEnd)) {
        continue;
      }

      // Actueel en hogere einddatum
      if (cObj > 0 && !pos(dEnd) && (volg > vObj)) {
        continue;
      }

      cObj = obj;
      vObj = volg;
      adres = a;
    }

    return adres;
  }
}
