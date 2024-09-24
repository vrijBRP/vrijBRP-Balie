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

package nl.procura.gba.web.services.beheer.requestinbox.zaken.afschriftbs;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import nl.procura.burgerzaken.requestinbox.api.model.InboxEnum;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxBody;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxBodyProcessor;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxItem;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessingResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

public class InboxAfschriftbsProcessor extends RequestInboxBodyProcessor {

  public InboxAfschriftbsProcessor(RequestInboxItem record, Services services) {
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
      AfschriftBsInboxData data = RequestInboxBody.fromJson(getRecord(), AfschriftBsInboxData.class);
      inboxBody.addBsn("BSN", data.getPersonBsn())
          .add("Aanvraag", data.getRequestType())
          .add("Soort", data.getCivilRecordType())
          .add("Reden", data.getCivilRequestReason());
    } catch (RuntimeException e) {
      log(ERROR, "Fout bij inlezen verzoek", e.getMessage());
      inboxBody.add("Fout bij inlezen verzoek", e.getMessage());
    }
    return inboxBody;
  }

  @Data
  private static class AfschriftBsInboxData {

    private Long            personBsn;
    private CivilRecordType civilRecordType;
    private RequestType     requestType;
    private String          civilRequestReason;
  }

  @Getter
  @AllArgsConstructor
  private enum CivilRecordType implements InboxEnum<Integer> {

    BIRTH_CERTIFICATE(1, "Geboorteakte"),
    MARRIAGE_CERTIFICATE(2, "Huwelijksakte"),
    DIVORCE_CERTIFICATE(3, "Echtscheidingsakte"),
    CERTIFICATE_OF_PARTNERSHIP(4, "Akte van partnerschap"),
    CERTIFICATE_OF_ENDING_OF_PARTNERSHIP(5, "Akte van beëindiging partnerschap");

    private final Integer id;
    private final String  descr;

    @Override
    public String toString() {
      return descr;
    }
  }

  @Getter
  @AllArgsConstructor
  private enum RequestType implements InboxEnum<Integer> {

    REQUEST_FOR_ANOTHER_PERSON(0, "Aanvraag voor een andere persoon"),
    REQUEST_FOR_REQUESTOR_SELF(1, "Aanvraag voor de aanvrager zelf");

    private final Integer id;
    private final String  descr;

    @Override
    public String toString() {
      return descr;
    }
  }
}
