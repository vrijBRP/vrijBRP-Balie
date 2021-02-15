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

package nl.procura.gba.web.modules.hoofdmenu.sms.page3;

import static nl.procura.gba.web.modules.hoofdmenu.sms.page3.Page3SmsBean.*;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.sms.SmsService;
import nl.procura.gba.web.services.beheer.sms.SmsTemplate;
import nl.procura.gba.web.services.zaken.contact.MobilePhoneNumber;
import nl.procura.sms.rest.domain.Application;
import nl.procura.sms.rest.domain.Customer;
import nl.procura.sms.rest.domain.Sender;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.validation.Bsn;

public class Page3SmsForm extends GbaForm<Page3SmsBean> {

  private final Customer customer;
  private final Bsn      bsn;

  public Page3SmsForm(Customer customer) {
    this(customer, null, null);
  }

  public Page3SmsForm(Customer customer, Bsn bsn, MobilePhoneNumber mobileNumber) {

    setCaption("Nieuw bericht");
    setReadonlyAsText(true);
    setColumnWidths("170px", "");
    setOrder(CUSTOMER, APP, BSN, DESTINATION, ZAAK_ID, TEMPLATE, SENDER, MESSAGE, AUTO_SEND);

    this.customer = customer;
    this.bsn = bsn;

    Page3SmsBean b = new Page3SmsBean();
    b.setCustomer(customer.getCustomerDescription());

    if (!customer.getApplications().isEmpty()) {
      b.setApp(customer.getApplications().get(0));
    }

    if (bsn != null && bsn.isCorrect()) {
      b.setBsn(bsn.getDefaultBsn());
    }

    if (mobileNumber != null && fil(mobileNumber.getMobileNr())) {
      b.setDestination(mobileNumber.getMobileNr());
    }

    setBean(b);
  }

  @Override
  public void afterSetColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(DESTINATION)) {

    }
    super.afterSetColumn(column, field, property);
  }

  @Override
  public void afterSetBean() {
    getField(APP, ProNativeSelect.class).setContainerDataSource(new ApplicationContainer());
    getField(TEMPLATE, ProNativeSelect.class).setContainerDataSource(new TemplateContainer());
    getField(TEMPLATE).addListener((ValueChangeListener) event -> {
      SmsTemplate template = (SmsTemplate) event.getProperty().getValue();
      if (template != null) {
        getField(MESSAGE).setValue(template.getContent());
        getField(SENDER).setValue(template.getSender());
        getField(SENDER).setReadOnly(template.getSender() != null);
        getBean().setSender(template.getSender());
        repaint();
      }
    });

    getField(BSN).setReadOnly(bsn != null);
    getField(SENDER, ProNativeSelect.class).setContainerDataSource(new SenderContainer());
    super.afterSetBean();
  }

  public class ApplicationContainer extends IndexedContainer {

    public static final String DESCRIPTION = "Description";

    public ApplicationContainer() {
      addContainerProperty(DESCRIPTION, String.class, null);
      if (customer != null) {
        for (Application app : customer.getApplications()) {
          addItem(app).getItemProperty(DESCRIPTION).setValue(app.getApplicationDescription());
        }
      }
    }
  }

  public class SenderContainer extends IndexedContainer {

    public static final String DESCRIPTION = "Description";

    public SenderContainer() {
      addContainerProperty(DESCRIPTION, String.class, null);
      if (customer != null) {
        for (Sender sender : Services.getInstance().getSmsService().getSenders()) {
          addItem(sender).getItemProperty(DESCRIPTION).setValue(
              sender.getName() + " (" + sender.getDescription() + ")");
        }
      }
    }
  }

  public class TemplateContainer extends IndexedContainer {

    public static final String DESCRIPTION = "Description";

    public TemplateContainer() {
      addContainerProperty(DESCRIPTION, String.class, null);
      if (customer != null) {
        SmsService smsService = Services.getInstance().getSmsService();
        List<Sender> senders = smsService.getSenders();
        for (SmsTemplate template : smsService.getTemplates(senders)) {
          addItem(template).getItemProperty(DESCRIPTION).setValue(template.getSmsType().getOms());
        }
      }
      addItem(new SmsTemplate()).getItemProperty(DESCRIPTION).setValue("Geen sjabloon");
    }
  }
}
