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

package nl.procura.gbaws.web.vaadin.module.email.log.page2;

import nl.procura.gbaws.db.wrappers.EmailLogWrapper;
import nl.procura.gbaws.web.vaadin.module.email.ModuleEmailPage;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

@SuppressWarnings("serial")
public class Page2EmailLog extends ModuleEmailPage {

  private final EmailLogWrapper emailLog;
  private Page2EmailLogForm     form = null;

  public Page2EmailLog(EmailLogWrapper emailLog) {
    this.emailLog = emailLog;
    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);

      form = new Page2EmailLogForm(emailLog);
      addComponent(form);
    }

    super.event(event);
  }
}
