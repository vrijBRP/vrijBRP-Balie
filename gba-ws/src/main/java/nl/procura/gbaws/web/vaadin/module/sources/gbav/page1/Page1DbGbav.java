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

package nl.procura.gbaws.web.vaadin.module.sources.gbav.page1;

import java.util.List;

import nl.procura.gbaws.db.handlers.GbavProfileDao;
import nl.procura.gbaws.db.wrappers.GbavProfileWrapper;
import nl.procura.gbaws.web.vaadin.layouts.dialogs.DeleteProcedure;
import nl.procura.gbaws.web.vaadin.module.sources.ModuleDbPage;
import nl.procura.gbaws.web.vaadin.module.sources.gbav.page2.Page2DbGbav;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1DbGbav extends ModuleDbPage {

  private Page1DbGbavTable table;

  public Page1DbGbav() {
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonNew);
      addButton(buttonDel);

      table = new Page1DbGbavTable() {

        @Override
        public void onDoubleClick(Record record) {
          getNavigation().goToPage(new Page2DbGbav(record.getObject(GbavProfileWrapper.class)));
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
    getNavigation().goToPage(new Page2DbGbav(new GbavProfileWrapper()));
    super.onNew();
  }

  @Override
  public void onDelete() {

    new DeleteProcedure<GbavProfileWrapper>(table) {

      @Override
      protected void deleteValues(List<GbavProfileWrapper> values) {
        GbavProfileDao.removeAndCommit(values);
      }
    };

    super.onDelete();
  }
}
