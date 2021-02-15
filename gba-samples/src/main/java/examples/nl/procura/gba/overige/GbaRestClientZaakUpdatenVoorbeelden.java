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
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementViewer;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakStatus;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakStatusUpdateVraag;

import examples.nl.procura.gba.GbaRestClientVoorbeelden;
import examples.nl.procura.gba.GbaRestGuice;
import examples.nl.procura.gba.GbaRestGuice.Timer;

public class GbaRestClientZaakUpdatenVoorbeelden extends GbaRestClientVoorbeelden {

  public GbaRestClientZaakUpdatenVoorbeelden() throws GbaRestClientException {
    zaakUpdaten();
  }

  public static void main(String[] args) {
    GbaRestGuice.getInstance(GbaRestClientZaakUpdatenVoorbeelden.class);
  }

  @Timer
  protected void zaakUpdaten() throws GbaRestClientException {
    GbaRestZaakStatusUpdateVraag vraag = new GbaRestZaakStatusUpdateVraag();
    vraag.setZaakId("test");
    vraag.setStatus(GbaRestZaakStatus.VERWERKT);
    vraag.setOpmerking("Dit is een opmerking!");

    GbaRestElementViewer.info(getObject(client.getZaak().setStatus(vraag).check()));
  }
}
