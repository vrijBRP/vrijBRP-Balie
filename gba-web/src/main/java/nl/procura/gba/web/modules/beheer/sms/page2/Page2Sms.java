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

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.beheer.sms.SmsTemplate;
import nl.procura.sms.rest.domain.Customer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2Sms extends NormalPageTemplate {

  private Page2SmsForm form = null;
  private SmsTemplate  smsTemplate;

  public Page2Sms(SmsTemplate smsTemplate) {
    super("Toevoegen / muteren e-mail sjablonen");
    this.smsTemplate = smsTemplate;
    addButton(buttonPrev, buttonNew, buttonSave);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      final Customer customer = getServices().getSmsService().getCustomers().getCustomer();
      form = new Page2SmsForm(smsTemplate, customer);
      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onNew() {
    this.smsTemplate = new SmsTemplate();
    form.reset();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {
    doSave();
    successMessage("De gegevens zijn opgeslagen.");
  }

  private void doSave() {

    form.commit();

    Page2SmsBean bean = form.getBean();
    smsTemplate.setSmsType(bean.getType());
    smsTemplate.setActivated(bean.isActive());
    smsTemplate.setSenderId(bean.getSender().getId());
    smsTemplate.setContent(bean.getContent());
    smsTemplate.setAutoSend(bean.isAutoSend());

    getServices().getSmsService().save(smsTemplate);
  }
}
