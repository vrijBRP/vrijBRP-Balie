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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab5.page1;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;

import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.dialogs.DeleteRecordsFromTable;
import nl.procura.gba.web.components.layouts.navigation.GbaPopupButton;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.documenten.DocumentenTabPage;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab5.PrintOptieValue;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab5.PrinterContainer;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab5.page2.Tab5DocumentenPage2;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab5.page4.Tab5DocumentenPage4;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab5.page5.Tab5DocumentenPage5;
import nl.procura.gba.web.modules.beheer.overig.TableSelectionCheck;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.functies.VaadinUtils;
import nl.procura.vaadin.theme.twee.Icons;

public class Tab5DocumentenPage1 extends DocumentenTabPage {

  private List<PrintOptieValue> printers;
  private GbaTable              table = null;

  public Tab5DocumentenPage1() {
    super("Overzicht van de printopties");
    setMargin(true);
    addButtons();
  }

  @Override
  public void event(PageEvent event) {

    Opties opties = VaadinUtils.addOrReplaceComponent(getButtonLayout(), new Opties());
    getButtonLayout().setComponentAlignment(opties, Alignment.MIDDLE_LEFT);

    if (event.isEvent(InitPage.class)) {
      printers = new PrinterContainer(getServices()).getPrinters();
      setTable();
      addExpandComponent(table);
      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table));

    } else if (event.isEvent(AfterReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void onDelete() {

    new DeleteRecordsFromTable(table) {

      @Override
      public void deleteRecord(Record r) {
        Tab5DocumentenPage1.this.deleteRecord(r);
      }
    };
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Tab5DocumentenPage2(new PrintOptie()));
  }

  protected void deleteRecord(Record r) {
    getServices().getPrintOptieService().delete((PrintOptie) r.getObject());
  }

  private void addButtons() {

    addButton(buttonNew, buttonDel);
  }

  private List<PrintOptie> getSelectedPrintOpties() {
    List<PrintOptie> printopties = new ArrayList<>();

    for (Record r : table.getSelectedRecords()) {
      PrintOptie printOptie = (PrintOptie) r.getObject();
      printopties.add(printOptie);
    }
    return printopties;
  }

  private boolean printerAanwezig(PrintOptie printOptie) {
    boolean aanwezig = printers.contains(new PrintOptieValue(printOptie.getPrintType(), printOptie.getPrintoptie()));

    if (!aanwezig) {
      printOptie.setPrintoptie(null);
    }

    return aanwezig;
  }

  private void setTable() {

    table = new GbaTable() {

      @Override
      public void onDoubleClick(Record record) {

        PrintOptie printOptie = (PrintOptie) record.getObject();
        boolean isPrinterAanwezig = printerAanwezig(printOptie);
        getNavigation().goToPage(new Tab5DocumentenPage2(printOptie, isPrinterAanwezig));
      }

      @Override
      public void setColumns() {

        setSelectable(true);
        setMultiSelect(true);

        addColumn("", 30).setClassType(Embedded.class);
        addColumn("ID", 50);
        addColumn("Type", 130);
        addColumn("Naam", 250);
        addColumn("Opties");
        addColumn("Uitvoer naar", 250);
      }

      @Override
      public void setRecords() {
        List<PrintOptie> list = getServices().getPrintOptieService().getPrintOpties();

        for (PrintOptie printOptie : list) {
          Record r = table.addRecord(printOptie);
          r.addValue(printerAanwezig(printOptie) ? "" : new TableImage(Icons.getIcon(Icons.ICON_ERROR)));
          r.addValue(printOptie.getCPrintoptie());
          r.addValue(printOptie.getPrintType().getDescr());
          r.addValue(printOptie.getOms());
          r.addValue(printOptie.getBeschrijving());
          r.addValue(printOptie.getLocatie());
        }

        super.setRecords();
      }
    };
  }

  public class Opties extends GbaPopupButton {

    public Opties() {

      super("Opties", "150px", "145px");

      addChoice(new Choice("Documenten koppelen") {

        @Override
        public void onClick() {

          TableSelectionCheck.checkSelection(table);
          List<PrintOptie> printOpties = getSelectedPrintOpties();
          getNavigation().goToPage(new Tab5DocumentenPage5(printOpties));
        }
      });

      addChoice(new Choice("Locaties koppelen") {

        @Override
        public void onClick() {

          TableSelectionCheck.checkSelection(table);
          List<PrintOptie> printOpties = getSelectedPrintOpties();
          getNavigation().goToPage(new Tab5DocumentenPage4(printOpties));
        }
      });
    }
  }
}
