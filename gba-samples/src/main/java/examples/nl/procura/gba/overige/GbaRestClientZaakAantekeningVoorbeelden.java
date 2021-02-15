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

package examples.nl.procura.gba.overige;

import nl.procura.gba.web.rest.client.GbaRestClientException;
import nl.procura.gba.web.rest.v1_0.zaak.aantekening.GbaRestZaakAantekeningToevoegenVraag;

import examples.nl.procura.gba.GbaRestClientVoorbeelden;
import examples.nl.procura.gba.GbaRestGuice;
import examples.nl.procura.gba.GbaRestGuice.Timer;

public class GbaRestClientZaakAantekeningVoorbeelden extends GbaRestClientVoorbeelden {

  public GbaRestClientZaakAantekeningVoorbeelden() throws GbaRestClientException {
    aantekeningToevoegen();
  }

  public static void main(String[] args) {
    GbaRestGuice.getInstance(GbaRestClientZaakAantekeningVoorbeelden.class);
  }

  @Timer
  protected void aantekeningToevoegen() throws GbaRestClientException {

    GbaRestZaakAantekeningToevoegenVraag vraag = new GbaRestZaakAantekeningToevoegenVraag();
    vraag.setZaakId("123456");
    vraag.setOnderwerp("onderwerp");
    vraag.setInhoud("inhoud");

    getObject(client.getZaak().getAantekening().toevoegen(vraag));
  }
}
