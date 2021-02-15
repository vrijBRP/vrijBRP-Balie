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

package nl.procura.gbaws.web.vaadin.layouts.footer;

import static nl.procura.gba.common.MiscUtils.getBuildText;
import static nl.procura.gba.common.MiscUtils.getCopyright;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import nl.procura.vaadin.component.label.Break;

public class LoginFooterLayout extends VerticalLayout {

  @Override
  public void attach() {
    addComponent(new Break());
    addStyleName("footerLayout");

    Label label = new Label(String.format("%s<br/>%s", getBuildText(), getCopyright()), Label.CONTENT_XHTML);
    addComponent(label);
    setComponentAlignment(label, Alignment.MIDDLE_CENTER);

    super.attach();
  }
}
