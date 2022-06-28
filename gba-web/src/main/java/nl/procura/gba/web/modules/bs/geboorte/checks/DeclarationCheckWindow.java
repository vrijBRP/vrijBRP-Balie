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

package nl.procura.gba.web.modules.bs.geboorte.checks;

import static nl.procura.vaadin.theme.twee.ProcuraTheme.ICOON_24.INFORMATION;
import static nl.procura.vaadin.theme.twee.ProcuraTheme.ICOON_24.WARNING;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.vaadin.component.layout.info.InfoLayout;

import lombok.Data;

@Data
public abstract class DeclarationCheckWindow extends GbaModalWindow {

  public abstract boolean isMatchValues();

  public abstract boolean isShowIcon();

  protected InfoLayout getInfoLayout() {
    String msg;
    String type = INFORMATION;
    if (isShowIcon()) {
      msg = "Deze controle is van toepassing op online aangiftes";
      if (isMatchValues()) {
        msg += "<hr>De belangrijkste persoonsgegevens komen overeen";
      } else {
        type = WARNING;
        msg += "<hr>De belangrijkste persoonsgegevens zijn verschillend";
      }
    } else {
      msg = "Deze controle is alleen van toepassing op online aangiftes";
    }

    return new InfoLayout("", type, msg);
  }
}
