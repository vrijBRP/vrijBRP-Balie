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

package nl.procura.gba.web.rest.v1_0.zaak.verwerken.inbox.geboorte;

import static nl.procura.gba.web.components.containers.Container.PLAATS;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.*;

import java.util.List;

import nl.procura.brp.inbox.soap.endpoints.v1.geboorteaangifte.aangifte.vraag.*;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.modules.bs.geboorte.processen.GeboorteProcessen;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.inbox.GbaRestInboxVerwerkingType;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.DossierService;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.NaamsPersoonType;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.GeboorteService;
import nl.procura.gba.web.services.bs.geboorte.GezinssituatieType;
import nl.procura.gba.web.services.bs.geboorte.RedenVerplicht;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.gba.web.services.inbox.InboxRecord;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessingResult;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessor;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.ZaakCommentaren;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdType;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificatie;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.standard.ProcuraDate;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.validation.Bsn;

public class InboxGeboorteProcessor extends CaseProcessor {

  public InboxGeboorteProcessor(Services services) {
    super(services);
  }

  public CaseProcessingResult addAangifte(InboxRecord inboxRecord) {

    Vragen vragen = fromStream(inboxRecord, Vragen.class);
    Zaak vraagZaak = vragen.getVraag().getZaak();
    Geboorte vraagGeboorte = vragen.getVraag().getGeboorte();
    Aangever vraagAangever = vraagGeboorte.getAangever();
    Moeder vraagMoeder = vraagGeboorte.getMoeder();
    List<Kind> vraagKinderen = vraagGeboorte.getKinderen();

    GeboorteService service = getServices().getGeboorteService();
    Dossier dossier = (Dossier) service.getNewZaak();
    DossierGeboorte geboorte = (DossierGeboorte) dossier.getZaakDossier();

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

    setAangever(vraagAangever, geboorte.getAangever());
    setMoeder(vraagMoeder, geboorte.getMoeder());
    setVader(vraagAangever, geboorte.getVader());
    setKinderen(vraagKinderen, geboorte);
    setAangifte(vraagGeboorte, geboorte);

    log(INFO, "Geboorteaangifte opgeslagen", vraagZaak.getZaaknummer());

    resetPagina(dossier);
    addZaakRelatie(relatieZaakId, inboxRecord, ZaakType.GEBOORTE);

    // Commentaar toevoegen met mogelijke waarschuwingen
    ZaakCommentaren commentaar = dossier.getCommentaren();
    commentaar.verwijderen().toevoegenWarn(GeboorteProcessen.getProcesStatussen(dossier).getMessages());

    // Opslaan
    DossierService dossierService = getServices().getDossierService();
    dossierService.saveDossier(dossier);
    dossierService.saveZaakDossier(geboorte);
    service.save(dossier);

    getServices().getZaakStatusService().updateStatus(inboxRecord, ZaakStatusType.VERWERKT,
        "Verwerkt tot een geboorteaangifte");

    return getResult();
  }

  private void setKinderen(List<Kind> vraagKinderen, DossierGeboorte geboorte) {

    FieldValue gemeente = PLAATS.get(getServices().getGebruiker().getGemeenteCode());

    if (!pos(gemeente.getValue())) {
      throw new ProException(WARNING, "Kan de huidige gemeente niet bepalen.");
    }

    for (Kind vraagKind : vraagKinderen) {
      if (pos(new ProcuraDate().diffInDays(new ProcuraDate(vraagKind.getGeboortedatum())))) {
        throw new ProException(WARNING, "De geboortedatum kan niet in de toekomst liggen.");
      }

      DossierPersoon kind = new DossierPersoon();
      kind.setVoornaam(vraagKind.getVoornamen());
      kind.setGeslacht(Geslacht.get(vraagKind.getGeslacht().name()));
      kind.setDatumGeboorte(new GbaDateFieldValue(new DateTime(vraagKind.getGeboortedatum())));
      kind.setTijdGeboorte(new DateTime(0, along(vraagKind.getGeboortetijd()), DateTime.TimeType.TIME_4_DIGITS));
      kind.setGeboorteplaats(gemeente);
      kind.setGeboorteland(Landelijk.getNederland());

      getServices().getDossierService().addKind(geboorte, kind);
    }
  }

