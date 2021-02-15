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

package examples.nl.procura.wk.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.diensten.gba.ple.procura.arguments.PLELoginArgs;
import nl.procura.diensten.gba.wk.baseWK.WkSearchResult;
import nl.procura.diensten.gba.wk.client.WKHTTPClient;
import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.diensten.gba.wk.utils.ObjectViewer;

import examples.nl.procura.ple.remote.AbstractTestRemote;

public class RemoteWKExample extends AbstractTestRemote {

  private final static Logger logger = LoggerFactory.getLogger(RemoteWKExample.class.getName());

  public RemoteWKExample() {

    ZoekArgumenten zoekArgumenten = new ZoekArgumenten();
    zoekArgumenten.setCode_straat("");
    zoekArgumenten.setStraatnaam("Troposfeer");
    zoekArgumenten.setHuisnummer("7");
    zoekArgumenten.setHuisletter("");
    zoekArgumenten.setHuisnummeraanduiding(" ");
    zoekArgumenten.setHuisnummertoevoeging(" ");
    zoekArgumenten.setCode_gemeentedeel("");
    zoekArgumenten.setGemeentedeel("");
    zoekArgumenten.setCode_lokatie("");
    zoekArgumenten.setLokatie("");
    zoekArgumenten.setPostcode("");

    PLELoginArgs loginArgumenten = new PLELoginArgs();
    loginArgumenten.setUname(AbstractTestRemote.USER);
    loginArgumenten.setPasswd(AbstractTestRemote.PW);

    WKHTTPClient client = new WKHTTPClient(AbstractTestRemote.URL + "/gba");

    long st = System.currentTimeMillis();

    client.setType(WKHTTPClient.OBJECT);

    long et = System.currentTimeMillis();

    WkSearchResult resultaat = client.find(zoekArgumenten, loginArgumenten);

    showAdressen(resultaat);

    logger.info("client search: " + (et - st));
  }

  public static void main(String[] args) {

    new RemoteWKExample();
  }

  private void showAdressen(WkSearchResult resultaat) {
    ObjectViewer.view(resultaat);
  }
}
