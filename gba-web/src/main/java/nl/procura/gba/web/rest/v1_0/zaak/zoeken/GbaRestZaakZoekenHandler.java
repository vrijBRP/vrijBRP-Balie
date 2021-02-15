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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;
import static nl.procura.gba.web.rest.v1_0.zaak.GbaRestElementHandler.add;
import static nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakVraagType.*;
import static nl.procura.gba.web.services.zaken.algemeen.ZaakSortering.DATUM_INGANG_OUD_NIEUW;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.rest.v1_0.GbaRestHandler;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.*;
import nl.procura.gba.web.rest.v1_0.zaak.attribuut.GbaRestZaakAttribuut;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.afstamming.erkenning.GbaRestErkenningHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.afstamming.geboorte.GbaRestGeboorteHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.afstamming.naamskeuze.GbaRestNaamskeuzeHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.huwelijk.GbaRestHuwelijkHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.omzetting.GbaRestOmzettingHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.onderzoek.GbaRestOnderzoekHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.ontbinding.GbaRestOntbindingHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.overlijden.gemeente.GbaRestOverlijdenGemeenteHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.overlijden.levenloos.GbaRestLevenloosHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.overlijden.lijkvinding.GbaRestLijkvindingHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.registration.GbaRestRegistrationHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.correspondentie.GbaRestCorrespondentieHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.covog.GbaRestVogHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.geheimhouding.GbaRestGeheimhoudingHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.gpk.GbaRestGpkHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.gv.GbaRestGvHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.inbox.GbaRestInboxHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.indicatie.GbaRestIndicatieHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.inhouding.GbaRestInhoudingVermissingHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.naamgebruik.GbaRestNaamgebruikHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.personmutations.GbaRestPersonMutationHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.reisdocument.GbaRestReisdocumentHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.rijbewijs.GbaRestRijbewijsHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.riskanalysis.GbaRestRiskAnalysisHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.terugmelding.GbaRestTerugmeldingHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.uittreksel.GbaRestUittrekselHandler;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.verhuizing.GbaRestVerhuizingHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.zaken.algemeen.*;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.AantekeningHistorie;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekening;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningHistorie;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.AttribuutHistorie;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuut;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdType;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificatie;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatie;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelaties;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.contact.PlContactgegeven;
import nl.procura.standard.exceptions.ProException;
import nl.procura.validation.Anummer;
import nl.procura.validation.Bsn;

public class GbaRestZaakZoekenHandler extends GbaRestHandler {

  public GbaRestZaakZoekenHandler(Services services) {
    super(services);
  }

  /**
   * Geeft alle ZaakArgumenten terug
   */
  public ZaakArgumenten getAlleZaakArgumenten(ZaakArgumenten za) {
    za.getTypen().addAll(ZaakType.getAll());
    za.getStatussen().addAll(ZaakStatusType.getAll());
    return za;
  }

