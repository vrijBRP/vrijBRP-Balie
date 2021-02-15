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

package nl.procura.gba.web.modules.zaken.personmutations.page2;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.NativeSelect;

import nl.procura.gba.web.components.layouts.GbaHorizontalLayout;

public class Page2PersonListMutationsNavLayout extends GbaHorizontalLayout implements Button.ClickListener {

  private final Button prevButton = new Button("<");
  private final Button nextButton = new Button(">");

  private int                count;
  private int                current;
  private final NavListener  navListener;
  private final NativeSelect field;

  public Page2PersonListMutationsNavLayout(NativeSelect field, NavListener navListener) {
    setSpacing(true);
    this.field = field;
    this.navListener = navListener;

    prevButton.setWidth("40px");
    prevButton.addListener(this);

    nextButton.setWidth("40px");
    nextButton.addListener(this);

    addComponent(prevButton);
    addComponent(nextButton);
    setComponentAlignment(prevButton, Alignment.MIDDLE_LEFT);
    setComponentAlignment(nextButton, Alignment.MIDDLE_LEFT);
  }

  @Override
  public void buttonClick(Button.ClickEvent clickEvent) {

    if (clickEvent.getButton() == prevButton) {
      navListener.setCurrent(--current);
    }

    if (clickEvent.getButton() == nextButton) {
      navListener.setCurrent(++current);
    }

    checkButtons();
  }

  public void setCurrent(Object item) {
    int i = 0;
    for (Object fieldItem : field.getContainerDataSource().getItemIds()) {
      if (fieldItem == item) {
        this.current = i;
        checkButtons();
      }
      i++;
    }
  }

  public void update(int count) {
    this.count = count;
    this.current = 0;
    checkButtons();
  }

  private void checkButtons() {
    prevButton.setEnabled(current > 0);
    nextButton.setEnabled((current + 1) < count);
  }

  @FunctionalInterface
  public interface NavListener {

    void setCurrent(int current);
  }
}
