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

package nl.procura.gba.web.modules.bs.registration.person;

import static java.util.Collections.unmodifiableList;

import java.util.List;
import java.util.function.Consumer;

import nl.procura.gba.web.components.layouts.proces.ProcessLayout;
import nl.procura.gba.web.modules.bs.registration.person.modules.AbstractPersonPage;
import nl.procura.vaadin.component.layout.page.PageLayout;
import nl.procura.vaadin.component.layout.page.PageNavigation;

class ButtonNavigation {

  private final PageNavigation             navigation;
  private final Consumer<ButtonNavigation> listener;

  private final List<ProcessLayout.ProcessButton> buttons;
  private int                                     current;

  public ButtonNavigation(PageNavigation navigation, List<ProcessLayout.ProcessButton> buttons,
      Consumer<ButtonNavigation> listener) {
    this.navigation = navigation;
    this.buttons = unmodifiableList(buttons);
    this.listener = listener;
  }

  public void goTo(int index) {
    buttons.get(index).onClick();
  }

  public void goTo(AbstractPersonPage page, ProcessLayout.ProcessButton button) {
    int index = buttons.indexOf(button);
    PageLayout currentPage = navigation.getCurrentPage();
    // only going forward requires validation
    if (currentPage instanceof AbstractPersonPage && index > current) {
      ((AbstractPersonPage) currentPage).checkPage(() -> goTo(page, index));
    } else {
      // no validation and changes are lost
      goTo(page, index);
    }
  }

  private void goTo(PageLayout page, int index) {
    navigation.goToPage(page);
    current = index;
    listener.accept(this);
  }

  public boolean hasNext() {
    return current < buttons.size() - 1;
  }

  public boolean hasPrevious() {
    return current > 0;
  }

  public void previous() {
    if (hasPrevious()) {
      goTo(current - 1);
    }
  }

  public void next(Runnable noNextAction) {
    PageLayout currentPage = navigation.getCurrentPage();
    if (currentPage instanceof AbstractPersonPage) {
      AbstractPersonPage page = (AbstractPersonPage) currentPage;
      if (hasNext()) {
        page.onNextPage(() -> goTo(current + 1));
      } else {
        page.checkPage(() -> page.onNextPage(noNextAction));
      }
    }
  }

}
