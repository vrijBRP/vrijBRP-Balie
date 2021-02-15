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

package examples.nl.procura.ple.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.diensten.gba.ple.base.BasePLViewer;
import nl.procura.diensten.gba.ple.base.PLEMessage;
import nl.procura.diensten.gba.ple.base.PLEResult;
import nl.procura.diensten.gba.ple.client.PLEHTTPClient;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLELoginArgs;
import nl.procura.gbaws.testdata.Testdata;

public class RemoteAfnIndExample extends AbstractTestRemote {

  private final static Logger LOGGER = LoggerFactory.getLogger(RemoteAfnIndExample.class.getName());

  public RemoteAfnIndExample(boolean showContent) {
    PLEHTTPClient client = new PLEHTTPClient(URL + "/gba");
    find(client, showContent);
  }

  public static void main(String[] args) {
    new RemoteAfnIndExample(true);
  }

  private void find(PLEHTTPClient client, boolean showContent) {
    long st = System.currentTimeMillis();
    PLEResult resultaat = client.find(getArgumenten(), getLoginArgumenten());

    if (showContent) {
      BasePLViewer.view(resultaat.getBasePLs());
    }

    long et = System.currentTimeMillis();
    LOGGER.info("Aantal: " + resultaat.getBasePLs().size());
    for (PLEMessage message : resultaat.getMessages()) {
      LOGGER.info(message.getCode() + ": " + message.getDescr());
    }

    LOGGER.info("client search: " + (et - st));
  }

  public PLELoginArgs getLoginArgumenten() {
    PLELoginArgs loginArgumenten = new PLELoginArgs();
    loginArgumenten.setUname(USER);
    loginArgumenten.setPasswd(PW);
    return loginArgumenten;
  }

  public PLEArgs getArgumenten() {
    PLEArgs args = new PLEArgs();
    args.addNummer(Testdata.TEST_ANR_101.toString());
    args.setSearchIndications(true);
    args.setReasonForIndications("I");
    return args;
  }
}
