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

package examples.nl.procura.bvbsn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.ictu.bsn.AfzenderDE;
import nl.procura.bvbsn.BvBsnActionTemplate;
import nl.procura.bvbsn.soap.BvBsnSoapHandler;

public class MyTestCase {

  private final static Logger LOGGER   = LoggerFactory.getLogger(MyTestCase.class.getName());
  private AfzenderDE          afzender = new AfzenderDE();
  private BvBsnActionTemplate action;

  public void init() {

    String endpoint = "https://<ip>/bvbsn60/gebruiker.asmx";
    getAction().setSoapHandler(new BvBsnSoapHandler(endpoint));
    afzender.setAfzender("PROCURA B.V.");
    afzender.setBerichtNr("1");
  }

  public void send() {

    try {
      getAction().send();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void test() {

    LOGGER.info("xml in    : " + getAction().getSoapHandler().getRequest().getXmlMessage());
    LOGGER.info("xml out   : " + getAction().getSoapHandler().getResponse().getXmlMessage());

    LOGGER.info("Verwerking: " + getAction().getVerwerking().isSucces());
    LOGGER.info(getAction().getOutputMessage());
  }

  public boolean isActionSuccess() {
    return false;
  }

  public void evaluate() {
  }

  public AfzenderDE getAfzender() {
    return afzender;
  }

  public void setAfzender(AfzenderDE afzender) {
    this.afzender = afzender;
  }

  public BvBsnActionTemplate getAction() {
    return action;
  }

  public void setAction(BvBsnActionTemplate action) {
    this.action = action;
  }
}
