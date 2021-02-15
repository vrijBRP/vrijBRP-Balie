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

package examples.nl.procura.gbaws.gbav;

import nl.procura.gba.web.rest.client.GbaRestClientException;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.deblokkeren.GbaWsRestGbavDeblokkerenVraag;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.update.GbaWsRestGbavAccountUpdatenVraag;
import nl.procura.gbaws.web.rest.v1_0.gbav.wachtwoord.versturen.GbaWsRestGbavWachtwoordVersturenVraag;

import examples.nl.procura.gbaws.GbaWsRestClientVoorbeelden;

public class GbaWsRestClientGbavVoorbeelden extends GbaWsRestClientVoorbeelden {

  public GbaWsRestClientGbavVoorbeelden() throws GbaRestClientException {
    accounts();
    wachtwoordGenereren();
    accountDeblokkeren();
    //      accountUpdaten ();
    //      wachtwoordVersturen ();
  }

  public static void main(String[] args) throws GbaRestClientException {
    new GbaWsRestClientGbavVoorbeelden();
  }

  protected void accounts() throws GbaRestClientException {
    getObject(client.getGbav().getAccounts());
  }

  protected void accountDeblokkeren() throws GbaRestClientException {
    GbaWsRestGbavDeblokkerenVraag vraag = new GbaWsRestGbavDeblokkerenVraag(5);
    getObject(client.getGbav().accountDeblokkeren(vraag));
  }

  protected void accountUpdaten() throws GbaRestClientException {
    GbaWsRestGbavAccountUpdatenVraag vraag = new GbaWsRestGbavAccountUpdatenVraag();
    vraag.setCode(getFirstAccount());
    vraag.setDatum(20150102);
    vraag.setWachtwoord("123");
    getObject(client.getGbav().accountUpdaten(vraag));
  }

  protected void wachtwoordGenereren() throws GbaRestClientException {
    getObject(client.getGbav().wachtwoordGenereren());
  }

  protected void wachtwoordVersturen() throws GbaRestClientException {
    GbaWsRestGbavWachtwoordVersturenVraag vraag = new GbaWsRestGbavWachtwoordVersturenVraag();
    vraag.setCode(getFirstAccount());
    vraag.setWachtwoord("9876");
    getObject(client.getGbav().wachtwoordVersturen(vraag));
  }

  private int getFirstAccount() throws GbaRestClientException {
    return client.getGbav().getAccounts().getEntity().getAccounts().get(0).getCode();
  }
}
