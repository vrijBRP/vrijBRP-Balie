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

package nl.procura.burgerzaken.requestinbox.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import nl.procura.burgerzaken.requestinbox.api.RequestConfig;

public class DocumentsApiTest extends AbstractTestWireMockClient {

  @Test
  public void mustReturnDocumentContent() {
    RequestConfig config = new RequestConfig();
    config.accessToken("myToken");

    String id = "018cd3c7-31f4-78e4-9e53-ebf9f039e649";
    byte[] response = getDocumentsApi().getDocumentContent(id, config);
    Assertions.assertEquals(28032, response.length);
  }
}
