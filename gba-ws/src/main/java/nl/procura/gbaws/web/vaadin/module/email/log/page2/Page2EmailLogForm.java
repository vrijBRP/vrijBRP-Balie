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

import static nl.procura.gbaws.web.vaadin.module.email.log.page2.Page2EmailLogBean.*;

import nl.procura.gbaws.db.wrappers.EmailLogWrapper;
import nl.procura.gbaws.web.vaadin.layouts.forms.DefaultForm;

public class Page2EmailLogForm extends DefaultForm {

  public Page2EmailLogForm(EmailLogWrapper emailLog) {

    setCaption("E-mail");
    setColumnWidths("130px", "");
    setOrder(STATUS, SUBJECT, BODY, D_IN, ERROR);

    Page2EmailLogBean bean = new Page2EmailLogBean();

    bean.setStatus(emailLog.getSentMessage());
    bean.setSubject(emailLog.getTable().getSubject());
    bean.setBody(emailLog.getTable().getBody().replaceAll("\n", "</br>"));
    bean.setdIn(emailLog.getDateTime());
    bean.setError(emailLog.getError());

    setBean(bean);
  }

  @Override
  public Page2EmailLogBean getBean() {
    return (Page2EmailLogBean) super.getBean();
  }
}
