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

package nl.procura.gba.web.components.layouts.page.relaties;

import com.vaadin.ui.Component;
import com.vaadin.ui.PopupView;

public class ZoekRelatiesPopup extends PopupView {

  private ZoekRelatiesLayout relatiesLayout;

  public ZoekRelatiesPopup(String small, ZoekRelatiesLayout relatiesLayout) {
    super(small, relatiesLayout);
    this.relatiesLayout = relatiesLayout;
  }

  @Override
  public void attach() {
    setContent(new Content() {

      @Override
      public String getMinimizedValueAsHTML() {
        return "Relaties";
      }

      @Override
      public Component getPopupComponent() {
        return relatiesLayout;
      }
    });

    super.attach();
  }
}
