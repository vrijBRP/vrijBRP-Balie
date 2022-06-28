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

package nl.procura.gba.web.rest.v1_0.zaak.verwerken.inbox;

import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;

import java.util.List;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.inbox.GbaRestInboxVerwerkingType;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.inbox.InboxRecord;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessingResult;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessor;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSBytesContent;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSDocument;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSService;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;

public class InboxBijlageProcessor extends CaseProcessor {

  public InboxBijlageProcessor(Services services) {
    super(services);
  }

  public CaseProcessingResult addBijlage(InboxRecord inboxRecord) {

    List<ZaakKey> zaakKeys = getZaakKeys(inboxRecord.getZaakIdExtern());

    byte[] bytes = inboxRecord.getBestandsbytes();
    String filename = inboxRecord.getBestandsnaam();

    ZaakType zaakType = zaakKeys.get(0).getZaakType();
    String relatieZaakId = zaakKeys.get(0).getZaakId();
    String inboxInterneZaakId = inboxRecord.getZaakId();
    String inboxExternZaakId = inboxRecord.getZaakIdExtern();

    log(INFO, "Verwerking: {0}", GbaRestInboxVerwerkingType.get(inboxRecord.getVerwerkingId()));
    log(INFO, "Zaaknummers: {0} (intern), {1} (extern)", inboxInterneZaakId, inboxExternZaakId);

    DMSService dms = getServices().getDmsService();

    DMSDocument dmsDocument = DMSDocument.builder()
        .content(DMSBytesContent.fromFilename(filename, bytes))
        .user("Via e-loket")
        .datatype(DocumentType.ONBEKEND.getType())
        .zaakId(relatieZaakId)
        .build();

    dms.save(dmsDocument);
    addZaakRelatie(relatieZaakId, inboxRecord, zaakType);

    String statusMessage = "Bijlage toegevoegd aan de huwelijkszaak";
    getServices().getZaakStatusService().updateStatus(inboxRecord, ZaakStatusType.VERWERKT, statusMessage);

    return getResult();
  }
}
