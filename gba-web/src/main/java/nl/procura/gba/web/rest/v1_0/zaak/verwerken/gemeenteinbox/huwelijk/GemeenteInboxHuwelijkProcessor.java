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

package nl.procura.gba.web.rest.v1_0.zaak.verwerken.gemeenteinbox.huwelijk;

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.util.ArrayList;
import java.util.List;

import nl.procura.brp.inbox.soap.endpoints.v1.huwelijksplanner.reservering.vraag.Ambtenaar;
import nl.procura.brp.inbox.soap.endpoints.v1.huwelijksplanner.reservering.vraag.Ambtenaren;
import nl.procura.brp.inbox.soap.endpoints.v1.huwelijksplanner.reservering.vraag.Naamgebruik;
import nl.procura.brp.inbox.soap.endpoints.v1.huwelijksplanner.reservering.vraag.Optie;
import nl.procura.brp.inbox.soap.endpoints.v1.huwelijksplanner.reservering.vraag.Opties;
import nl.procura.brp.inbox.soap.endpoints.v1.huwelijksplanner.reservering.vraag.Partner;
import nl.procura.brp.inbox.soap.endpoints.v1.huwelijksplanner.reservering.vraag.Partners;
import nl.procura.brp.inbox.soap.endpoints.v1.huwelijksplanner.reservering.vraag.Reservering;
import nl.procura.brp.inbox.soap.endpoints.v1.huwelijksplanner.reservering.vraag.Verbintenis;
import nl.procura.brp.inbox.soap.endpoints.v1.huwelijksplanner.reservering.vraag.Verbintenissoort;
import nl.procura.brp.inbox.soap.endpoints.v1.huwelijksplanner.reservering.vraag.Vragen;
import nl.procura.brp.inbox.soap.endpoints.v1.huwelijksplanner.reservering.vraag.Zaak;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.DateTime.TimeType;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.bs.huwelijk.processen.HuwelijkProcessen;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.inbox.GbaRestInboxVerwerkingType;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.DossierService;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijkOptie;
import nl.procura.gba.web.services.bs.huwelijk.HuwelijkService;
import nl.procura.gba.web.services.gba.basistabellen.huwelijksambtenaar.HuwelijksAmbtenaar;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatie;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatieOptie.HuwelijksLocatieOptie;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessingResult;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessor;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.ZaakCommentaren;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdType;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificatie;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.gemeenteinbox.GemeenteInboxRecord;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.validation.Bsn;

public class GemeenteInboxHuwelijkProcessor extends CaseProcessor<GemeenteInboxRecord> {

  public GemeenteInboxHuwelijkProcessor(Services services) {
    super(services);
  }

