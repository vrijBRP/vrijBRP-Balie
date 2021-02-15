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

package nl.procura.gba.web.modules.beheer.onderhoud.page1.tab8;

import static nl.procura.gba.web.modules.beheer.onderhoud.page1.tab8.Tab8OnderhoudPageBean.CREATED;

import nl.procura.gba.web.common.session.Sessions;
import nl.procura.gba.web.components.layouts.form.GbaForm;

public class Tab8OnderhoudPageForm extends GbaForm<Tab8OnderhoudPageBean> {

  public Tab8OnderhoudPageForm(Sessions sessions) {

    setColumnWidths("180px", "");
    setOrder(CREATED);

    Tab8OnderhoudPageBean bean = new Tab8OnderhoudPageBean();
    bean.setCreated(sessions.getInfo().getLastCreationTimeFormatted());
    bean.setDestroyed(sessions.getInfo().getLastDestructionTimeFormatted());

    setBean(bean);
  }

  @Override
  public Tab8OnderhoudPageBean getNewBean() {
    return new Tab8OnderhoudPageBean();
  }
}