  /**
   * Vertaal de REST vraag naar ZaakArgumentne
   */
  public ZaakArgumenten getZaakArgumenten(GbaRestZaakVraag vraag) {

    ZaakArgumenten za = new ZaakArgumenten();

    // Als er een zaakId is met 1 type dan die combineren
    if (fil(vraag.getZaakId()) && vraag.getTypen().size() == 1) {
      za.addZaakKey(new ZaakKey(vraag.getZaakId(), ZaakType.get(vraag.getTypen().get(0).getCode())));
    } else {
      za.addZaakKey(new ZaakKey(vraag.getZaakId()));
      for (GbaRestZaakType type : vraag.getTypen()) {
        za.getTypen().add(ZaakType.get(type.getCode()));
      }
    }

    // Zoeken op persoonnummer
    if (fil(vraag.getPersoonsnummer())) {
      Bsn bsn = new Bsn(vraag.getPersoonsnummer());
      if (bsn.isCorrect()) {
        za.setNummer(bsn.getDefaultBsn());
      } else {
        Anummer anr = new Anummer(vraag.getPersoonsnummer());
        if (anr.isCorrect()) {
          za.setNummer(anr.getAnummer());
        }
      }
    }

    if (vraag.getZaakIdType() != null) {
      String code = vraag.getZaakIdType().getCode();
      za.setZaakIdType(ZaakIdType.get(code));
    }

    for (GbaRestZaakStatus status : vraag.getStatussen()) {
      za.getStatussen().add(ZaakStatusType.get(status.getCode()));
    }

    for (GbaRestZaakAttribuut attribuut : vraag.getAttributen()) {
      if (attribuut.isBestaat()) {
        za.addAttributen(attribuut.getWaarde());
      } else {
        za.addOntbrekendeAttributen(attribuut.getWaarde());
      }
    }

    if (pos(vraag.getDatumVanaf())) {
      za.setdInvoerVanaf(vraag.getDatumVanaf());
    }

    if (pos(vraag.getDatumTm())) {
      za.setdInvoerTm(vraag.getDatumTm());
    }

    if (pos(vraag.getDatumIngangVanaf())) {
      za.setdIngangVanaf(vraag.getDatumIngangVanaf());
    }

    if (pos(vraag.getDatumIngangTm())) {
      za.setdIngangTm(vraag.getDatumIngangTm());
    }

    if (pos(vraag.getDatumMutatieVanaf())) {
      za.setdMutatieVanaf(vraag.getDatumMutatieVanaf());
    }

    if (pos(vraag.getDatumMutatieTm())) {
      za.setdMutatieTm(vraag.getDatumMutatieTm());
    }

    if (pos(vraag.getMaxAantal())) {
      za.setMax(vraag.getMaxAantal());
    }

    if (vraag.getSortering() != null) {
      za.setSortering(ZaakSortering.get(vraag.getSortering().getCode(), DATUM_INGANG_OUD_NIEUW));
    }

    return za;
  }

  /**
   * Geeft zaken terug als REST objecten
   */
  public List<GbaRestElement> getZaakSleutels(GbaRestZaakVraag vraag) {

    List<GbaRestElement> zaken = new ArrayList<>();

    for (ZaakKey zaakKey : getServices().getZakenService().getZaakKeys(getZaakArgumenten(vraag))) {

      GbaRestElement gbaZaak = new GbaRestElement(GbaRestElementType.ZAAK);
      GbaRestElement algemeen = gbaZaak.add(GbaRestElementType.ALGEMEEN);

      add(algemeen, ZAAKID, zaakKey.getZaakId());
      add(algemeen, TYPE, zaakKey.getZaakType().getCode(), zaakKey.getZaakType().getOms());
      add(algemeen, DATUM_INGANG, zaakKey.getdIng(), date2str(zaakKey.getdIng().longValue()));
      add(algemeen, DATUM_INVOER, zaakKey.getdInv(), date2str(zaakKey.getdInv().longValue()));
      add(algemeen, TIJD_INVOER, zaakKey.gettInv(), time2str(zaakKey.gettInv().toString()));

      zaken.add(gbaZaak);
    }

    return zaken;
  }

