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

package examples.nl.procura.vog;

import nl.procura.covog.soap.VogSoapHandler;

public class TestCOVOGOntvangen {

  public TestCOVOGOntvangen() {
    String endpoint = "https://covogacc.minjus.gemnet.nl/webservices/AanvraagNpAPI";
    VogSoapHandler soapHandler = new VogSoapHandler(endpoint, "<relatiecode>", "<password>");

    try {
      System.out.println("RETURN: " + soapHandler.send("039300201006085041"));
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      System.out.println("Request : " + soapHandler.getRequest().getXmlMessage());
      System.out.println("Response: " + soapHandler.getResponse().getXmlMessage());
    }
  }

  public static void main(String[] args) {
    new TestCOVOGOntvangen();
  }
}
