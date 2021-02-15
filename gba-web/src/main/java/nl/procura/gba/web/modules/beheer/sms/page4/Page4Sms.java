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

package nl.procura.gba.web.modules.beheer.sms.page4;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.sms.rest.domain.Sender;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page4Sms extends NormalPageTemplate {

  private final Sender sender;
  private Page4SmsForm form;

  public Page4Sms(Sender sender) {
    super("SMS verzender");
    this.sender = sender;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      addButton(buttonPrev);
      addButton(buttonSave);
      form = new Page4SmsForm(sender);
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
    sender.setName(form.getBean().getName());
    sender.setDescription(form.getBean().getDescription());
    getServices().getSmsService().updateSender(sender);
    onPreviousPage();
    super.onSave();
  }
}
