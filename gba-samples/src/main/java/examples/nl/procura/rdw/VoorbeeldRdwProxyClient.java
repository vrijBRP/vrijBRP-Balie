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

package examples.nl.procura.rdw;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.rdw.client.RdwClient;
import nl.procura.rdw.functions.Proces;
import nl.procura.rdw.functions.RdwUtils;

public class VoorbeeldRdwProxyClient {

  private final static Logger LOGGER = LoggerFactory.getLogger(VoorbeeldRdwProxyClient.class.getName());

  public VoorbeeldRdwProxyClient() throws FileNotFoundException {

    String url = "https://<ip>/start.rdw";
    Proces returnProces = RdwClient.send(url, new FileInputStream("src/main/resources/examples/1651-01.xml"));

    LOGGER.info("RETURN: " + returnProces);
    RdwUtils.toStream(returnProces, System.out, false, true);
  }

  public static void main(String[] args) throws FileNotFoundException {
    new VoorbeeldRdwProxyClient();
  }
}
