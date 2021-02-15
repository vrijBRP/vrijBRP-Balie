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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.selecties;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.services.zaken.selectie.Selectie;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.theme.twee.Icons;

public class SelectieInfoLayout extends InfoLayout {

  public SelectieInfoLayout(Exception e) {
    StringBuilder msg = new StringBuilder();
    setException(msg, e);
  }

  public SelectieInfoLayout(Selectie selectie, Exception e) {
    StringBuilder msg = new StringBuilder();

    if (selectie != null) {
      setHeader(selectie.getSelectie());
      msg.append(selectie.getOmschrijving().replaceAll("\n", "</br>"));
    }

    setException(msg, e);
  }

  private void setException(StringBuilder msg, Exception e) {
    setIcon(Icons.getIcon(Icons.ICON_INFO));
    if (e != null) {
      if (!msg.toString().isEmpty()) {
        msg.append("<hr>");
      }
      msg.append(MiscUtils.setClass(false, getMessage(new StringBuilder(), e)));
      setIcon(Icons.getIcon(Icons.ICON_WARN));
    }
    setMessage(msg.toString());
  }

  private String getMessage(StringBuilder message, Throwable e) {
    if (e != null) {
      if (!message.toString().isEmpty()) {
        message.append("<br/>");
      }
      message.append(e.getMessage());
      getMessage(message, e.getCause());
    }
    return message.toString();
  }
}
