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

package nl.procura.gba.web.components.layouts.lazyloading;

import com.vaadin.lazyloadwrapper.LazyLoadWrapper;
import com.vaadin.ui.Component;

public abstract class LazyLayout extends LazyLoadWrapper {

  public LazyLayout(Component lazyloadComponent, int placeholderVisibleDelay) {
    super(lazyloadComponent, placeholderVisibleDelay);
  }

  public LazyLayout(Component lazyloadComponent, int placeholderVisibleDelay, String width, String height) {
    super(lazyloadComponent, 0, placeholderVisibleDelay);
    setWidth(width);
    setHeight(height);
  }

  public abstract void onVisible();

  @Override
  public void setClientSideIsVisible(boolean visible) {
    onVisible();
    super.setClientSideIsVisible(visible);
  }

  public LazyLayout setIndicatorLeft() {
    addStyleName("indicator-left");
    return this;
  }

  public LazyLayout setIndicatorNone() {
    addStyleName("indicator-none");
    return this;
  }
}