  @Override
  public CaseProcessingResult process(GemeenteInboxRecord inboxRecord) {
    Vragen vragen = fromStream(inboxRecord, Vragen.class);
    Zaak vraagZaak = vragen.getVraag().getZaak();
    Verbintenis vraagVerbintenis = vragen.getVraag().getVerbintenis();
    Opties vraagOpties = vraagVerbintenis.getOpties();
    Partners vraagPartners = vraagVerbintenis.getPartners();
    Reservering vraagReservering = vraagVerbintenis.getReservering();
    Verbintenissoort verbintenissoort = vraagVerbintenis.getVerbintenissoort();

    HuwelijkService service = getServices().getHuwelijkService();
    Dossier dossier = (Dossier) service.getNewZaak();
    DossierHuwelijk huwelijk = (DossierHuwelijk) dossier.getZaakDossier();

    String relatieZaakId = dossier.getZaakId();
    String inboxInterneZaakId = inboxRecord.getZaakId();
    String inboxExternZaakId = inboxRecord.getZaakIdExtern();

    log(INFO, "Verwerking: {0}", GbaRestInboxVerwerkingType.get(inboxRecord.getVerwerkingId()));
    log(INFO, "Zaaknummers: {0} (intern), {1} (extern)", inboxInterneZaakId, inboxExternZaakId);

    setZaakNummer(vraagZaak, dossier);

    dossier.setBron(vraagZaak.getBron());
    dossier.setLeverancier(vraagZaak.getLeverancier());

    if (vraagZaak.getDatumingang() != null) {
      dossier.setDatumIngang(new DateTime(vraagZaak.getDatumingang()));
    }

    setSoortVerbintenis(verbintenissoort, huwelijk);
    huwelijk.setToelichtingVerbintenis("Pakket: " + vraagReservering.getPakket());
    DateTime tijdstip = getDatumTijdVerbintenis(vraagReservering);
    huwelijk.setDatumVerbintenis(tijdstip);
    huwelijk.setTijdVerbintenis(tijdstip);

    setPartner(vraagPartners.getPartner1(), huwelijk.getPartner1());
    setPartner(vraagPartners.getPartner2(), huwelijk.getPartner2());
    setLocatie(vraagReservering.getLocatie(), huwelijk);
    setOpties(vraagOpties, service, huwelijk);
    setNaamgebruik(vraagPartners, huwelijk);
    setAmbtenaren(vraagReservering, huwelijk);

    log(INFO, "Huwelijkszaak opgeslagen", vraagZaak.getZaaknummer());

    resetPagina(dossier);
    addZaakRelatie(relatieZaakId, inboxRecord, ZaakType.HUWELIJK_GPS_GEMEENTE);

    // Commentaar toevoegen met mogelijke waarschuwingen
    ZaakCommentaren commentaar = dossier.getCommentaren();
    commentaar.verwijderen().toevoegenWarn(HuwelijkProcessen.getProcesStatussen(dossier).getMessages());

    // Opslaan
    DossierService dossierService = getServices().getDossierService();
    dossierService.saveDossier(dossier);
    dossierService.saveZaakDossier(huwelijk);
    service.save(dossier);

    getServices().getZaakStatusService().updateStatus(inboxRecord, ZaakStatusType.VERWERKT,
        "Verwerkt tot een huwelijkszaak");

    return getResult();
  }

  public boolean isNaamgebruikGewijzigd(DossierPersoon persoon, String nieuwNaamgebruikCode) {
    return persoon.getNaamgebruik() == null || !persoon.getNaamgebruik().equalsIgnoreCase(nieuwNaamgebruikCode);
  }

  private DateTime getDatumTijdVerbintenis(Reservering vraagReservering) {
    return new DateTime(vraagReservering.getDatumverbintenis(), vraagReservering.getTijdverbintenis(),
        TimeType.TIME_4_DIGITS);
  }

  /**
   * Voeg de huwelijkslocatie toe
   */
  private HuwelijksLocatie getLocatieByName(String locatie) {
    for (HuwelijksLocatie huwLocatie : getServices().getHuwelijkService().getHuwelijksLocaties(
        GeldigheidStatus.ALLES)) {
      if (isAlias(huwLocatie.getHuwelijksLocatie(), huwLocatie.getAliassen(), locatie)) {
        log(INFO, "Huwelijkslocatie {0} toegevoegd", locatie);
        return huwLocatie;
      }
    }
    throw new ProException(ERROR, "Geen huwelijkslocatie {0} gevonden", locatie);
  }

  private DossierHuwelijkOptie getOptie(Optie optie, List<DossierHuwelijkOptie> dossierHuwelijksOpties) {
    for (DossierHuwelijkOptie dossierOptie : dossierHuwelijksOpties) {
      HuwelijksLocatieOptie huwOpt = dossierOptie.getOptie();
      if (isAlias(huwOpt.getHuwelijksLocatieOptie(), huwOpt.getAliassen(), optie.getId())) {
        return dossierOptie;
      }
    }

    throw new ProException(ERROR, "Geen optie genaamd {0} voor deze huwelijkslocatie", optie.getId());
  }

