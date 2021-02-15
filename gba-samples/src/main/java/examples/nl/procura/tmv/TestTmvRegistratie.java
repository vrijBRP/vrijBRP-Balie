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

package examples.nl.procura.tmv;

import nl.procura.tmv.actions.TmvRegistratieAction;
import nl.procura.tmv.soap.TmvSoapHandler;

public class TestTmvRegistratie {

  public TestTmvRegistratie() {

    TmvRegistratieAction action = new TmvRegistratieAction();

    action.nieuw("1", "217", 0, 7956913492L);

    action.setToelichting("BlaDiebla");
    action.addDetail(3, 210, "Ria", "Maria");
    String endpoint = "http://pc16:8080/mockTerugmeldvoorzieningSoapBinding?WSDL";
    action.setSoapHandler(new TmvSoapHandler(endpoint, "<username>", "<password>"));

    try {
      action.send();

      System.out.println(action.isVerwerkingSuccess());
      System.out.println(action.getResponseObject().getDossiernummer());
      System.out.println(action.getOutputMessage());

    } catch (Exception e) {
      System.out.println("FOUT: " + e.getMessage());

    } finally {
      System.out.println("Request : " + action.getSoapHandler().getRequest().getXmlMessage());
      System.out.println("Response: " + action.getSoapHandler().getResponse().getXmlMessage());
    }
  }

  public static void main(String[] args) {
    new TestTmvRegistratie();
  }
}
