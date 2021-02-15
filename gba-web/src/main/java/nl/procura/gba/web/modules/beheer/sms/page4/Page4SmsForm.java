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

import static nl.procura.gba.web.modules.beheer.sms.page4.Page4SmsBean.DESCRIPTION;
import static nl.procura.gba.web.modules.beheer.sms.page4.Page4SmsBean.NAME;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.sms.rest.domain.Sender;

public class Page4SmsForm extends GbaForm<Page4SmsBean> {

  public Page4SmsForm(Sender sender) {

    setCaption("Verzender");
    setColumnWidths("100px", "");
    setOrder(NAME, DESCRIPTION);

    Page4SmsBean bean = new Page4SmsBean();
    bean.setName(sender.getName());
    bean.setDescription(sender.getDescription());

    setBean(bean);
  }
}
