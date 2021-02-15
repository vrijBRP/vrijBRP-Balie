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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab3;

import static nl.procura.standard.Globalfunctions.trim;

import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.dialogs.DeleteRecordsFromTable;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.documenten.DocumentenTabPage;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab3.importeren.Tab3DocumentImportWindow;
import nl.procura.gba.web.services.zaken.documenten.afnemers.DocumentAfnemer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Tab3DocumentenPage1 extends DocumentenTabPage {

  protected final Button buttonImport = new Button("Importeren");
  private GbaTable       table        = null;

  public Tab3DocumentenPage1() {
    super("Overzicht van de afnemers");
    setMargin(true);
  }

  @Override
  public void event(nl.procura.vaadin.component.layout.page.pageEvents.PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      addButton(buttonNew, buttonDel, buttonImport);

      if (getWindow().isModal()) {
        addButton(buttonClose);
      }

      table = new GbaTable() {

        @Override
        public void onDoubleClick(Record record) {

          try {
            DocumentAfnemer afnemerGeklikt = (DocumentAfnemer) record.getObject();
            getNavigation().goToPage(new Tab3DocumentenPage2(afnemerGeklikt));
          } catch (Exception e) {
            getApplication().handleException(getWindow(), e);
          }
        }

        @Override
        public void setColumns() {

          setSelectable(true);
          setMultiSelect(true);

          addColumn("ID", 50);
          addColumn("Afnemer");
          addColumn("Ter attentie van");
          addColumn("Adres");
          addColumn("Grondslag");
          addColumn("Verstrek. bep.");
        }

        @Override
        public void setRecords() {

          List<DocumentAfnemer> list = getServices().getDocumentService().getAfnemers();

          for (DocumentAfnemer documentAfnemer : list) {
            Record r = addRecord(documentAfnemer);

            r.addValue(documentAfnemer.getCDocumentAfn());
            r.addValue(documentAfnemer.getDocumentAfn());

            StringBuilder adres = new StringBuilder();
            adres.append(documentAfnemer.getAdres());
            adres.append(" ");
            adres.append(documentAfnemer.getPostcode());
            adres.append(" ");
            adres.append(documentAfnemer.getPlaats());

            r.addValue(documentAfnemer.getTav());
            r.addValue(adres);
            r.addValue(documentAfnemer.getGrondslagType());

            StringBuilder vb = new StringBuilder();
            vb.append((documentAfnemer.isVerstrekBep() ? "Ja" : "Nee"));
            vb.append(", ");
            vb.append(documentAfnemer.getToekenning());

            r.addValue(trim(vb.toString()));
          }
        }
      };

      addExpandComponent(table);

      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table));
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonImport) {
      onImport();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onClose() {

    if (getWindow().isModal()) {
      getWindow().closeWindow();
    }

    super.onClose();
  }

  @Override
  public void onDelete() {

    new DeleteRecordsFromTable(table) {

      @Override
      public void deleteRecord(Record r) {
        Tab3DocumentenPage1.this.deleteRecord(r);
      }
    };
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Tab3DocumentenPage2(new DocumentAfnemer()));
  }

  protected void deleteRecord(Record r) {
    getServices().getDocumentService().delete((DocumentAfnemer) r.getObject());
  }

  private void onImport() {

    getApplication().getParentWindow().addWindow(new Tab3DocumentImportWindow() {

      @Override
      public void windowClose(CloseEvent e) {
        table.init();
        super.windowClose(e);
      }
    });
  }

}
