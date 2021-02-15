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

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.sms.rest.domain.AddMessageRequest;
import nl.procura.sms.rest.domain.Customer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.validation.Bsn;

public class Page3Sms extends NormalPageTemplate {

  private Page3SmsForm form;

  public Page3Sms() {
    super("SMS bericht - Nieuw");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      addButton(buttonPrev);
      addButton(buttonSave);
      Customer customer = getServices().getSmsService().getCustomers().getCustomer();
      form = new Page3SmsForm(customer);
      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    form.commit();

    Page3SmsBean bean = form.getBean();
    Bsn bsn = new Bsn(bean.getBsn());
    AddMessageRequest request = new AddMessageRequest();
    request.setApplicationName(bean.getApp().getApplicationName());
    request.setBsn(bsn.isCorrect() ? bsn.getLongBsn() : null);
    request.setDestination(bean.getDestination());
    request.setZaakId(bean.getZaakId());
    request.setSender(bean.getSender());
    request.setAutoSend(bean.isAutoSend());
    request.setSmsMessage(bean.getMessage());

    getServices().getSmsService().addMessage(request);

    onPreviousPage();

    super.onSave();
  }
}
