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

package examples.nl.procura.gbaws.tabellen;

import nl.procura.gba.web.rest.client.GbaRestClientException;
import nl.procura.gbaws.web.rest.v1_0.tabellen.GbaWsRestTabellenVraag;

import examples.nl.procura.gbaws.GbaWsRestClientVoorbeelden;
import examples.nl.procura.gbaws.GbaWsRestGuice;
import examples.nl.procura.gbaws.GbaWsRestGuice.Timer;

public class GbaWsRestClientTabelVoorbeelden extends GbaWsRestClientVoorbeelden {

  public GbaWsRestClientTabelVoorbeelden() throws GbaRestClientException {
    tabellen();
  }

  public static void main(String[] args) {
    GbaWsRestGuice.getInstance(GbaWsRestClientTabelVoorbeelden.class);
  }

  @Timer
  protected void tabellen() throws GbaRestClientException {
    GbaWsRestTabellenVraag vraag = new GbaWsRestTabellenVraag();
    vraag.setHistorie(false);
    vraag.getCodes().add(32);
    System.out.println("Records: " + getObject(client.getTabel().getTabellen(vraag))
        .getTabellen().get(0).getRecords().size());
  }
}
