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

package examples.nl.procura.gba.zoeken;

import java.util.Collections;

import nl.procura.gba.web.rest.client.GbaRestClientException;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementViewer;
import nl.procura.gba.web.rest.v1_0.persoon.GbaRestPersoonVraag;
import nl.procura.gbaws.testdata.Testdata;

import examples.nl.procura.gba.GbaRestClientVoorbeelden;
import examples.nl.procura.gba.GbaRestGuice;
import examples.nl.procura.gba.GbaRestGuice.Timer;

public class GbaRestClientPersoonZoekenVoorbeelden extends GbaRestClientVoorbeelden {

  public GbaRestClientPersoonZoekenVoorbeelden() throws GbaRestClientException {
    persoonslijstenZoeken1();
    persoonslijstenZoeken2();
    persoonZoeken();
    persoonNummersZoeken();
  }

  public static void main(String[] args) {
    GbaRestGuice.getInstance(GbaRestClientPersoonZoekenVoorbeelden.class);
  }

  @Timer
  protected void persoonslijstenZoeken1() throws GbaRestClientException {
    GbaRestPersoonVraag vraag = new GbaRestPersoonVraag();
    vraag.setNummers(Collections.singletonList(Testdata.TEST_BSN_1));
    getObject(client.getPersoon().getPersoonslijsten(vraag));
  }

  @Timer
  protected void persoonslijstenZoeken2() throws GbaRestClientException {
    GbaRestElementViewer.info(getObject(client.getPersoon().getPersoonslijst(Testdata.TEST_BSN_1.toString())));
  }

  @Timer
  protected void persoonZoeken() throws GbaRestClientException {
    GbaRestElementViewer.info(getObject(client.getPersoon().get(Testdata.TEST_BSN_1.toString())));
  }

  @Timer
  protected void persoonNummersZoeken() throws GbaRestClientException {
    GbaRestElementViewer.info(getObject(client.getPersoon().getNummers(Testdata.TEST_BSN_1.toString())));
  }
}