  /**
   * Geeft zaken terug als REST objecten
   */
  public List<GbaRestElement> getZaken(GbaRestZaakVraag vraag) {

    List<GbaRestElement> zaken = new ArrayList<>();
    GbaRestZaakVraagType vraagType = vraag.getVraagType();

    for (Zaak zaak : getZaken(getZaakArgumenten(vraag), vraagType)) {

      // Zaaksysteem identificaties opvragen voor MINIMAAL
      if (vraagType != null && vraagType.is(MINIMAAL)) {
        if (!zaak.getZaakHistorie().getIdentificaties().exists()) {
          zaak.getZaakHistorie()
              .setIdentificaties(getServices().getZaakIdentificatieService().getIdentificaties(zaak));
        }
      }

      GbaRestElement gbaZaak = new GbaRestElement(GbaRestElementType.ZAAK);
      GbaRestElement algemeen = gbaZaak.add(GbaRestElementType.ALGEMEEN);

      Locatie l = zaak.getLocatieInvoer();
      UsrFieldValue g = zaak.getIngevoerdDoor();

      add(algemeen, ZAAKID, zaak.getZaakId());
      add(algemeen, TYPE, zaak.getType().getCode(), zaak.getType().getOms());
      add(algemeen, SOORT, zaak.getSoort());
      add(algemeen, STATUS, zaak.getStatus().getCode(), zaak.getStatus().getOms());
      add(algemeen, BRON, zaak.getBron());
      add(algemeen, LEVERANCIER, zaak.getLeverancier());
      add(algemeen, DATUM_INGANG, zaak.getDatumIngang().getLongDate(), zaak.getDatumIngang().getFormatDate());
      add(algemeen, DATUM_INVOER, zaak.getDatumTijdInvoer().getLongDate(),
          zaak.getDatumTijdInvoer().getFormatDate());
      add(algemeen, TIJD_INVOER, zaak.getDatumTijdInvoer().getLongTime(),
          zaak.getDatumTijdInvoer().getFormatTime());
      add(algemeen, LOCATIE_INVOER, l.getCLocation(), l.getLocatie(), l.getOmschrijving());
      add(algemeen, GEBRUIKER_INVOER, g.getValue(), g.getDescription());

      // Algemene databasegegevens.
      addSysteemIdentificaties(zaak, algemeen);
      addStatussen(zaak, algemeen);
      addAttributen(zaak, algemeen);
      addGerelateerdeZaken(zaak, algemeen);

      if (vraagType != null && vraagType.is(STANDAARD, MAXIMAAL)) {

        addAantekeningen(zaak, algemeen);
        addContactgegevens(zaak, algemeen);

        add(algemeen, BSN, zaak.getBurgerServiceNummer().getValue(),
            zaak.getBurgerServiceNummer().getDescription());
        add(algemeen, ANR, zaak.getAnummer().getValue(), zaak.getAnummer().getDescription());

        switch (zaak.getType()) {
          case CORRESPONDENTIE:
            new GbaRestCorrespondentieHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case GPK:
            new GbaRestGpkHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case INDICATIE:
            new GbaRestIndicatieHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case NAAMGEBRUIK:
            new GbaRestNaamgebruikHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case UITTREKSEL:
            new GbaRestUittrekselHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case VERHUIZING:
            new GbaRestVerhuizingHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case VERSTREKKINGSBEPERKING:
            new GbaRestGeheimhoudingHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case RIJBEWIJS:
            new GbaRestRijbewijsHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case REISDOCUMENT:
            new GbaRestReisdocumentHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case OVERLIJDEN_IN_GEMEENTE:
            new GbaRestOverlijdenGemeenteHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case LEVENLOOS:
            new GbaRestLevenloosHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case LIJKVINDING:
            new GbaRestLijkvindingHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case TERUGMELDING:
            new GbaRestTerugmeldingHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case COVOG:
            new GbaRestVogHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case GEBOORTE:
            new GbaRestGeboorteHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case ERKENNING:
            new GbaRestErkenningHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case NAAMSKEUZE:
            new GbaRestNaamskeuzeHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case HUWELIJK_GPS_GEMEENTE:
            new GbaRestHuwelijkHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case OMZETTING_GPS:
            new GbaRestOmzettingHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case ONTBINDING_GEMEENTE:
            new GbaRestOntbindingHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case INHOUD_VERMIS:
            new GbaRestInhoudingVermissingHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case INBOX:
            new GbaRestInboxHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case ONDERZOEK:
            new GbaRestOnderzoekHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case RISK_ANALYSIS:
            new GbaRestRiskAnalysisHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case REGISTRATION:
            new GbaRestRegistrationHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case PL_MUTATION:
            new GbaRestPersonMutationHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case GEGEVENSVERSTREKKING:
            new GbaRestGvHandler(getServices()).convert(gbaZaak, zaak);
            break;

          case OVERLIJDEN_IN_BUITENLAND:
            throw new ProException(ERROR, zaak.getType() + " is nog niet uitgewerkt");

          case LEGE_PERSOONLIJST:
          case ONBEKEND:
          default:
            throw new ProException(ERROR, "Onbekende zaaktype:  " + zaak.getType());
        }
      }

      zaken.add(gbaZaak);
    }

    return zaken;
  }

