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

package nl.procura.gba.web.modules.hoofdmenu.sms.page1;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.sms.page2.Page2Sms;
import nl.procura.gba.web.modules.hoofdmenu.sms.page3.Page3Sms;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.sms.rest.domain.DeleteMessageRequest;
import nl.procura.sms.rest.domain.Message;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Page1Sms extends NormalPageTemplate {

  private Page1SmsLayout smsLayout;

  public Page1Sms() {
    super("SMS berichten - overzicht");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      smsLayout = new Page1SmsLayout() {

        @Override
        public void selectRecord(Record record) {
          Page1Sms.this.selectRecord(record);
        }

        @Override
        public void infoMessage(String msg) {
          Page1Sms.this.infoMessage(msg);
        }

        @Override
        public Services getServices() {
          return Page1Sms.this.getServices();
        }
      };

      if (getServices().getSmsService().isSmsServiceActive()) {

        buttonSearch.setCaption("Herladen (F3)");
        buttonSearch.setWidth("120px");
        buttonDel.setWidth("120px");

        addButton(buttonNew);
        addButton(buttonSearch);
        addButton(buttonDel);
        addButton(smsLayout.buttonVerwerk);
        smsLayout.buttonVerwerk.setEnabled(
            getApplication().isProfielActie(ProfielActie.UPDATE_HOOFD_INZAGE_SMS));
        addComponent(smsLayout.getPagingLayout());
        addExpandComponent(smsLayout.getTable());

      } else {
        addComponent(new InfoLayout("Ter informatie", "De SMS service is niet geactiveerd in de parameters."));
      }

      smsLayout.search();
    }

    if (event.isEvent(AfterReturn.class)) {
      smsLayout.search();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (smsLayout.buttonVerwerk.equals(button)) {
      smsLayout.onSend();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNew() {
    if (getServices().getSmsService().isSmsServiceActive()) {
      getNavigation().addPage(new Page3Sms());
    }
  }

  @Override
  public void onSearch() {
    smsLayout.search();
  }

  @Override
  public void onDelete() {
    if (getServices().getSmsService().isSmsServiceActive()) {
      new DeleteProcedure<Message>(smsLayout.getTable()) {

        @Override
        public void afterDelete() {
          smsLayout.search();
        }

        @Override
        public void deleteValue(Message message) {
          DeleteMessageRequest request = new DeleteMessageRequest(message.getSmsId());
          getServices().getSmsService().delete(request);
        }
      };
    }
  }

  @Override
  public void onEnter() {
    smsLayout.search();
  }

  private void selectRecord(Record record) {
    Message message = record.getObject(Message.class);
    getNavigation().goToPage(new Page2Sms(message));
  }
}
