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

package nl.procura.gbaws.web.vaadin.module.auth.usr.page2;

import static nl.procura.gbaws.web.vaadin.module.auth.usr.page2.Page2AuthUsrBean.*;

import nl.procura.gbaws.web.vaadin.layouts.forms.DefaultForm;

public class Page2AuthUsrForm extends DefaultForm {

  public Page2AuthUsrForm() {

    setOrder(NAME, DISPLAY, PW, ADMIN, PROFILE);
    setColumnWidths("130px", "");
  }

  @Override
  public Object getNewBean() {
    return new Page2AuthUsrBean();
  }

  @Override
  public Page2AuthUsrBean getBean() {
    return (Page2AuthUsrBean) super.getBean();
  }
}
