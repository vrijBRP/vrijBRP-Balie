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

package nl.procura.gba.web.components.layouts.page.zoekhistorie;

import com.vaadin.ui.Component;
import com.vaadin.ui.PopupView;

public class ZoekHistoriePopup extends PopupView {

  private Component component = null;

  public ZoekHistoriePopup(String small, Component component) {

    super(small, component);
    setComponent(component);
  }

  @Override
  public void attach() {

    setContent(new Content() {

      @Override
      public String getMinimizedValueAsHTML() {
        return "Historie";
      }

      @Override
      public Component getPopupComponent() {
        return getComponent();
      }
    });

    super.attach();
  }

  public Component getComponent() {
    return component;
  }

  public void setComponent(Component component) {
    this.component = component;
  }
}
