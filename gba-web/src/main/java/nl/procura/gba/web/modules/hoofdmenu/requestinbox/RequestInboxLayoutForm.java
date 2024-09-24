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

package nl.procura.gba.web.modules.hoofdmenu.requestinbox;

import static nl.procura.gba.web.modules.hoofdmenu.requestinbox.RequestInboxLayoutBean.DESCRIPTION;
import static nl.procura.gba.web.modules.hoofdmenu.requestinbox.RequestInboxLayoutBean.HANDLER;
import static nl.procura.gba.web.modules.hoofdmenu.requestinbox.RequestInboxLayoutBean.ID;
import static nl.procura.gba.web.modules.hoofdmenu.requestinbox.RequestInboxLayoutBean.STATUS;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxItem;

public class RequestInboxLayoutForm extends ReadOnlyForm<RequestInboxLayoutBean> {

  public RequestInboxLayoutForm(RequestInboxItem record) {
    setCaption("Verzoek");
    setColumnWidths("130px", "400px", "130px", "");
    setOrder(ID, HANDLER, DESCRIPTION, STATUS);

    RequestInboxLayoutBean bean = new RequestInboxLayoutBean();
    bean.setId(record.getId());
    bean.setDescription(record.getDescription());
    bean.setStatus(record.getStatus().getDescr());
    bean.setHandler(record.getHandlerDescription());
    setBean(bean);
  }
}
