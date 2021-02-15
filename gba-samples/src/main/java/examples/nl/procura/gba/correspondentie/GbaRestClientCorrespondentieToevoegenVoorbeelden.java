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

package examples.nl.procura.gba.correspondentie;

import static nl.procura.standard.Globalfunctions.getSystemDate;
import static nl.procura.standard.Globalfunctions.getSystemTime;

import nl.procura.gba.web.rest.client.GbaRestClientException;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementViewer;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakStatus;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.correspondentie.GbaRestZaakCorrespondentieToevoegenVraag;
import nl.procura.gbaws.testdata.Testdata;

import examples.nl.procura.gba.GbaRestClientVoorbeelden;
import examples.nl.procura.gba.GbaRestGuice;
import examples.nl.procura.gba.GbaRestGuice.Timer;

public class GbaRestClientCorrespondentieToevoegenVoorbeelden extends GbaRestClientVoorbeelden {

  public GbaRestClientCorrespondentieToevoegenVoorbeelden() {
    try {
      toevoegenReisdocument();
      toevoegenVerlopenToestemming();
      toevoegenAnders();
    } catch (GbaRestClientException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    GbaRestGuice.getInstance(GbaRestClientCorrespondentieToevoegenVoorbeelden.class);
  }

  @Timer
  protected void toevoegenVerlopenToestemming() throws GbaRestClientException {

    GbaRestZaakCorrespondentieToevoegenVraag vraag = new GbaRestZaakCorrespondentieToevoegenVraag();
    vraag.setZaakId("");
    vraag.setZaakStatus(GbaRestZaakStatus.ONBEKEND);
    vraag.setBurgerServiceNummer(Testdata.TEST_BSN_1);
    vraag.setAnummer(Testdata.TEST_ANR_1);
    vraag.setDatumIngang("20140401");
    vraag.setBron("BSM");
    vraag.setLeverancier("PROCURA");
    vraag.setDatumInvoer(getSystemDate());
    vraag.setTijdInvoer(getSystemTime());

    vraag.setRoute("B");
    vraag.setCorrespondentieType(3); // Verlopen toestemming
    vraag.setToelichting("Toelichting");

    GbaRestElementViewer.info(getObject(client.getZaak().toevoegen(vraag)));
  }

  @Timer
  protected void toevoegenReisdocument() throws GbaRestClientException {

    GbaRestZaakCorrespondentieToevoegenVraag vraag = new GbaRestZaakCorrespondentieToevoegenVraag();
    vraag.setZaakId("");
    vraag.setZaakStatus(GbaRestZaakStatus.GEANNULEERD);
    vraag.setBurgerServiceNummer(Testdata.TEST_BSN_1);
    vraag.setAnummer(5463824651L);
    vraag.setDatumIngang("20140401");
    vraag.setBron("BSM");
    vraag.setLeverancier("PROCURA");
    vraag.setDatumInvoer(getSystemDate());
    vraag.setTijdInvoer(getSystemTime());

    vraag.setRoute("B");
    vraag.setCorrespondentieType(2); // Verlopen reisdocument
    vraag.setToelichting("Toelichting");

    GbaRestElementViewer.info(getObject(client.getZaak().toevoegen(vraag)));
  }

  @Timer
  protected void toevoegenAnders() throws GbaRestClientException {

    GbaRestZaakCorrespondentieToevoegenVraag vraag = new GbaRestZaakCorrespondentieToevoegenVraag();
    vraag.setZaakId("");
    vraag.setZaakStatus(GbaRestZaakStatus.GEWEIGERD);
    vraag.setBurgerServiceNummer(Testdata.TEST_BSN_1);
    vraag.setAnummer(5463824651L);
    vraag.setDatumIngang("20140401");
    vraag.setBron("BSM");
    vraag.setLeverancier("PROCURA");
    vraag.setDatumInvoer(getSystemDate());
    vraag.setTijdInvoer(getSystemTime());

    vraag.setRoute("B");
    vraag.setCorrespondentieType(1); // Anders reisdocument
    vraag.setAnders("bladiebla");
    vraag.setToelichting("Toelichting");

    GbaRestElementViewer.info(getObject(client.getZaak().toevoegen(vraag)));
  }
}
