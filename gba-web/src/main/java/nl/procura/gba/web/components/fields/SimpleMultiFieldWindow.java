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

package nl.procura.gba.web.components.fields;

import com.vaadin.data.Container;
import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class SimpleMultiFieldWindow extends GbaModalWindow {

  private final Container        container;
  private final SimpleMultiField multiField;
  private MainModuleContainer    mainModule = null;
  private GbaTable               table      = null;

  public SimpleMultiFieldWindow(String caption, final Container container, final SimpleMultiField multiField) {

    super(caption + " (Druk op escape om te sluiten)", "350px");

    this.container = container;
    this.multiField = multiField;
  }

  @Override
  public void attach() {

    super.attach();

    if (mainModule == null) {

      mainModule = new MainModuleContainer();

      addComponent(mainModule);

      mainModule.getNavigation().addPage(new Module());
    }
  }

  @Override
  public void closeWindow() {

    multiField.setValues(table.getSelectedValues(Object.class));

    super.closeWindow();
  }

  public class Module extends ModuleTemplate {

    public Module() {
      setMargin(false);
    }

    @Override
    public void event(PageEvent event) {

      super.event(event);

      if (event.isEvent(InitPage.class)) {

        getPages().getNavigation().goToPage(new Page());
      }
    }
  }

  class Page extends NormalPageTemplate {

    private final Button buttonAll  = new Button("Alles");
    private final Button buttonNone = new Button("Niets");

    @Override
    public void event(PageEvent event) {

      if (event.isEvent(InitPage.class)) {

        addButton(buttonAll, 1f);
        addButton(buttonNone, 1f);
        addButton(buttonClose);

        getButtonLayout().setWidth("100%");

        buttonAll.setWidth("100%");
        buttonNone.setWidth("100%");

        table = new GbaTable() {

          @Override
          public void reloadRecords() {
            super.reloadRecords();

            for (Record record : getRecords()) {
              if (multiField.getValues().contains(record.getObject())) {
                select(record.getItemId());
              }
            }
          }

          @Override
          public void setColumns() {
            setSelectable(true);
            setMultiSelect(true);
            addColumn("Keuzes");
            super.setColumns();
          }

          @Override
          public void setRecords() {

            for (Object value : container.getItemIds()) {
              Record record = addRecord(value);
              record.addValue(value);
            }

            super.setRecords();
          }
        };

        setInfo("Met de <b>Ctrl-</b> en <b>Shift-toetsen</b> kunnen meerdere regels worden geselecteerd.");
        addComponent(table);
        table.setPageLength(0);
        table.focus();
      }

      super.event(event);
    }

    @Override
    public void handleEvent(Button button, int keyCode) {

      if (button == buttonAll) {
        table.selectAll(true);
      } else if (button == buttonNone) {
        table.selectAll(false);
      }

      super.handleEvent(button, keyCode);
    }

    @Override
    public void onClose() {
      close();
    }
  }
}
