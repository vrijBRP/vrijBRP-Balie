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

package nl.procura.gba.web.modules.beheer.onderhoud.page1.tab1;

import static nl.procura.gba.common.MiscUtils.setClass;

import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.ssl.web.rest.v1_0.connections.SslRestConnection;
import nl.procura.ssl.web.rest.v1_0.connections.SslRestConnectionMessage;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

public class VerbindingControleWindow extends ModalWindow {

  private final SslRestConnection connection;

  public VerbindingControleWindow(SslRestConnection connection) {
    this.connection = connection;
    setWidth("700px");
    setCaption("Verbinding (Druk op escape om te sluiten)");
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new Page()));
  }

  class Page extends ButtonPageTemplate {

    private Table table = new Table();

    public Page() {
      setMargin(false);
      H2 h2 = new H2(connection.getName());
      addButton(BUTTON_CLOSE);
      BUTTON_CLOSE.setWidth("100px");
      getButtonLayout().addComponent(h2, getButtonLayout().getComponentIndex(BUTTON_CLOSE));
      getButtonLayout().setExpandRatio(h2, 1f);
      getButtonLayout().setWidth("100%");
    }

    @Override
    public void event(PageEvent event) {
      if (event.isEvent(InitPage.class)) {
        setSpacing(true);
        setTable(new Table());
        addComponent(getTable());
      }

      super.event(event);
    }

    @Override
    public void onClose() {
      getWindow().closeWindow();
    }

    public Table getTable() {
      return table;
    }

    public void setTable(Table table) {
      this.table = table;
    }

    class Table extends GbaTable {

      @Override
      public void setColumns() {
        addColumn("Meldingen").setUseHTML(true);
        addStyleName(ProcuraTheme.TABLE.NEWLINE_WRAP);
        super.setColumns();
      }

      @Override
      public void setRecords() {
        connection.getMessages().forEach(message -> {
          IndexedTable.Record record = addRecord(message);
          record.addValue(toHtmlStatus(message));
        });

        super.setRecords();
      }
    }
  }

  public static String toHtmlStatus(SslRestConnectionMessage message) {
    switch (message.getType()) {
      case SUCCESS:
        return setClass(true, message.getDescription());
      case ERROR:
        return setClass(false, message.getDescription());
      case INFO:
      default:
        return message.getDescription();
    }
  }
}
