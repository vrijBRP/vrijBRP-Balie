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

package examples.nl.procura.gbaws.database.procura;

import nl.procura.gba.web.rest.client.GbaRestClientException;
import nl.procura.gbaws.web.rest.v1_0.database.procura.query.GbaWsRestProcuraSelecterenVraag;

import examples.nl.procura.gbaws.GbaWsRestClientVoorbeelden;

public class GbaWsRestClientProcuraDatabaseVoorbeelden extends GbaWsRestClientVoorbeelden {

  public GbaWsRestClientProcuraDatabaseVoorbeelden() throws GbaRestClientException {
    query();
  }

  public static void main(String[] args) throws GbaRestClientException {
    new GbaWsRestClientProcuraDatabaseVoorbeelden();
  }

  protected void query() throws GbaRestClientException {
    GbaWsRestProcuraSelecterenVraag vraag = new GbaWsRestProcuraSelecterenVraag();
    vraag.setQuery("select x from Straat x");
    getObject(client.getProcura().getDatabase().selecteren(vraag));
  }
}
