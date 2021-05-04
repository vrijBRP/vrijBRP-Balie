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

package nl.procura.gba.web.modules.zaken.rijbewijs.errorpage;

import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Embedded;

public class RdwInfoLayout extends CustomLayout {

  private String message = "";

  public RdwInfoLayout(String message) {

    this.message = message;

    Embedded icon = new Embedded(null, new ThemeResource("../flat/icons/16/warning.png"));
    icon.setHeight("16px");
    addComponent(icon, "icon");
  }

  @Override
  public void attach() {

    build();

    super.attach();
  }

  private void build() {

    StringBuilder sb = new StringBuilder();

    if (fil(message)) {

      sb.append("<table class='rdwinfolayout'>");
      sb.append("<tr>");
      sb.append("<td class='icon'><div location=\"icon\"></div></td>");
      sb.append("<td class='message'>");
      sb.append(message);
      sb.append("</td>");
      sb.append("</tr>");
      sb.append("</table>");
    }

    setTemplateContents(sb.toString());
  }
}
