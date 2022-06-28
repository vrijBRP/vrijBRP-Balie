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

package nl.procura.gba.web.modules.beheer.verkiezing.page1;

import java.util.List;

import nl.procura.gba.jpa.personen.db.KiesrVerk;
import nl.procura.gba.web.components.dialogs.DeleteRecordsFromTable;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.verkiezing.page2.Page2Verkiezing;
import nl.procura.gba.web.services.beheer.verkiezing.KiezersregisterService;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Page1Verkiezing extends NormalPageTemplate {

  private GbaTable table = null;

  public Page1Verkiezing() {
    super("Overzicht van verkiezingen");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonNew, buttonDel);

      table = new GbaTable() {

        @Override
        public void onDoubleClick(Record record) {
          getNavigation().goToPage(new Page2Verkiezing(record.getObject(KiesrVerk.class)));
        }

        @Override
        public void setColumns() {
          setSelectable(true);
          addColumn("Gemeente", 200);
          addColumn("Verkiezing", 300);
          addColumn("Datum kandidaatstelling", 200);
          addColumn("Datum verkiezing", 200);
          addColumn("Aantal stemgerechtigden");
          super.setColumns();
        }

        @Override
        public void setRecords() {
          KiezersregisterService service = getServices().getKiezersregisterService();
          List<KiesrVerk> verkiezingen = service.getVerkiezingen();
          for (KiesrVerk verkiezing : verkiezingen) {
            Record r = addRecord(verkiezing);
            r.addValue(verkiezing.getGemeente() + " (" + verkiezing.getCodeGemeenteFormat() + ")");
            r.addValue(verkiezing.getAfkVerkiezing() + " (" + verkiezing.getVerkiezing() + ")");
            r.addValue(new ProcuraDate(verkiezing.getdKand()).getFormatDate());
            r.addValue(new ProcuraDate(verkiezing.getdVerk()).getFormatDate());
            r.addValue(service.getAantalKiezers(verkiezing));
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
      public void deleteRecord(Record record) {
        Page1Verkiezing.this.deleteRecord(record);
      }
    };
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Page2Verkiezing(new KiesrVerk()));
  }

  protected void deleteRecord(Record record) {
    getServices().getKiezersregisterService().delete(record.getObject(KiesrVerk.class));
  }
}
