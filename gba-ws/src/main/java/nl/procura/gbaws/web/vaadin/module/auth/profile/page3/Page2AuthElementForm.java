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

package nl.procura.gbaws.web.vaadin.module.auth.profile.page3;

import static nl.procura.gbaws.web.vaadin.module.auth.profile.page3.Page2AuthElementBean.*;

import nl.procura.gbaws.web.vaadin.layouts.forms.DefaultForm;
import nl.procura.gbaws.web.vaadin.module.auth.profile.page1.ElementsProfile;

public class Page2AuthElementForm extends DefaultForm {

  public Page2AuthElementForm(ElementsProfile ep) {

    setCaption("Profiel");

    Page2AuthElementBean bean = new Page2AuthElementBean();

    switch (ep.getDatabaseType()) {

      case PROCURA:
      default:
        setOrder(PROFILE, DATABASE);
        break;

      case GBA_V:
        setOrder(PROFILE, DATABASE, GBAV_ACCOUNT);
        bean.setGbavAccount(ep.getGbavProfile().toString());
        break;
    }

    setColumnWidths("100px", "");

    bean.setProfile(ep.getProfile().toString());
    bean.setDatabase(ep.getDatabaseType().toString());

    setBean(bean);
  }

  @Override
  public Object getNewBean() {
    return new Page2AuthElementBean();
  }

  @Override
  public Page2AuthElementBean getBean() {
    return (Page2AuthElementBean) super.getBean();
  }
}
