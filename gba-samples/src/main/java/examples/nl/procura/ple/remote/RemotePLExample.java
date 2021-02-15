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

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.base.BasePLViewer;
import nl.procura.diensten.gba.ple.base.PLEMessage;
import nl.procura.diensten.gba.ple.base.PLEResult;
import nl.procura.diensten.gba.ple.client.PLEHTTPClient;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.gba.ple.procura.arguments.PLELoginArgs;
import nl.procura.gbaws.testdata.Testdata;

public class RemotePLExample extends AbstractTestRemote {

  private final static Logger LOGGER = LoggerFactory.getLogger(RemotePLExample.class.getName());

  public RemotePLExample(boolean showContent) {
    find(new PLEHTTPClient(URL + "/gba"), showContent);
  }

  public static void main(String[] args) {
    new RemotePLExample(true);
  }

  public PLELoginArgs getLoginArgumenten() {
    PLELoginArgs loginArgumenten = new PLELoginArgs();
    loginArgumenten.setUname(USER);
    loginArgumenten.setPasswd(PW);
    return loginArgumenten;
  }

  public PLEArgs getArgumenten() {
    PLEArgs args = new PLEArgs();
    args.addNummer(Testdata.TEST_BSN_1.toString());
    args.addCat(GBACat.PERSOON);
    args.addCat(GBACat.HUW_GPS);
    args.setShowHistory(true);
    args.setShowArchives(true);
    args.setMaxFindCount(100);
    args.setDatasource(PLEDatasource.PROCURA);

    return args;
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
}
