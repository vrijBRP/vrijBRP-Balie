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

package nl.procura.gba.web.modules.beheer.kassa.page1;

import java.util.List;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;

import nl.procura.gba.web.components.dialogs.DeleteRecordsFromTable;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.kassa.page2.Page2Kassa;
import nl.procura.gba.web.services.beheer.kassa.KassaProduct;
import nl.procura.gba.web.services.beheer.kassa.KassaUtils;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Page1Kassa extends NormalPageTemplate {

  private final Button buttonOntbrekende = new Button("Ontbrekende");
  private GbaTable     table             = null;

  public Page1Kassa() {
    super("Overzicht van de kassagegevens");

  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonNew, buttonDel, buttonOntbrekende);

      table = new GbaTable() {

        @Override
        public void init() {

          checkAantalOntbrekend();

          super.init();
        }

        @Override
        public void onDoubleClick(Record record) {
          try {
            getNavigation().goToPage(new Page2Kassa((KassaProduct) record.getObject()));
          } catch (Exception e) {
            getApplication().handleException(getWindow(), e);
          }
        }

        @Override
        public void setColumns() {

          setSelectable(true);
          setMultiSelect(true);

          addColumn("ID", 50);
          addColumn("Kassacode", 150);
          addColumn("Type", 200);
          addColumn("Omschrijving");

          super.setColumns();
        }

        @Override
        public void setRecords() {

          List<KassaProduct> list = getServices().getKassaService().getKassaProducten();

          for (KassaProduct kassa : list) {

            Record r = addRecord(kassa);
            r.addValue(kassa.getCKassa());
            r.addValue(kassa.getKassa());
            r.addValue(kassa.getKassaType());
            r.addValue(kassa.getDescr());
          }
        }
      };

      addExpandComponent(table);

      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table));
    } else if (event.isEvent(AfterBackwardReturn.class)) {

      table.init();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if ((button == buttonOntbrekende) || (keyCode == ShortcutAction.KeyCode.F4)) {
      addMissing();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onDelete() {

    new DeleteRecordsFromTable(table) {

      @Override
      public void deleteRecord(Record r) {
        Page1Kassa.this.deleteRecord(r);
      }
    };
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Page2Kassa(new KassaProduct()));
  }

  protected void deleteRecord(Record r) {
    getServices().getKassaService().delete((KassaProduct) r.getObject());
    checkAantalOntbrekend();
  }

  private void addMissing() {

    List<KassaProduct> ontbrekendeProducten = KassaUtils.getOntbrekendeProducten(getServices().getKassaService());

    if (ontbrekendeProducten.size() > 0) {

      OntbrekendeKassaWindow window = new OntbrekendeKassaWindow(ontbrekendeProducten) {

        @Override
        protected void close() {

          table.init();

          super.close();
        }
      };

      getParentWindow().addWindow(window);
    } else {
      infoMessage("Er zijn geen ontbrekende producten");
    }
  }

  private void checkAantalOntbrekend() {
    int aantal = KassaUtils.getOntbrekendeProducten(getServices().getKassaService()).size();
    buttonOntbrekende.setCaption("Ontbrekende producten (" + aantal + ")");
  }
}