  /**
   * Zoek zaken in Proweb Personen
   */
  public List<Zaak> getZaken(ZaakArgumenten za, GbaRestZaakVraagType vraagType) {

    ZakenService zdb = getServices().getZakenService();
    List<Zaak> totaalLijst = new ArrayList<>();
    List<Zaak> zaken = zdb.getMinimaleZaken(za);

    if (MAXIMAAL.is(vraagType)) {
      totaalLijst.addAll(getServices().getZakenService().getVolledigeZaken(zaken));
    } else if (STANDAARD.is(vraagType)) {
      totaalLijst.addAll(getServices().getZakenService().getStandaardZaken(zaken));
    } else {
      totaalLijst.addAll(zaken);
    }

    return totaalLijst;
  }

  /**
   * Werk de status in PROWEB Personen bij
   */

  public void updateStatus(GbaRestZaakStatusUpdateVraag vraag) {

    ZaakArgumenten za = new ZaakArgumenten(vraag.getZaakId());
    ZaakStatusType nieuweStatus = ZaakStatusType.get(vraag.getStatus().getCode());

    if (nieuweStatus == ZaakStatusType.ONBEKEND) {
      throw new ProException(ERROR, "Onbekende zaaktype:  " + vraag.getStatus());
    }

    for (Zaak zaak : getZaken(za, GbaRestZaakVraagType.STANDAARD)) {
      getServices().getZakenService()
          .getService(zaak)
          .updateStatus(zaak, zaak.getStatus(), nieuweStatus, vraag.getOpmerking());
    }
  }

  /**
   * Voegt de aantekeningen toe
   */
  private void addAantekeningen(Zaak zaak, GbaRestElement algemeen) {

    AantekeningHistorie historie = zaak.getZaakHistorie().getAantekeningHistorie();

    if (!historie.exists()) {
      GbaRestElement systemen = algemeen.add(AANTEKENINGEN);
      for (PlAantekening aantekening : historie.getAantekeningen()) {

        GbaRestElement element = systemen.add(AANTEKENING);
        PlAantekeningHistorie laatste = aantekening.getLaatsteHistorie();
        DateTime tijdstip = laatste.getTijdstip();

        add(element, GEBRUIKER, laatste.getGebruiker().getDescription());
        add(element, DATUM_INVOER, tijdstip.getLongDate(), tijdstip.getFormatDate());
        add(element, TIJD_INVOER, tijdstip.getLongTime(), tijdstip.getFormatTime());
        add(element, ONDERWERP, laatste.getOnderwerp());
        add(element, INHOUD, laatste.getInhoud());
      }
    }
  }

  /**
   * Voegt de attributen toe
   */
  private void addAttributen(Zaak zaak, GbaRestElement algemeen) {

    AttribuutHistorie historie = zaak.getZaakHistorie().getAttribuutHistorie();

    if (historie.size() > 0) {
      GbaRestElement systemen = algemeen.add(ATTRIBUTEN);
      for (ZaakAttribuut attribuut : historie.getAttributen()) {
        add(systemen.add(ATTRIBUUT), NAAM, attribuut.getAttribuut());
      }
    }
  }

  /**
   * Voegt de gekoppelde zaken toe
   */
  private void addGerelateerdeZaken(Zaak zaak, GbaRestElement algemeen) {

    ZaakRelaties relaties = zaak.getZaakHistorie().getRelaties();
    if (relaties.size() > 0) {
      GbaRestElement systemen = algemeen.add(RELATIES);
      for (ZaakRelatie zaakRelatie : relaties.getRelaties()) {
        GbaRestElement relatie = systemen.add(RELATIE);
        ZaakType gerelateerdZaakType = zaakRelatie.getGerelateerdZaakType();
        add(relatie, RELATIE_ZAAK_ID, zaakRelatie.getGerelateerdZaakId());
        add(relatie, RELATIE_ZAAK_TYPE, gerelateerdZaakType.getCode(), gerelateerdZaakType.getOms());
      }
    }
  }

