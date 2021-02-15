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

import nl.procura.tmv.actions.TmvInzageVerzoekAction;
import nl.procura.tmv.soap.TmvSoapHandler;

public class TestInzage {

  public TestInzage() {

    TmvInzageVerzoekAction action = new TmvInzageVerzoekAction();

    action.nieuw("1", TmvInzageVerzoekAction.SOORT_OVERZICHT_DOSSIER);

    action.setDossiernummer(782691L);

    String endpoint = "http://127.0.0.1:8080/mockTerugmeldvoorzieningSoapBinding";

    // String endpoint = "https://82.161.198.126/tmvws/services/Terugmeldvoorziening";

    //      String endpoint = "http://sv01:8090/sslweb/proxy?url=https://87.213.53.117/gba-v/online/lo3services/adhoc";

    action.setSoapHandler(new TmvSoapHandler(endpoint, "990008", "gbb1Xxk2"));

    try {
      action.send();

      System.out.println(action.isVerwerkingSuccess());

      System.out.println(action.getResponseObject().getVerwerkingomschrijving());

      System.out.println(action.getOutputMessage());

      // Antwoord antwoord = action.getResponseObject ();

    } catch (Exception e) {
      System.out.println("FOUT: " + e.getMessage());

    } finally {
      System.out.println("Request : " + action.getSoapHandler().getRequest().getXmlMessage());

      System.out.println("Response: " + action.getSoapHandler().getResponse().getXmlMessage());
    }
  }

  public static void main(String[] args) {

    new TestInzage();
  }
}
