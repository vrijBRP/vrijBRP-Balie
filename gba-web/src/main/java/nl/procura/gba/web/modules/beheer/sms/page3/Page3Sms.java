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

package nl.procura.gba.web.modules.beheer.sms.page3;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.sms.page4.Page4Sms;
import nl.procura.sms.rest.domain.DeleteSenderRequest;
import nl.procura.sms.rest.domain.Sender;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Page3Sms extends NormalPageTemplate {

  private Table table = null;

  public Page3Sms() {
    super("SMS verzenders");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      if (getServices().getSmsService().isSmsServiceActive()) {
        addButton(buttonNew, buttonDel);
        table = new Table();
        addExpandComponent(table);
      } else {
        addComponent(new InfoLayout("Ter informatie", "De SMS service is niet geactiveerd in de parameters."));
      }

    }
    if (event.isEvent(AfterReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void onDelete() {
    if (getServices().getSmsService().isSmsServiceActive()) {
      new DeleteProcedure<Sender>(table) {

        @Override
        public void afterDelete() {
          onSearch();
        }

        @Override
        public void deleteValue(Sender sender) {
          DeleteSenderRequest request = new DeleteSenderRequest();
          request.setSenderId(sender.getId());
          getServices().getSmsService().deleteSender(request);
        }
      };
    }
  }

  @Override
  public void onNew() {
    if (getServices().getSmsService().isSmsServiceActive()) {
      getNavigation().goToPage(new Page4Sms(new Sender()));
    }
    super.onNew();
  }

  private void selectRecord(Record record) {
    Sender sender = record.getObject(Sender.class);
    getNavigation().goToPage(new Page4Sms(sender));
  }

  public class Table extends GbaTable {

    @Override
    public void onDoubleClick(Record record) {
      selectRecord(record);
      super.onDoubleClick(record);
    }

    @Override
    public void setColumns() {

      setSelectable(true);
      setMultiSelect(true);

      addColumn("Nr", 40);
      addColumn("Naam", 150);
      addColumn("Omschrijving");

      super.setColumns();
    }

    @Override
    public void setRecords() {
      if (getServices().getSmsService().isSmsServiceActive()) {
        int nr = 0;
        for (Sender sender : getServices().getSmsService().getSenders()) {
          Record record = addRecord(sender);
          record.addValue(++nr);
          record.addValue(sender.getName());
          record.addValue(sender.getDescription());
        }
      }
    }
  }
}
