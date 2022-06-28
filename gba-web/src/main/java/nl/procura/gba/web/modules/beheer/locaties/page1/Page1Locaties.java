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

package nl.procura.gba.web.modules.beheer.locaties.page1;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.services.beheer.locatie.LocatieType.AFHAAL_LOCATIE;
import static nl.procura.gba.web.services.beheer.locatie.LocatieType.NORMALE_LOCATIE;

import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;

import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.dialogs.DeleteRecordsFromTable;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.locaties.page2.Page2Locaties;
import nl.procura.gba.web.modules.beheer.locaties.page4.Page4Locaties;
import nl.procura.gba.web.modules.beheer.overig.TableSelectionCheck;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.theme.twee.Icons;

public class Page1Locaties extends NormalPageTemplate {

  private final Button buttonPrintERS = new Button("Printers koppelen");
  private GbaTable     table          = null;

  public Page1Locaties() {

    super("Overzicht van de locaties");

    addButton(buttonNew);
    addButton(buttonDel);
    addButton(buttonPrintERS);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      table = new GbaTable() {

        @Override
        public void onDoubleClick(Record record) {

          getNavigation().goToPage(new Page2Locaties((Locatie) record.getObject()));
        }

        @Override
        public void setColumns() {

          setSelectable(true);
          setMultiSelect(true);

          addColumn("Nr.", 40);
          addColumn("Type", 100);
          addColumn("Locatie", 150);
          addColumn("Omschrijving");
          addColumn("RAAS-id", 100);
          addColumn("kassa-id", 100);
          addColumn("&nbsp;", 15).setClassType(Embedded.class);
          addColumn("IP-adres (sen)", 200).setUseHTML(true);
        }

        @Override
        public void setRecords() {

          List<Locatie> list = getServices().getLocatieService().getAlleLocaties(NORMALE_LOCATIE,
              AFHAAL_LOCATIE);

          int nr = 0;

          for (Locatie loc : list) {

            nr++;

            Record r = addRecord(loc);
            r.addValue(nr);
            r.addValue(loc.getLocatieType());
            r.addValue(loc.getLocatie());
            r.addValue(loc.getOmschrijving());
            r.addValue(loc.getCodeRaas());
            r.addValue(loc.getGkasId());

            if (getServices().getLocatieService().isCorrect(loc)) {
              r.addValue("");
              r.addValue(setClass(true, loc.getIp()));
            } else {
              r.addValue(new TableImage(Icons.getIcon(Icons.ICON_ERROR)));
              r.addValue(setClass(false, loc.getIp()));
            }
          }
        }
      };

      addExpandComponent(getTable());

      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table));
    } else if (event.isEvent(AfterBackwardReturn.class)) {

      table.init();
    }

    super.event(event);
  }

  public GbaTable getTable() {
    return table;
  }

  public void setTable(GbaTable table) {
    this.table = table;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (buttonPrintERS.equals(button)) {

      TableSelectionCheck.checkSelection(table);
      List<Locatie> locaties = table.getSelectedValues(Locatie.class);
      getNavigation().goToPage(new Page4Locaties(locaties));
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onDelete() {

    new DeleteRecordsFromTable(table) {

      @Override
      public void deleteRecord(Record r) {
        Page1Locaties.this.deleteRecord(r);
      }
    };
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Page2Locaties(new Locatie()));
  }

  protected void deleteRecord(Record r) {
    getServices().getLocatieService().delete((Locatie) r.getObject());
  }
}
