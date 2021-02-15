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

package nl.procura.gba.web.modules.beheer.sms.page2;

import static nl.procura.gba.web.modules.beheer.sms.page2.Page2SmsBean.*;

import java.util.List;

import com.vaadin.data.util.IndexedContainer;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.sms.SmsService;
import nl.procura.gba.web.services.beheer.sms.SmsTemplate;
import nl.procura.gba.web.services.beheer.sms.SmsType;
import nl.procura.sms.rest.domain.Customer;
import nl.procura.sms.rest.domain.Sender;
import nl.procura.vaadin.component.field.ProNativeSelect;

public class Page2SmsForm extends GbaForm<Page2SmsBean> {

  private final SmsTemplate smsTemplate;
  private final Customer    customer;

  public Page2SmsForm(SmsTemplate smsTemplate, Customer customer) {

    this.smsTemplate = smsTemplate;
    this.customer = customer;

    setOrder(TYPE, ACTIVE, SENDER, CONTENT, AUTO_SEND);
    setColumnWidths(WIDTH_130, "");

    Page2SmsBean bean = new Page2SmsBean();
    bean.setActive(smsTemplate.isActivated());
    bean.setContent(smsTemplate.getContent());
    bean.setAutoSend(smsTemplate.isAutoSend());

    setBean(bean);

    getField(TYPE, ProNativeSelect.class).setValue(smsTemplate.getSmsType());
    getField(SENDER, ProNativeSelect.class).setValue(smsTemplate.getSender());
  }

  @Override
  public void setBean(Object bean) {
    super.setBean(bean);
    List<Sender> senders = getSmsService().getSenders();
    List<SmsTemplate> templates = getSmsService().getTemplates(senders);
    List<SmsType> smsTypes = getSmsService().getSmsTypes(templates, smsTemplate);

    getField(TYPE, ProNativeSelect.class).setContainerDataSource(new SmsTypeContainer(smsTypes));
    getField(SENDER, ProNativeSelect.class).setContainerDataSource(new SenderContainer(customer, senders));
  }

  private SmsService getSmsService() {
    return Services.getInstance().getSmsService();
  }

  public class SenderContainer extends IndexedContainer {

    public static final String DESCRIPTION = "Description";

    public SenderContainer(Customer customer, List<Sender> senders) {
      addContainerProperty(DESCRIPTION, String.class, null);
      if (customer != null) {
        for (Sender sender : senders) {
          addItem(sender).getItemProperty(DESCRIPTION).setValue(
              sender.getName() + " (" + sender.getDescription() + ")");
        }
      }
    }
  }
}
