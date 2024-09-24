/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.amp;

import java.util.List;

import nl.amp.api.domain.GeannuleerdeOrder;
import nl.amp.api.domain.ObjectFactory;
import nl.amp.api.domain.OpgehaaldeOrder;
import nl.amp.api.domain.OphalenAnnuleringenRequest;
import nl.amp.api.domain.OphalenOrdersRequest;
import nl.amp.api.domain.OphalenUitreikingenRequest;
import nl.amp.api.domain.UitreikingenOrder;
import nl.procura.amp.AMPSoapHandler;

public class AmpExample {

  public static void main(String[] args) {
    new AmpExample();
  }

  public AmpExample() {
    ophalenOrders();
  }

  public void ophalenOrders() {
    String url = "http://srv-411t:85/cert-manager/proxy?url=https://acp.ampesb.nl:12400/ThuisbezorgenReisdocumentService";
    AMPSoapHandler handler = new AMPSoapHandler(url);
    ophalenOrders(handler);
    ophalenAnnuleringen(handler);
    ophalenUitreikingen(handler);
  }

  private void ophalenOrders(AMPSoapHandler handler) {
    List<OpgehaaldeOrder> orders = handler.ophalenOrders(ophalenOrdersRequest())
        .getOrders().getValue()
        .getOpgehaaldeOrder();

    for (OpgehaaldeOrder order : orders) {
      System.out.printf("Ophalen orders: %d %s%n",
          order.getAanvraagnummer(), order.getOrderreferentieid().getValue());
    }
  }

  private void ophalenAnnuleringen(AMPSoapHandler handler) {
    List<GeannuleerdeOrder> orders = handler.ophalenAnnuleringen(getAnnuleringsRequest())
        .getGeannuleerdeOrders().getGeannuleerdeOrder();
    for (GeannuleerdeOrder order : orders) {
      System.out.printf("Ophalen annuleringen: %s %s %s%n",
          order.getOrderreferentieid(), order.getCode(), order.getOmschrijving());
    }
  }

  private OphalenAnnuleringenRequest getAnnuleringsRequest() {
    ObjectFactory of = new ObjectFactory();
    return of.createOphalenAnnuleringenRequest()
        .withGemeentecode((short) 1963);
  }

  private void ophalenUitreikingen(AMPSoapHandler handler) {
    List<UitreikingenOrder> orders = handler.ophalenUitreikingen(getUitreikingenRequest())
        .getOrders().getUitreikingenOrder();
    for (UitreikingenOrder order : orders) {
      System.out.printf("Ophalen uitreikingen: %s%n",
          order.getOrderreferentieid());
    }
  }

  private OphalenUitreikingenRequest getUitreikingenRequest() {
    ObjectFactory of = new ObjectFactory();
    return of.createOphalenUitreikingenRequest()
        .withGemeentecode((short) 1963);
  }

  private OphalenOrdersRequest ophalenOrdersRequest() {
    ObjectFactory of = new ObjectFactory();
    return of.createOphalenOrdersRequest()
        .withGemeentecode((short) 1963);
  }
}