  /**
   * Voegt de contactgegevens toe
   */
  private void addContactgegevens(Zaak zaak, GbaRestElement algemeen) {

    List<PlContactgegeven> gegevens = getServices().getContactgegevensService().getContactgegevens(zaak);

    if (!gegevens.isEmpty()) {

      GbaRestElement contact = algemeen.add(GbaRestElementType.CONTACT);
      GbaRestElement telefoon = contact.add(GbaRestElementType.TELEFOON);

      for (PlContactgegeven aant : gegevens) {

        String gegeven = aant.getContactgegeven().getGegeven();
        String waarde = aant.getAant();

        if (ContactgegevensService.TEL_MOBIEL.equalsIgnoreCase(gegeven)) {
          telefoon.add(MOBIEL).set(waarde);
        } else if (ContactgegevensService.TEL_THUIS.equalsIgnoreCase(gegeven)) {
          telefoon.add(THUIS).set(waarde);
        } else if (ContactgegevensService.TEL_WERK.equalsIgnoreCase(gegeven)) {
          telefoon.add(WERK).set(waarde);
        } else if (ContactgegevensService.EMAIL.equalsIgnoreCase(gegeven)) {
          contact.add(EMAIL).set(waarde);
        }
      }
    }
  }

  /**
   * Voegt de huidige status toe
   */
  private void addStatussen(Zaak zaak, GbaRestElement algemeen) {

    ZaakStatus zaakStatus = zaak.getZaakHistorie().getStatusHistorie().getHuidigeStatus();

    if (!zaak.getZaakHistorie().getStatusHistorie().getStatussen().isEmpty()) {

      GbaRestElement statussen = algemeen.add(STATUSSEN);

      if (pos(zaakStatus.getCode())) {

        GbaRestElement status = statussen.add(STATUS);
        DateTime datumTijdInvoer = zaakStatus.getDatumTijdInvoer();
        add(status, STATUS, zaakStatus.getStatus().getCode(), zaakStatus.getStatus().getOms());
        add(status, DATUM_INVOER, datumTijdInvoer.getLongDate(), datumTijdInvoer.getFormatDate());
        add(status, TIJD_INVOER, datumTijdInvoer.getLongTime(), datumTijdInvoer.getFormatTime());
        add(status, OPMERKING, zaakStatus.getOpmerking());

        long codeGebruiker = zaakStatus.getIngevoerdDoor().getLongValue();
        Gebruiker gebruiker = getServices().getGebruikerService().getGebruikerByCode(codeGebruiker, false);
        getServices().getGebruikerService().setInformatie(gebruiker);

        if (gebruiker.isStored()) {
          GbaRestElement gebruikerElement = status.add(GEBRUIKER);
          add(gebruikerElement, CODE, gebruiker.getCUsr());
          add(gebruikerElement, GEBRUIKERSNAAM, gebruiker.getGebruikersnaam());
          add(gebruikerElement, NAAM, gebruiker.getNaam());
          add(gebruikerElement, EMAIL, gebruiker.getEmail());
          add(gebruikerElement, TELEFOON, gebruiker.getTelefoonnummer());
          add(gebruikerElement, TOELICHTING, gebruiker.getOmschrijving());
        }
      }
    }
  }

  /**
   * Voegt de identificaties van andere systemen toe
   */
  private void addSysteemIdentificaties(Zaak zaak, GbaRestElement algemeen) {

    List<ZaakIdentificatie> identificaties = zaak.getZaakHistorie().getIdentificaties().getNummers();

    if (!identificaties.isEmpty()) {

      GbaRestElement systemen = algemeen.add(SYSTEMEN);

      for (ZaakIdentificatie zaakId : identificaties) {

        GbaRestElement systeem = systemen.add(SYSTEEM);
        ZaakIdType zaakIdType = zaakId.getZaakIdType();

        add(systeem, TYPE, zaakIdType.getCode(), zaakIdType.getOms());
        add(systeem, ZAAKID, zaakId.getExternId());
      }
    }
  }
}
