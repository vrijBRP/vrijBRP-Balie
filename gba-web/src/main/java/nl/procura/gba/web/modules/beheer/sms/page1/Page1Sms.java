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

package nl.procura.gba.web.modules.beheer.sms.page1;

import java.util.List;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.dialogs.DeleteRecordsFromTable;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.sms.page2.Page2Sms;
import nl.procura.gba.web.services.beheer.sms.SmsTemplate;
import nl.procura.sms.rest.domain.Sender;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Page1Sms extends NormalPageTemplate {

  private GbaTable table = null;

  public Page1Sms() {
    super("Overzicht van SMS sjablonen");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      if (getServices().getSmsService().isSmsServiceActive()) {

        addButton(buttonNew, buttonDel);

        table = new GbaTable() {

          @Override
          public void onDoubleClick(Record record) {
            getNavigation().goToPage(new Page2Sms((SmsTemplate) record.getObject()));
          }

          @Override
          public void setColumns() {

            setSelectable(true);
            setMultiSelect(true);

            addColumn("ID", 50);
            addColumn("Actief", 70);
            addColumn("Autoverzending", 100);
            addColumn("SMS afzender", 200).setUseHTML(true);
            addColumn("Type");
            addColumn("SMS bericht");

            super.setColumns();
          }

          @Override
          public void setRecords() {

            if (getServices().getSmsService().isSmsServiceActive()) {
              List<Sender> senders = getServices().getSmsService().getSenders();
              List<SmsTemplate> list = getServices().getSmsService().getTemplates(senders);

              for (SmsTemplate template : list) {
                Record r = addRecord(template);
                r.addValue(template.getCSms());
                r.addValue(template.isActivated() ? "Ja" : "Nee");
                r.addValue(template.isAutoSend() ? "Ja" : "Nee");
                r.addValue(getSenders(template.getSender()));
                r.addValue(template.getSmsType());
                r.addValue(template.getContent());
              }
            }
          }
        };
        addExpandComponent(table);
        getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table));
      } else {
        addComponent(new InfoLayout("Ter informatie", "De SMS service is niet geactiveerd in de parameters."));
      }

    } else if (event.isEvent(AfterBackwardReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void onDelete() {
    if (getServices().getSmsService().isSmsServiceActive()) {
      new DeleteRecordsFromTable(table) {

        @Override
        public void deleteRecord(Record record) {
          Page1Sms.this.deleteRecord(record);
        }
      };
    }
  }

  @Override
  public void onNew() {
    if (getServices().getSmsService().isSmsServiceActive()) {
      getNavigation().goToPage(new Page2Sms(new SmsTemplate()));
    }
  }

  protected void deleteRecord(Record record) {
    getServices().getSmsService().delete((SmsTemplate) record.getObject());
  }

  private String getSenders(Sender sender) {
    return sender != null ? sender.getName() + " (" + sender.getDescription() + ")"
        : MiscUtils.setClass(false,
            "Niet bekend");
  }
}
