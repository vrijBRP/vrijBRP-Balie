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

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;

import nl.procura.brp.inbox.soap.endpoints.v1.huwelijksplanner.voornemen.vraag.Vragen;
import nl.procura.brp.inbox.soap.endpoints.v1.huwelijksplanner.voornemen.vraag.Zaak;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.bs.huwelijk.processen.HuwelijkProcessen;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.inbox.GbaRestInboxVerwerkingType;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.bs.huwelijk.HuwelijkService;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessingResult;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessor;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.ZaakCommentaren;
import nl.procura.gba.web.services.zaken.gemeenteinbox.GemeenteInboxRecord;

public class GemeenteInboxVoornemenProcessor extends CaseProcessor<GemeenteInboxRecord> {

  public GemeenteInboxVoornemenProcessor(Services services) {
    super(services);
  }

  @Override
  public CaseProcessingResult process(GemeenteInboxRecord inboxRecord) {
    Vragen vragen = fromStream(inboxRecord, Vragen.class);
    Zaak vraagZaak = vragen.getVraag().getZaak();
    Integer datumVoornemen = vragen.getVraag().getDatumvoornemen();

    HuwelijkService service = getServices().getHuwelijkService();
    Dossier dossier = (Dossier) getZaak(vraagZaak.getZaaknummer(), ZaakType.HUWELIJK_GPS_GEMEENTE);
    DossierHuwelijk huwelijk = (DossierHuwelijk) dossier.getZaakDossier();

    DateTime dateTime = new DateTime(datumVoornemen);
    huwelijk.setDatumVoornemen(dateTime);

    String relatieZaakId = dossier.getZaakId();
    String inboxInterneZaakId = inboxRecord.getZaakId();
    String inboxExternZaakId = inboxRecord.getZaakIdExtern();

    log(INFO, "Verwerking: {0}", GbaRestInboxVerwerkingType.get(inboxRecord.getVerwerkingId()));
    log(INFO, "Zaaknummers: {0} (intern), {1} (extern)", inboxInterneZaakId, inboxExternZaakId);
    log(INFO, "Datum voornemen: {0}", dateTime.getFormatDate());

    resetPagina(dossier);
    addZaakRelatie(relatieZaakId, inboxRecord, ZaakType.HUWELIJK_GPS_GEMEENTE);

    // Commentaar toevoegen met mogelijke waarschuwingen
    ZaakCommentaren commentaar = dossier.getCommentaren();
    commentaar.verwijderen().toevoegenWarn(HuwelijkProcessen.getProcesStatussen(dossier).getMessages());

    // Opslaan
    service.save(dossier);

    String statusMessage = "Datum voornemen (" + dateTime.getFormatDate() + ") toegevoegd aan de huwelijkszaak";
    getServices().getZaakStatusService().updateStatus(inboxRecord, ZaakStatusType.VERWERKT, statusMessage);

    return getResult();
  }
}
