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

package nl.procura.gba.web.modules.beheer.profielen.page1;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.SELECT;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Alignment;

import nl.procura.gba.web.components.dialogs.DeleteRecordsFromTable;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.profielen.ModuleProfielPageTemplate;
import nl.procura.gba.web.modules.beheer.profielen.components.ProfielImportExportHandler;
import nl.procura.gba.web.modules.beheer.profielen.page1.kopie.KopieProfielWindow;
import nl.procura.gba.web.modules.beheer.profielen.page2.Page2Profielen;
import nl.procura.gba.web.modules.beheer.profielen.page7.Page7Profielen;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page1Profielen extends ModuleProfielPageTemplate {

  private GbaTable table = null;

  public Page1Profielen() {

    super("Overzicht van profielen");

    addButton(buttonNew);
    addButton(buttonDel);
  }

  @Override
  public void event(PageEvent event) {

    Opties opties = VaadinUtils.addOrReplaceComponent(getButtonLayout(), new Opties());
    getButtonLayout().setComponentAlignment(opties, Alignment.MIDDLE_LEFT);

    if (event.isEvent(InitPage.class)) {

      table = new GbaTable() {

        @Override
        public void onDoubleClick(Record record) {
          getNavigation().goToPage(new Page2Profielen((Profiel) record.getObject()));
        }

        @Override
        public void setColumns() {

          setSelectable(true);
          setMultiSelect(true);
          addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

          addColumn("Code", 30);
          addColumn("Profiel", 150);
          addColumn("Omschrijving");
        }

        @Override
        public void setRecords() {

          List<Profiel> list = getServices().getProfielService().getProfielen();

          for (Profiel profiel : list) {
            Record r = addRecord(profiel);
            r.addValue(profiel.getCProfile());
            r.addValue(profiel.getProfiel());
            r.addValue(profiel.getOmschrijving());
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
  public void onDelete() {

    new DeleteRecordsFromTable(table) {

      @Override
      public void deleteRecord(Record r) {
        Page1Profielen.this.deleteRecord(r);
      }
    };
  }

  @Override
  public void onNew() {

    getNavigation().goToPage(new Page2Profielen(new Profiel()));
  }

  private void deleteRecord(Record r) {

    if (r.getObject() instanceof Profiel) {
      Profiel profiel = (Profiel) r.getObject();
      getServices().getProfielService().delete(profiel);
    }
  }

  public class Opties extends Page1ProfielenPopup {

    @Override
    public void onExporteren() {

      ArrayList<Profiel> profielen = table.getSelectedValues(Profiel.class);

      if (profielen.isEmpty()) {
        throw new ProException(SELECT, WARNING, "Geen records geselecteerd.");
      }

      new ProfielImportExportHandler().exportProfielen((GbaWindow) getWindow(), profielen);
    }

    @Override
    public void onImporteren() {

      getNavigation().goToPage(new Page7Profielen());
    }

    @Override
    public void onKopieerGebruikers() {

      getWindow().addWindow(new KopieProfielWindow());
    }
  }
}
