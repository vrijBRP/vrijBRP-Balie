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
import nl.procura.gba.web.rest.v1_0.zaak.attribuut.GbaRestZaakAttribuutToevoegenVraag;
import nl.procura.gba.web.rest.v1_0.zaak.attribuut.GbaRestZaakAttribuutVerwijderenVraag;

import examples.nl.procura.gba.GbaRestClientVoorbeelden;
import examples.nl.procura.gba.GbaRestGuice;
import examples.nl.procura.gba.GbaRestGuice.Timer;

public class GbaRestClientZaakAttribuutVoorbeelden extends GbaRestClientVoorbeelden {

  public GbaRestClientZaakAttribuutVoorbeelden() throws GbaRestClientException {
    attribuutToevoegenTekst();
    attribuutToevoegenJaNee();
    attribuutToevoegenNummer();
    attribuutVerwijderen();
  }

  public static void main(String[] args) {
    GbaRestGuice.getInstance(GbaRestClientZaakAttribuutVoorbeelden.class);
  }

  @Timer
  protected void attribuutToevoegenTekst() throws GbaRestClientException {
    GbaRestZaakAttribuutToevoegenVraag vraag = new GbaRestZaakAttribuutToevoegenVraag();
    vraag.setZaakId("test");
    vraag.setAttribuut("Attribuut.tekst");

    getObject(client.getZaak().getAttribuut().toevoegen(vraag));
  }

  @Timer
  protected void attribuutToevoegenJaNee() throws GbaRestClientException {
    GbaRestZaakAttribuutToevoegenVraag vraag = new GbaRestZaakAttribuutToevoegenVraag();
    vraag.setZaakId("test");
    vraag.setAttribuut("Attribuut.boolean");

    getObject(client.getZaak().getAttribuut().toevoegen(vraag));
  }

  @Timer
  protected void attribuutToevoegenNummer() throws GbaRestClientException {
    GbaRestZaakAttribuutToevoegenVraag vraag = new GbaRestZaakAttribuutToevoegenVraag();
    vraag.setZaakId("test");
    vraag.setAttribuut("Attribuut.nummer");

    getObject(client.getZaak().getAttribuut().toevoegen(vraag));
  }

  @Timer
  protected void attribuutVerwijderen() throws GbaRestClientException {
    GbaRestZaakAttribuutVerwijderenVraag vraag = new GbaRestZaakAttribuutVerwijderenVraag();
    vraag.setZaakId("test");
    vraag.setAttribuut("Attribuut.nummer");

    getObject(client.getZaak().getAttribuut().verwijderen(vraag));
  }
}
