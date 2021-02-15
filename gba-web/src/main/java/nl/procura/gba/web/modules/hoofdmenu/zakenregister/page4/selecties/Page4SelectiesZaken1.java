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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.selecties;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.SELECT;

import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.gba.jpa.personen.dao.SelDao;
import nl.procura.gba.web.components.dialogs.DeleteRecordsFromTable;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.selecties.export.SelectieImportExportHandler;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.zaken.selectie.Selectie;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable;

public class Page4SelectiesZaken1 extends NormalPageTemplate {

  private Table1         table        = null;
  protected final Button buttonImport = new Button("Importeren");
  protected final Button buttonExport = new Button("Exporteren");

  public Page4SelectiesZaken1() {
    super("Zakenregister - selecties");
    setSpacing(true);

    buttonNew.setWidth("120px");
    buttonDel.setWidth("120px");
    buttonImport.setWidth("120px");
    buttonExport.setWidth("120px");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      if (getApplication().isProfielActie(ProfielActie.UPDATE_SELECTIES)) {
        addButton(buttonNew);
        addButton(buttonDel);
        addButton(buttonImport);
        addButton(buttonExport);
      }

      table = new Table1();
      if (getButtonLayout().getComponentCount() > 0) {
        getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table));
      } else {
        getMainbuttons().addComponent(new GbaIndexedTableFilterLayout(table));
      }
      addExpandComponent(table);

    } else if (event.isEvent(AfterReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonImport) {
      onImport();
    }
    if (button == buttonExport) {
      onExport();
    }
    super.handleEvent(button, keyCode);
  }

  private void onImport() {
    getNavigation().goToPage(new Page4SelectiesZaken4());
  }

  private void onExport() {

    List<Selectie> selecties = table.getSelectedValues(Selectie.class);

    if (selecties.isEmpty()) {
      throw new ProException(SELECT, WARNING, "Geen records geselecteerd.");
    }

    new SelectieImportExportHandler().exportSelecties((GbaWindow) getWindow(), selecties);
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Page4SelectiesZaken3(new Selectie()));
  }

  @Override
  public void onDelete() {

    new DeleteRecordsFromTable(table) {

      @Override
      public void deleteRecord(IndexedTable.Record r) {
        getServices().getSelectieService().delete(r.getObject(Selectie.class));
      }
    };
  }

  public class Table1 extends GbaTable {

    public Table1() {
      setMultiSelect(true);
    }

    @Override
    public void setColumns() {
      setSelectable(true);
      addColumn("Omschrijving", 500);
      addColumn("Aantal zaken");
    }

    @Override
    public void onDoubleClick(Record record) {
      getNavigation().goToPage(new Page4SelectiesZaken2(record.getObject(Selectie.class)));
      super.onDoubleClick(record);
    }

    @Override
    public void setRecords() {

      for (Selectie selectie : getServices().getSelectieService().getSelecties()) {
        Record r = addRecord(selectie);
        r.addValue(selectie.getSelectie());
        try {
          r.addValue(astr(SelDao.getFromStatement(selectie.getStatement()).getRows().size()));
        } catch (Exception e) {
          r.addValue("Databasemelding");
        }
      }

      super.setRecords();
    }
  }
}
