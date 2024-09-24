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

package nl.procura.gbaws.web.vaadin.module.email.log.page1;

import java.util.List;

import nl.procura.gba.jpa.personenws.db.EmailLog;
import nl.procura.gbaws.db.handlers.EmailLogDao;
import nl.procura.gbaws.db.wrappers.EmailLogWrapper;
import nl.procura.gbaws.web.vaadin.module.email.ModuleEmailPage;
import nl.procura.gbaws.web.vaadin.module.email.log.page2.Page2EmailLog;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.pageable.PageableTableLayout;
import nl.procura.vaadin.component.table.pageable.PageableTableResult;
import nl.procura.vaadin.component.table.pageable.PageableTableSelection;

public class Page1EmailLog extends ModuleEmailPage {

  private Page1EmaiLLogTable  table;
  private PageableTableLayout tableLayout;

  public Page1EmailLog() {
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonDel);
      addButton(buttonNext);

      buttonNext.setCaption("Opnieuw verzenden (F2)");

      table = new Page1EmaiLLogTable() {

        @Override
        public void onDoubleClick(Record record) {
          getNavigation().goToPage(new Page2EmailLog(EmailLogDao.get(record.getObject(Integer.class))));
          super.onClick(record);
        }
      };

      tableLayout = new PageableTableLayout<>(table);

      addExpandComponent(tableLayout);

      onSearch();
    } else if (event.isEvent(AfterReturn.class)) {
      onSearch();
    }

    super.event(event);
  }

  @Override
  public void onSearch() {

    List<Integer> requests = EmailLogDao.getLogs();
    tableLayout.setResult(new PageableTableResult(requests));
    table.init();
  }

  @Override
  public void onNextPage() {

    List<EmailLogWrapper> records = tableLayout.getSelectedRecords();

    if (records.size() > 0) {

      int emailSents = 0;

      for (EmailLogWrapper log : records) {

        log.send();

        emailSents++;
      }

      if (emailSents == 1) {

        successMessage("De e-mail is verzonden");
      } else {

        successMessage(emailSents + " e-mails verzonden");
      }
    } else {
      throw new ProException("Geen records geselecteerd");
    }

    super.onNextPage();
  }

  @Override
  public void onDelete() {

    new PageableTableSelection<Integer>(tableLayout, PageableTableSelection.Action.VERWIJDEREN) {

      @Override
      protected void execute(List<Integer> requests) {
        EmailLogDao.removeAndCommit(requests, EmailLog.class);
      }

      @Override
      protected void afterExecution() {
        onSearch();
      }
    };

    super.onDelete();
  }
}