  private void setAangifte(Geboorte vraagGeboorte, DossierGeboorte geboorte) {
    Gezinssituatie gezinssituatie = vraagGeboorte.getAangifte().getGezinssituatie();
    switch (gezinssituatie) {
      case BINNEN_HETERO_HUWELIJK:
        geboorte.setGezinssituatie(GezinssituatieType.BINNEN_HETERO_HUWELIJK);
        break;
      default:
        throw new ProException("Onbekende waarde: gezinssituatie " + gezinssituatie);
    }

    RedenVerplichtOfBevoegd redenVerplicht = vraagGeboorte.getAangifte().getRedenVerplichtOfBevoegd();
    switch (redenVerplicht) {
      case VADER:
        geboorte.setRedenVerplichtBevoegd(RedenVerplicht.VADER);
        break;
      default:
        throw new ProException("Onbekende waarde: reden verplicht / bevoegd " + redenVerplicht);
    }

    NaamskeuzePersoon naamskeuzePersoon = vraagGeboorte.getNamenrecht().getNaamskeuzePersoon();
    switch (naamskeuzePersoon) {
      case MOEDER:
        geboorte.setNaamskeuzePersoon(NaamsPersoonType.MOEDER);
        setNaamskeuzePersoon(geboorte, geboorte.getMoeder());
        break;
      case VADER_OF_DUO_MOEDER:
        geboorte.setNaamskeuzePersoon(NaamsPersoonType.VADER_DUO_MOEDER);
        setNaamskeuzePersoon(geboorte, geboorte.getVader());
        break;
      default:
        throw new ProException("Onbekende waarde: naamskeuze persoon " + naamskeuzePersoon);
    }
  }

  private void setNaamskeuzePersoon(DossierGeboorte geboorte, DossierPersoon persoon) {
    geboorte.setKeuzeGeslachtsnaam(persoon.getGeslachtsnaam());
    geboorte.setKeuzeVoorvoegsel(persoon.getVoorvoegsel());
    geboorte.setKeuzeTitel(persoon.getTitel());
  }

  /**
   * Werk de contactgevens bij van de aangever
   */
  private void setContactGegevens(Aangever aangever) {
    Bsn bsn = new Bsn(aangever.getBsn().toString());
    if (bsn.isCorrect()) {
      String email = aangever.getContactgegevens().getEmail();
      String telMobiel = aangever.getContactgegevens().getTelefoon();
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
   * Zoek de partner op basis van het BSN
   */
  private void setAangever(Aangever aangever, DossierPersoon dossierPersoon) {
    setPersoon(dossierPersoon, aangever.getBsn());
    setContactGegevens(aangever);
  }

  /**
   * Zoek de moeder op basis van het BSN
   */
  private void setMoeder(Moeder moeder, DossierPersoon dossierPersoon) {
    setPersoon(dossierPersoon, moeder.getBsn());
  }

  /**
   * Zoek de aangever op basis van het BSN
   */
  private void setVader(Aangever aangever, DossierPersoon dossierPersoon) {
    setPersoon(dossierPersoon, aangever.getBsn());
  }

  private void setPersoon(DossierPersoon dossierPersoon, Integer bsn2) {
    Bsn bsn = new Bsn(bsn2.toString());
    if (!bsn.isCorrect()) {
      throw new ProException(ERROR, "Bsn {0} is incorrect", bsn.getDefaultBsn());
    }
    BasePLExt persoonslijst = getServices().getPersonenWsService().getPersoonslijst(bsn.getDefaultBsn());
    if (persoonslijst.getCats().isEmpty()) {
      throw new ProException(ERROR, "Geen persoon met bsn {0} gevonden", bsn.getFormatBsn());
    } else {
      String naam = persoonslijst.getPersoon().getNaam().getPredAdelVoorvGeslVoorn();
      log(INFO, "Persoon met bsn {0} ({1}) gevonden in de BRP", bsn.getFormatBsn(), naam);
      BsPersoonUtils.kopieDossierPersoon(persoonslijst, dossierPersoon);
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
