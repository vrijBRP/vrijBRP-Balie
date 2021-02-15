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

package nl.procura.gba.web.modules.beheer.email.page3;

import java.util.List;

import nl.procura.gba.web.components.dialogs.DeleteRecordsFromTable;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.services.beheer.link.PersonenLink;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Page3Email extends NormalPageTemplate {

  private GbaTable table = null;

  public Page3Email() {

    super("Overzicht verstuurde links");

    addButton(buttonDel);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      table = new GbaTable() {

        @Override
        public void setColumns() {

          setMultiSelect(true);
          setSelectable(true);

          addColumn("Nr.", 50);
          addColumn("Id", 250);
          addColumn("Type", 150);
          addColumn("Ingang", 80);
          addColumn("Einde", 80);
          addColumn("Eigenschappen");

          super.setColumns();
        }

        @Override
        public void setRecords() {

          List<PersonenLink> list = getServices().getLinkService().getLinks();

          for (PersonenLink link : list) {

            Record r = addRecord(link);

            r.addValue(link.getCLink());
            r.addValue(link.getId());
            r.addValue(link.getLinkType());
            r.addValue(link.getDatumIngang());
            r.addValue(link.getDatumEinde());
            r.addValue(link.getPropertiesAsTekst());
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
        Page3Email.this.deleteRecord(record);
      }
    };
  }

  protected void deleteRecord(Record record) {
    getServices().getLinkService().delete((PersonenLink) record.getObject());
  }
}
