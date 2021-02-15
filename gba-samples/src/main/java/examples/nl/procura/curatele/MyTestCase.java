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

package examples.nl.procura.curatele;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.curatele.soap.CurateleSoapHandler;
import nl.procura.soap.SoapHandler;
import nl.rechtspraak.namespaces.ccr.CCRWS;

public class MyTestCase {

  private final static Logger LOGGER = LoggerFactory.getLogger(MyTestCase.class.getName());

  private SoapHandler soapHandler = null;

  public void init() {
    String url = "http://webservice.rechtspraak.nl/curateleregister.asmx";
    setSoapHandler(new CurateleSoapHandler(url, "<username>", "<password>"));
  }

  protected void log() {
    LOGGER.info("xml in    : " + soapHandler.getRequest().getXmlMessage());
    LOGGER.info("xml out   : " + soapHandler.getResponse().getXmlMessage());
  }

  protected void send() {
    List<CCRWS> result = ((CurateleSoapHandler) getSoapHandler()).send("", "Goosen", "19570115");

    for (CCRWS p : result) {
      LOGGER.info("Person: " + p);
    }
  }

  public boolean isActionSuccess() {
    return false;
  }

  public void evaluate() {
  }

  public SoapHandler getSoapHandler() {
    return soapHandler;
  }

  public void setSoapHandler(SoapHandler soapHandler) {
    this.soapHandler = soapHandler;
  }
}
