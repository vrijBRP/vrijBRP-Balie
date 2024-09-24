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

package nl.procura.burgerzaken.zynyo.client;

import nl.procura.burgerzaken.zynyo.api.SignDocumentRequestBody;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import nl.procura.burgerzaken.zynyo.api.model.ZynyoSignedDocument;
import nl.procura.burgerzaken.zynyo.api.model.ZynyoSignDocumentRequest;

public class SigningApiTest extends AbstractTestWireMockClient {

  @Test
  public void mustReturnSignDocumentRequest() {
    SignDocumentRequestBody body = new SignDocumentRequestBody();
    ZynyoSignDocumentRequest signDocumentRequest = getSigningApi().postSignDocumentRequest(body);

    Assertions.assertEquals("74e65b2c-fc2a-4272-b438-f766cfe936c1", signDocumentRequest.documentUUID());
    Assertions.assertEquals(1, signDocumentRequest.signatoryLink().get(0).getSequence());
    Assertions.assertEquals("SIGN", signDocumentRequest.signatoryLink().get(0).getRole());
    Assertions.assertEquals("ca5f4f2f-21a5-437b-aac0-a2eda63aa3c0", signDocumentRequest.signatoryLink().get(0).getSignatoryUUID());
    Assertions.assertEquals("https://sandbox.zynyo.com/webapp/sign/ca5f4f2f-21a5-437b-aac0-a2eda63aa3c0", signDocumentRequest.signatoryLink().get(0).getDocumentLink());
  }

  @Test
  public void mustReturnDocumentContent() {
    String id = "74e65b2c-fc2a-4272-b438-f766cfe936c1";
    ZynyoSignedDocument document = getSigningApi().getSignedDocument(id);
    Assertions.assertNotNull(document.documentContent());
  }
}
