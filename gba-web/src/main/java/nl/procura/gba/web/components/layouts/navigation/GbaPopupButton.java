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

package nl.procura.gba.web.components.layouts.navigation;

import org.vaadin.hene.popupbutton.PopupButton;

import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Table;

import nl.procura.vaadin.theme.twee.ProcuraTweeTheme;

public class GbaPopupButton extends PopupButton {

  private final Tab tab = new Tab();

  public GbaPopupButton(String caption) {
    this(caption, "", "");
  }

  public GbaPopupButton(String caption, String buttonWidth, String optionsWidth) {

    super(caption);

    setWidth(buttonWidth, optionsWidth);
    addComponent(tab);
  }

  public void addChoice(Choice choice) {

    tab.addItem(new Object[]{ choice.getCaption() }, choice);

    tab.setPageLength(tab.getItemIds().size());
  }

  public boolean isChoice(String caption) {
    return getChoice(caption) != null;
  }

  /**
   * Verwijdert alle keuzes
   */
  public void removeChoices() {

    tab.removeAllItems();
  }

  @Override
  public void setWidth(String width) {
    setWidth(width, width);
  }

  @SuppressWarnings("unused")
  protected void onChoiceClick(Choice choice) {
  } // Override

  private Choice getChoice(String caption) {

    for (Object item : tab.getItemIds()) {
      if (item instanceof Choice) {
        Choice choice = (Choice) item;
        if (caption.equalsIgnoreCase(choice.getCaption())) {
          return choice;
        }
      }
    }

    return null;
  }

  private void setWidth(String buttonWidth, String optionsWidth) {

    super.setWidth(buttonWidth);

    tab.setWidth(optionsWidth);
  }

  public static class Choice {

    private String caption = "";

    public Choice(String caption) {

      setCaption(caption);
    }

    public String getCaption() {
      return caption;
    }

    public void setCaption(String caption) {
      this.caption = caption;
    }

    public void onClick() {
    }
  }

  public class Tab extends Table {

    public Tab() {

      setWidth("100%");

      setPageLength(4);

      addContainerProperty("Keuze", String.class, null);

      addStyleName(ProcuraTweeTheme.TABLE.CLICKABLE);

      setSelectable(false);

      setImmediate(true);

      addListener((ItemClickListener) event -> {

        Choice choice = (Choice) event.getItemId();

        choice.onClick();
      });
    }
  }
}