  private boolean isAlias(String name, List<String> aliasses, String pattern) {

    List<String> list = new ArrayList<>();
    list.add(name);
    list.addAll(aliasses);

    for (String alias : list) {
      if (pattern.trim().equalsIgnoreCase(alias.trim())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Voeg de ambtenaar toe
   */
  private void setAmbtenaar(int voorkeur, DossierPersoon persoon, String naam) {
    for (HuwelijksAmbtenaar ambtenaar : getServices().getHuwelijkService().getHuwelijksAmbtenaren(
        GeldigheidStatus.ALLES)) {
      if (isAlias(ambtenaar.getHuwelijksAmbtenaar(), ambtenaar.getAliassen(), naam)) {
        BsnFieldValue bsn = ambtenaar.getBurgerServiceNummer();
        if (bsn.isCorrect()) {
          log(INFO, "{0}e ambtenaar {1} ({2}) toegevoegd", voorkeur, naam,
              ambtenaar.getHuwelijksAmbtenaar());
          BasePLExt persoonslijst = getServices().getPersonenWsService().getPersoonslijst(
              bsn.getStringValue());
          BsPersoonUtils.kopieDossierPersoon(persoonslijst, persoon);
          persoon.setAktenaam(ambtenaar.getHuwelijksAmbtenaar());
          return;
        }
      }
    }

    throw new ProException(ERROR, "Geen ambtenaar genaamd {0} gevonden", naam);
  }

  /**
   * Ambtenaren toevoegen
   */
  private void setAmbtenaren(Reservering vraagReservering, DossierHuwelijk huwelijk) {
    Ambtenaren ambtenaren = vraagReservering.getAmbtenaren();
    if (ambtenaren != null) {
      for (Ambtenaar ambtenaar : ambtenaren.getAmbtenaren()) {
        switch (ambtenaar.getVoorkeur()) {
          case 1:
            setAmbtenaar(1, huwelijk.getAmbtenaar1(), ambtenaar.getNaam());
            break;

          case 2:
            setAmbtenaar(2, huwelijk.getAmbtenaar2(), ambtenaar.getNaam());
            break;

          default:
            log(WARNING, "Geen {0}e ambtenaar mogelijk (maximaal 2)", ambtenaar.getVoorkeur());
        }
      }
    }
  }

  /**
   * Werk de contactgevens bij van de partner
   */
  private void setContactGegevens(Partner partner) {
    Bsn bsn = new Bsn(partner.getBsn().toString());
    if (bsn.isCorrect()) {
      String email = partner.getContactgegevens().getEmail();
      String telMobiel = partner.getContactgegevens().getTelefoon();
      if (fil(email) || fil(telMobiel)) {
        ContactgegevensService service = getServices().getContactgegevensService();
        service.setContactWaarde(-1, bsn.getLongBsn(), ContactgegevensService.EMAIL, email, -1);
        service.setContactWaarde(-1, bsn.getLongBsn(), ContactgegevensService.TEL_MOBIEL, telMobiel, -1);
        service.setContactWaarde(-1, bsn.getLongBsn(), ContactgegevensService.TEL_THUIS, "", -1);
        service.setContactWaarde(-1, bsn.getLongBsn(), ContactgegevensService.TEL_WERK, "", -1);
      }
    }
  }

  /**
   * Zet de locatie
   */
  private void setLocatie(String locatie, DossierHuwelijk huwelijk) {
    HuwelijksLocatie huwelijksLocatie = getLocatieByName(locatie);
    if (huwelijksLocatie != null) {
      huwelijk.setHuwelijksLocatie(huwelijksLocatie);
    }
  }

  /**
   * Zet naamgebruik
   */
  private void setNaamgebruik(Partners vraagPartners, DossierHuwelijk huwelijk) {

    Naamgebruik ngPartner1 = vraagPartners.getPartner1().getNaamgebruik();
    Naamgebruik ngPartner2 = vraagPartners.getPartner2().getNaamgebruik();

    if (ngPartner1 != null && huwelijk.getPartner1().isVolledig()) {
      String nieuwNgPartner1 = ngPartner1.getNieuw().getCode().name();
      if (isNaamgebruikGewijzigd(huwelijk.getPartner1(), nieuwNgPartner1)) {
        huwelijk.setNaamGebruikPartner1(nieuwNgPartner1);
        log(INFO, "Naamgebruik partner 1 wordt gewijzigd naar {0}", nieuwNgPartner1);
      } else {
        log(INFO, "Naamgebruik partner 1 blijft " + nieuwNgPartner1);
      }
    }

    if (ngPartner2 != null && huwelijk.getPartner2().isVolledig()) {
      String nieuwNgPartner2 = ngPartner2.getNieuw().getCode().name();
      if (isNaamgebruikGewijzigd(huwelijk.getPartner2(), nieuwNgPartner2)) {
        huwelijk.setNaamGebruikPartner2(nieuwNgPartner2);
        log(INFO, "Naamgebruik partner 2 wordt gewijzigd naar {0}", nieuwNgPartner2);
      } else {
        log(INFO, "Naamgebruik partner 2 blijft " + nieuwNgPartner2);
      }
    }
  }

  private void setOpties(Opties vraagOpties, HuwelijkService service, DossierHuwelijk huwelijk) {
    if (huwelijk.getHuwelijksLocatie() != null) {

      List<DossierHuwelijkOptie> beschikbareOpties = service.getDossierHuwelijksOpties(huwelijk);
      List<DossierHuwelijkOptie> gewijzigdeOpties = new ArrayList<>();

      for (Optie optie : vraagOpties.getOpties()) {
        DossierHuwelijkOptie dossierOptie = getOptie(optie, beschikbareOpties);
        if (dossierOptie != null) {
          String huwelijksLocatieOptie = dossierOptie.getOptie().getHuwelijksLocatieOptie();
          log(INFO, "Optie {0} heeft nu waarde {1}", huwelijksLocatieOptie, optie.getWaarde());
          dossierOptie.setWaarde(optie.getWaarde());
          gewijzigdeOpties.add(dossierOptie);
        }
      }
      huwelijk.setOpties(gewijzigdeOpties);
    }
  }

  /**
   * Zoek de partner op basis van het BSN
   */
  private void setPartner(Partner partner, DossierPersoon dossierPersoon) {
    Bsn bsn = new Bsn(partner.getBsn().toString());
    if (!bsn.isCorrect()) {
      throw new ProException(ERROR, "Bsn {0} is incorrect", bsn.getDefaultBsn());
    }
    BasePLExt persoonslijst = getServices().getPersonenWsService().getPersoonslijst(bsn.getDefaultBsn());
    if (persoonslijst.getCats().isEmpty()) {
      throw new ProException(ERROR, "Geen partner met bsn {0} gevonden", bsn.getFormatBsn());
    } else {
      String naam = persoonslijst.getPersoon().getNaam().getPredAdelVoorvGeslVoorn();
      log(INFO, "Partner met bsn {0} ({1}) gevonden in de BRP", bsn.getFormatBsn(), naam);
      BsPersoonUtils.kopieDossierPersoon(persoonslijst, dossierPersoon);
      setContactGegevens(partner);
    }
  }

  /**
   * Zet de soort verbintenis
   */
  private void setSoortVerbintenis(Verbintenissoort verbintenissoort, DossierHuwelijk huwelijk) {
    switch (verbintenissoort) {
      case GPS:
        huwelijk.setSoortVerbintenis(SoortVerbintenis.GPS);
        break;

      case HUWELIJK:
        huwelijk.setSoortVerbintenis(SoortVerbintenis.HUWELIJK);
        break;

      default:
        throw new ProException("Onbekende verbintenissoort: " + verbintenissoort);
    }
  }

  /**
   * Voeg het extern zaaknummer toe als alternatief zaaknummer
   */
  private void setZaakNummer(Zaak vraagZaak, Dossier dossier) {
    ZaakIdentificatie id = new ZaakIdentificatie();
    id.setInternId(dossier.getZaakId());
    id.setExternId(vraagZaak.getZaaknummer());
    id.setType(ZaakIdType.ZAAKSYSTEEM.getCode());
    id.setZaakType(dossier.getType());
    dossier.getZaakHistorie().getIdentificaties().getNummers().add(id);
  }
}
