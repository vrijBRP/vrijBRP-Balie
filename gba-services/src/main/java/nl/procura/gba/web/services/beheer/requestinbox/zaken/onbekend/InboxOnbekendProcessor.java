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

package nl.procura.gba.web.services.beheer.requestinbox.zaken.onbekend;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import java.util.LinkedHashMap;
import java.util.Map;

import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxBody;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxBodyProcessor;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxItem;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessingResult;

public class InboxOnbekendProcessor extends RequestInboxBodyProcessor {

  public InboxOnbekendProcessor(RequestInboxItem record, Services services) {
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
      Map<String, Object> data = RequestInboxBody.fromJson(getRecord(), LinkedHashMap.class);
      data.forEach(inboxBody::add);
    } catch (RuntimeException e) {
      log(ERROR, "Fout bij inlezen verzoek", e.getMessage());
      inboxBody.add("Fout bij inlezen verzoek", e.getMessage());
    }
    return inboxBody;
  }
}
