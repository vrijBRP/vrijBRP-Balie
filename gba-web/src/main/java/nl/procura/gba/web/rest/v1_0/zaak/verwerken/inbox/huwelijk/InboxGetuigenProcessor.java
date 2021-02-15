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

package nl.procura.gba.web.rest.v1_0.zaak.verwerken.inbox.huwelijk;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.GETUIGE;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;

import java.util.List;

import nl.procura.brp.inbox.soap.endpoints.v1.huwelijksplanner.getuigen.vraag.*;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.bs.huwelijk.processen.HuwelijkProcessen;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.inbox.GbaRestInboxVerwerkingType;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.bs.huwelijk.HuwelijkService;
import nl.procura.gba.web.services.inbox.InboxRecord;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessingResult;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessor;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.ZaakCommentaren;
import nl.procura.standard.exceptions.ProException;

public class InboxGetuigenProcessor extends CaseProcessor {

  public InboxGetuigenProcessor(Services services) {
    super(services);
  }

  public CaseProcessingResult addGetuigen(InboxRecord inboxRecord) {

    Vragen vragen = fromStream(inboxRecord, Vragen.class);
    Zaak vraagZaak = vragen.getVraag().getZaak();
    Gemeentegetuigen gemeentegetuigen = vragen.getVraag().getGemeentegetuigen();
    Getuigen vraagGetuigen = vragen.getVraag().getGetuigen();

    HuwelijkService service = getServices().getHuwelijkService();
    Dossier dossier = (Dossier) getZaak(vraagZaak.getZaaknummer(), ZaakType.HUWELIJK_GPS_GEMEENTE);
    DossierHuwelijk huwelijk = (DossierHuwelijk) dossier.getZaakDossier();

    String relatieZaakId = dossier.getZaakId();
    String inboxInterneZaakId = inboxRecord.getZaakId();
    String inboxExternZaakId = inboxRecord.getZaakIdExtern();

    log(INFO, "Verwerking: {0}", GbaRestInboxVerwerkingType.get(inboxRecord.getVerwerkingId()));
    log(INFO, "Zaaknummers: {0} (intern), {1} (extern)", inboxInterneZaakId, inboxExternZaakId);

    if (gemeentegetuigen != null) {
      Integer aantalGemeentenGetuigen = gemeentegetuigen.getAantal();
      huwelijk.setGemeenteGetuigen(aantalGemeentenGetuigen);
    }

    // Verwijder de huidige getuigen
    for (DossierPersoon dossierGetuige : dossier.getPersonen(DossierPersoonType.GETUIGE)) {
      service.deleteGetuige(huwelijk, dossierGetuige);
    }

    // Voeg eventueel nieuwe getuigen toe
    if (vraagGetuigen != null) {
      List<Getuige> getuigen = vraagGetuigen.getGetuigen();
      for (Getuige getuige : getuigen) {
        DossierPersoon persoon = new DossierPersoon(GETUIGE);
        persoon.setCode(null);
        setGetuige(getuige, persoon);
        persoon.setToelichting(getuige.getRelatiebruidspaar());
        dossier.toevoegenPersoon(persoon);
      }
    }

    resetPagina(dossier);
    addZaakRelatie(relatieZaakId, inboxRecord, ZaakType.HUWELIJK_GPS_GEMEENTE);

    // Commentaar toevoegen met mogelijke waarschuwingen
    ZaakCommentaren commentaar = dossier.getCommentaren();
    commentaar.verwijderen().toevoegenWarn(HuwelijkProcessen.getProcesStatussen(dossier).getMessages());

    // Opslaan
    service.save(dossier);

    String statusMessage = "Getuigen toegevoegd aan de huwelijkszaak";
    getServices().getZaakStatusService().updateStatus(inboxRecord, ZaakStatusType.VERWERKT, statusMessage);

    return getResult();
  }

  private void setGetuige(Getuige getuige, DossierPersoon dossierPersoon) {
    String bsn = getuige.getBsn().toString();
    BasePLExt persoonslijst = getServices().getPersonenWsService().getPersoonslijst(bsn);
    if (persoonslijst.getCats().isEmpty()) {
      throw new ProException(ERROR, "Geen getuige met bsn {0} gevonden", bsn);
    } else {
      String naam = persoonslijst.getPersoon().getNaam().getPredAdelVoorvGeslVoorn();
      log(INFO, "Getuige met bsn {0} ({1}) gevonden in de BRP", bsn, naam);
      BsPersoonUtils.kopieDossierPersoon(persoonslijst, dossierPersoon);
      dossierPersoon.setVolgorde(Long.valueOf(getuige.getVoorkeur()));
    }
  }
}
