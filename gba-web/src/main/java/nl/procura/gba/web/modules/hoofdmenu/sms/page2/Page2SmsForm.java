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

package nl.procura.gba.web.modules.hoofdmenu.sms.page2;

import static nl.procura.gba.web.modules.hoofdmenu.sms.page2.Page2SmsBean.*;
import static nl.procura.standard.Globalfunctions.astr;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.sms.rest.domain.Application;
import nl.procura.sms.rest.domain.Customer;
import nl.procura.sms.rest.domain.Message;
import nl.procura.sms.rest.domain.Sender;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page2SmsForm extends ReadOnlyForm {

  public Page2SmsForm(Message message) {

    setCaption("Melding");
    setReadonlyAsText(false);
    setColumnWidths("170px", "", "170px", "");
    setOrder(UID, CUSTOMER, CODE, APP, TIMESTAMP, SENDER, DESTINATION, SEND_TO_SERVICE, BSN, SEND_TO_DESTINATION,
        ZAAK_ID, AUTO_SEND, MESSAGE);

    Customer customer = message.getCustomer();
    Application app = message.getApplication();
    Sender sender = message.getSender();

    Page2SmsBean b = new Page2SmsBean();
    b.setCode(message.getMessageCode());
    b.setUid(message.getSmsId());
    b.setDestination(message.getDestination());
    b.setBsn(astr(message.getBsn()));
    b.setZaakId(astr(message.getZaakId()));
    b.setTimestamp(new ProcuraDate(message.getTimestamp()).getFormatDate("MM-dd-yyyy HH:mm:ss"));
    b.setCustomer(customer.getCustomerDescription());
    b.setApp(app.getApplicationDescription());
    b.setSender(sender.getName() + " (" + sender.getDescription() + ")");
    b.setSendToService(message.isSendToSmsService() ? "Ja" : "Nee");
    b.setAutoSend(message.isAutoSend() ? "Ja" : "Nee");
    b.setMessage(message.getMessage());

    StringBuilder sendToDestination = new StringBuilder();
    if (message.isSendToDestinationSuccess()) {
      sendToDestination.append("Ja");
    } else if (message.isSendToDestinationFailed()) {
      sendToDestination.append("Nee");
    } else {
      sendToDestination.append("Nog onbekend");
    }
    b.setSendToDestination(sendToDestination.toString());

    setBean(b);
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {

    if (property.is(MESSAGE)) {
      column.setColspan(3);
    }
    super.afterSetColumn(column, field, property);
  }

  @Override
  public Page2SmsBean getBean() {
    return (Page2SmsBean) super.getBean();
  }
}
