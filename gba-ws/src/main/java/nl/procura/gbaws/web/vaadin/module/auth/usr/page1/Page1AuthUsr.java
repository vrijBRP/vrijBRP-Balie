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

package nl.procura.gbaws.web.vaadin.module.auth.usr.page1;

import java.util.List;

import nl.procura.gbaws.db.handlers.UsrDao;
import nl.procura.gbaws.db.wrappers.UsrWrapper;
import nl.procura.gbaws.web.vaadin.layouts.dialogs.DeleteProcedure;
import nl.procura.gbaws.web.vaadin.module.auth.ModuleAuthPage;
import nl.procura.gbaws.web.vaadin.module.auth.usr.page2.Page2AuthUsr;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1AuthUsr extends ModuleAuthPage {

  private Page1AuthUsrTable table;

  public Page1AuthUsr() {
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonNew);
      addButton(buttonDel);

      table = new Page1AuthUsrTable() {

        @Override
        public void onDoubleClick(Record record) {

          getNavigation().goToPage(new Page2AuthUsr(record.getObject(UsrWrapper.class)));

          super.onClick(record);
        }
      };

      addExpandComponent(table);
    } else if (event.isEvent(AfterReturn.class)) {

      table.init();
    }

    super.event(event);
  }

  @Override
  public void onNew() {

    getNavigation().goToPage(new Page2AuthUsr(new UsrWrapper()));

    super.onNew();
  }

  @Override
  public void onDelete() {

    new DeleteProcedure<UsrWrapper>(table) {

      @Override
      protected void deleteValues(List<UsrWrapper> values) {

        UsrDao.removeAndCommit(values);
      }
    };

    super.onDelete();
  }
}
