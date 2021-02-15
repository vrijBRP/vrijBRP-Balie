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

import nl.procura.gba.web.rest.client.GbaRestClientException;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakStatus;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakType;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakVraag;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakVraagType;
import nl.procura.gba.web.rest.v1_0.zaak.identificatie.GbaRestZaakIdType;

import examples.nl.procura.gba.GbaRestClientVoorbeelden;
import examples.nl.procura.gba.GbaRestGuice;
import examples.nl.procura.gba.GbaRestGuice.Timer;

public class GbaRestClientZaakZoekenVoorbeelden extends GbaRestClientVoorbeelden {

  public GbaRestClientZaakZoekenVoorbeelden() throws GbaRestClientException {
    zaakZoekenMinimaal();
    zaakZoekenStandaard();
    zaakSleutelsZoeken();
  }

  public static void main(String[] args) {
    GbaRestGuice.getInstance(GbaRestClientZaakZoekenVoorbeelden.class);
  }

  @Timer
  protected void zaakZoekenMinimaal() throws GbaRestClientException {
    GbaRestZaakVraag vraag = new GbaRestZaakVraag();
    vraag.setVraagType(GbaRestZaakVraagType.MINIMAAL);
    vraag.setZaakIdType(GbaRestZaakIdType.ZAAKSYSTEEM);
    vraag.setStatussen(GbaRestZaakStatus.INCOMPLEET);
    vraag.setDatumMutatieVanaf(20170101);
    vraag.setDatumMutatieTm(20171201);

    System.out.println(getObject(client.getZaak().get(vraag)));
  }

  @Timer
  protected void zaakZoekenStandaard() throws GbaRestClientException {
    GbaRestZaakVraag vraag = new GbaRestZaakVraag();
    vraag.setVraagType(GbaRestZaakVraagType.STANDAARD);
    vraag.setTypen(GbaRestZaakType.PL_MUTATION);
    vraag.setStatussen(GbaRestZaakStatus.OPGENOMEN);

    System.out.println(getObject(client.getZaak().get(vraag)));
  }

  @Timer
  protected void zaakSleutelsZoeken() throws GbaRestClientException {
    GbaRestZaakVraag vraag = new GbaRestZaakVraag();
    vraag.setDatumVanaf(20170101);

    System.out.println(getObject(client.getZaak().getSleutels(vraag)));
  }
}
