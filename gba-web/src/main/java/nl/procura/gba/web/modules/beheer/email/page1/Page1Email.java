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

package nl.procura.gba.web.modules.beheer.email.page1;

import java.util.List;

import nl.procura.gba.web.components.dialogs.DeleteRecordsFromTable;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.email.page2.Page2Email;
import nl.procura.gba.web.services.beheer.email.EmailTemplate;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Page1Email extends NormalPageTemplate {

  private GbaTable table = null;

  public Page1Email() {

    super("Overzicht van e-mail sjablonen");

    addButton(buttonNew, buttonDel);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      table = new GbaTable() {

        @Override
        public void onDoubleClick(Record record) {
          getNavigation().goToPage(new Page2Email((EmailTemplate) record.getObject()));
        }

        @Override
        public void setColumns() {

          setSelectable(true);
          setMultiSelect(true);

          addColumn("ID", 50);
          addColumn("Actief", 70);
          addColumn("Type", 200);
          addColumn("Onderwerp");
          addColumn("Van", 200);
          addColumn("Antwoord-naar", 200);

          super.setColumns();
        }

        @Override
        public void setRecords() {

          List<EmailTemplate> list = getServices().getEmailService().getTemplates();

          for (EmailTemplate template : list) {

            Record r = addRecord(template);
            r.addValue(template.getCEmail());
            r.addValue(template.isGeactiveerd() ? "Ja" : "Nee");
            r.addValue(template.getType());
            r.addValue(template.getOnderwerp());
            r.addValue(template.getVan());
            r.addValue(template.getAntwoordNaar());
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
        Page1Email.this.deleteRecord(record);
      }
    };
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Page2Email(new EmailTemplate()));
  }

  protected void deleteRecord(Record record) {
    getServices().getEmailService().delete((EmailTemplate) record.getObject());
  }
}
