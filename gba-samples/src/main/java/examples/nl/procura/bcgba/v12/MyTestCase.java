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

package examples.nl.procura.bcgba.v12;

import nl.bprbzk.bcgba.v12.AfzenderDE;
import nl.procura.bcgba.v12.BcGbaActionTemplate;
import nl.procura.bcgba.v12.soap.BcGbaSoapHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyTestCase {

  private AfzenderDE          afzender = new AfzenderDE();
  private BcGbaActionTemplate action;

  public MyTestCase() {
    afzender.setAfzender("<gemeentecode>");
    afzender.setBerichtNr("1");
  }

  public void init() {
    String endpoint = "https://demo.procura.nl/ssl-web/proxy?url=https://bcgba.burgerservicenummer.nl/bcgba.asmx";
    action.setSoapHandler(new BcGbaSoapHandler(endpoint));
  }

  public void send() {
    action.send();
  }

  public void test() {
    log.info("xml in    : " + getAction().getSoapHandler().getRequest().getXmlMessage());
    log.info("xml out   : " + getAction().getSoapHandler().getResponse().getXmlMessage());

    verwerkingSuccess();
    actionSuccess();

    log.info("Verwerking: " + getAction().getVerwerking().isSucces());
    log.info(getAction().getOutputMessage());
  }

  public boolean isActionSuccess() {
    return false;
  }

  public void actionSuccess() {
  }

  public void verwerkingSuccess() {
  }

  public AfzenderDE getAfzender() {
    return afzender;
  }

  public void setAfzender(AfzenderDE afzender) {
    this.afzender = afzender;
  }

  public BcGbaActionTemplate getAction() {
    return action;
  }

  public void setAction(BcGbaActionTemplate action) {
    this.action = action;
  }
}
