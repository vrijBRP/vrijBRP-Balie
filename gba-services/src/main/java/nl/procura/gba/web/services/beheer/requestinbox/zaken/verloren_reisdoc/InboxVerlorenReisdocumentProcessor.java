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

package nl.procura.gba.web.services.beheer.requestinbox.zaken.verloren_reisdoc;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import java.time.LocalDate;

import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxBody;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxBodyProcessor;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxItem;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessingResult;

import lombok.Data;

public class InboxVerlorenReisdocumentProcessor extends RequestInboxBodyProcessor {

  public InboxVerlorenReisdocumentProcessor(RequestInboxItem record, Services services) {
    super(record, services);
  }

  @Override
  public CaseProcessingResult process() {
    return new CaseProcessingResult();
  }

  @Override
  public RequestInboxBody getBody() {
    RequestInboxBody inboxBody = new RequestInboxBody();
    try {
      LostTravelDocumentInboxData data = RequestInboxBody.fromJson(getRecord(), LostTravelDocumentInboxData.class);
      inboxBody.addAnr("A-nummer", data.getAnr())
          .addBsn("BSN", data.getBsn())
          .add("Datum laatst gezien", data.getDateLostNoticed())
          .add("Datum laatst gebruikt", data.getDateLastUsed())
          .add("Plek", data.getLocation())
          .add("Reden", data.getReason())
          .add("Extra details", data.getAdditionalDetails())
          .add("Documentnummer", data.getDocNumber())
          .add("Soort reisdocument", data.getTravelDocumentType());
    } catch (RuntimeException e) {
      log(ERROR, "Fout bij inlezen verzoek", e.getMessage());
      inboxBody.add("Fout bij inlezen verzoek", e.getMessage());
    }
    return inboxBody;
  }

  @Data
  private static class LostTravelDocumentInboxData {

    Long               anr;
    Long               bsn;
    LocalDate          dateLostNoticed;
    LocalDate          dateLastUsed;
    String             location;
    String             reason;
    String             additionalDetails;
    String             docNumber;
    TravelDocumentType travelDocumentType;
  }
}
