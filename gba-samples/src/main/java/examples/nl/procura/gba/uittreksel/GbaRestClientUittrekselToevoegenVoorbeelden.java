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

package examples.nl.procura.gba.uittreksel;

import static nl.procura.standard.Globalfunctions.getSystemDate;
import static nl.procura.standard.Globalfunctions.getSystemTime;

import nl.procura.gba.web.rest.client.GbaRestClientException;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementViewer;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakStatus;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakStatusUpdateVraag;
import nl.procura.gba.web.rest.v1_0.zaak.aantekening.GbaRestZaakAantekeningToevoegenVraag;
import nl.procura.gba.web.rest.v1_0.zaak.attribuut.GbaRestZaakAttribuutToevoegenVraag;
import nl.procura.gba.web.rest.v1_0.zaak.bestand.GbaRestZaakBestandToevoegenVraag2;
import nl.procura.gba.web.rest.v1_0.zaak.identificatie.GbaRestZaakIdType;
import nl.procura.gba.web.rest.v1_0.zaak.identificatie.GbaRestZaakIdentificatieToevoegenVraag;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.uittreksel.GbaRestZaakUittrekselToevoegenDeelzaak;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.uittreksel.GbaRestZaakUittrekselToevoegenVraag;
import nl.procura.gbaws.testdata.Testdata;

import examples.nl.procura.gba.GbaRestClientVoorbeelden;
import examples.nl.procura.gba.GbaRestGuice;
import examples.nl.procura.gba.GbaRestGuice.Timer;

public class GbaRestClientUittrekselToevoegenVoorbeelden extends GbaRestClientVoorbeelden {

  public GbaRestClientUittrekselToevoegenVoorbeelden() {

    try {
      toevoegenUittreksel();
    } catch (GbaRestClientException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    GbaRestGuice.getInstance(GbaRestClientUittrekselToevoegenVoorbeelden.class);
  }

  @Timer
  protected void toevoegenUittreksel() throws GbaRestClientException {
    GbaRestZaakUittrekselToevoegenVraag vraag = new GbaRestZaakUittrekselToevoegenVraag();
    vraag.setZaakId("123456");
    vraag.setZaakStatus(GbaRestZaakStatus.WACHTKAMER);
    vraag.setBurgerServiceNummer(Testdata.TEST_BSN_1);
    vraag.setAnummer(5463824651L);
    vraag.setDatumIngang("20140401");
    vraag.setBron("BSM");
    vraag.setLeverancier("PROCURA");
    vraag.setDatumInvoer(getSystemDate());
    vraag.setTijdInvoer(getSystemTime());

    GbaRestZaakUittrekselToevoegenDeelzaak deelzaak1 = vraag.addDeelzaak();
    deelzaak1.setBurgerServiceNummer(Testdata.TEST_BSN_1.toString());
    deelzaak1.setUittrekselCode("533");

    GbaRestZaakUittrekselToevoegenDeelzaak deelzaak2 = vraag.addDeelzaak();
    deelzaak2.setBurgerServiceNummer(Testdata.TEST_BSN_2.toString());
    deelzaak2.setUittrekselCode("533");

    GbaRestElementViewer.info(getObject(client.getZaak().toevoegen(vraag)));

    identificatieToevoegen();
    bestandToevoegen();
    statusToevoegen();
    aantekeningToevoegen();
    attribuutToevoegen();
  }

  private void identificatieToevoegen() throws GbaRestClientException {
    GbaRestZaakIdentificatieToevoegenVraag vraag = new GbaRestZaakIdentificatieToevoegenVraag();
    vraag.setInternId("123456");
    vraag.setType(GbaRestZaakIdType.ZAAKSYSTEEM);
    vraag.setExternId("test");

    getObject(client.getZaak().getIdentificatie().toevoegen(vraag));
  }

  private void statusToevoegen() throws GbaRestClientException {
    GbaRestZaakStatusUpdateVraag vraag = new GbaRestZaakStatusUpdateVraag();
    vraag.setZaakId("test");
    vraag.setStatus(GbaRestZaakStatus.GEANNULEERD);

    getObject(client.getZaak().setStatus(vraag));
  }

  private void attribuutToevoegen() throws GbaRestClientException {
    GbaRestZaakAttribuutToevoegenVraag vraag = new GbaRestZaakAttribuutToevoegenVraag();
    vraag.setZaakId("test");
    vraag.setAttribuut("Attribuut.tekst");

    getObject(client.getZaak().getAttribuut().toevoegen(vraag));
  }

  private void aantekeningToevoegen() throws GbaRestClientException {
    GbaRestZaakAantekeningToevoegenVraag vraag = new GbaRestZaakAantekeningToevoegenVraag();
    vraag.setZaakId("test");
    vraag.setOnderwerp("onderwerp");
    vraag.setInhoud("inhoud");

    getObject(client.getZaak().getAantekening().toevoegen(vraag));
  }

  private void bestandToevoegen() throws GbaRestClientException {
    GbaRestZaakBestandToevoegenVraag2 vraag = new GbaRestZaakBestandToevoegenVraag2();
    vraag.setZaakId("test");
    vraag.setTitel("voorbeeld2");
    vraag.setBestandsNaam("voorbeeld2.txt");
    vraag.setInhoud("Hello World".getBytes());

    getObject(client.getZaak().getBestand().toevoegen(vraag));
  }
}
