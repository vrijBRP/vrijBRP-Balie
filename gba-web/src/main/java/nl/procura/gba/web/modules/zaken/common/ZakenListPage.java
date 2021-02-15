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

package nl.procura.gba.web.modules.zaken.common;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.status.ZaakStatusUpdater;
import nl.procura.gba.web.modules.persoonslijst.overig.PlPage;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable.ZaakRecord;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.functies.VaadinUtils;

public abstract class ZakenListPage<Z extends Zaak> extends PlPage {

  protected final ButtonStatus buttonStatus = new ButtonStatus();
  private ZakenListTable       table        = null;

  public ZakenListPage(String title) {
    super(title);
  }

  protected abstract void setTableColumns(ZakenListTable table);

  protected abstract void selectTableRecord(ZaakRecord<Z> zaakRecord);

  protected abstract void loadTableRecord(Record record, ZaakRecord<Z> zaakRecord);

  protected abstract ZaakArgumenten getZaakArgumenten();

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      table = new ZakenListTable(getZaakArgumenten()) {

        @Override
        public void onDoubleClick(Record record) {
          selectTableRecord(record.getObject(ZaakRecord.class));
        }

        @Override
        public void setColumns() {
          setSelectable(true);
          setMultiSelect(true);
          setTableColumns(this);
          super.setColumns();
        }

        @Override
        protected void loadZaak(Record record, ZaakRecord zaakRecord) {
          loadTableRecord(record, zaakRecord);
        }

        @Override
        public void onReload() {
          table.init();
        }
      };

      addExpandComponent(table);

      table.getSearchLayout().addToLayout(getButtonLayout());
    } else if (event.isEvent(AfterReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void onEnter() {
    table.init();
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonStatus) {
      onStatus();
    }

    super.handleEvent(button, keyCode);
  }

  public void onAfterDelete() {
  }

  @Override
  public void onDelete() {

    final GbaTable table = VaadinUtils.getChild(this, GbaTable.class);

    new DeleteProcedure<ZaakRecord>(table) {

      @Override
      public void afterDelete() {
        onAfterDelete();
      }

      @Override
      public void deleteValue(ZaakRecord zaakRecord) {
        getServices().getZakenService().getService(zaakRecord.getZaak()).delete(zaakRecord.getZaak());
      }
    };
  }

  private void onStatus() {

    final GbaTable table = VaadinUtils.getChild(this, GbaTable.class);

    new ZaakStatusUpdater(table) {

      @Override
      protected void reload() {
        table.init();
      }
    };
  }
}
