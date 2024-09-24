/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gbaws.web.vaadin.module.requests.page1;

import java.util.List;

import nl.procura.gba.jpa.personenws.db.Request;
import nl.procura.gbaws.db.handlers.RequestDao;
import nl.procura.gbaws.db.wrappers.UsrWrapper;
import nl.procura.gbaws.web.vaadin.layouts.DefaultPageLayout;
import nl.procura.gbaws.web.vaadin.layouts.tables.PageableLogTable;
import nl.procura.gbaws.web.vaadin.module.requests.page1.periodes.Anders;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.pageable.PageableTableLayout;
import nl.procura.vaadin.component.table.pageable.PageableTableResult;
import nl.procura.vaadin.component.table.pageable.PageableTableSelection;

@SuppressWarnings("serial")
public class Page1Request extends DefaultPageLayout {

  private PageableTableLayout tableLayout = null;
  private PageableLogTable    table       = null;
  private Page1RequestForm    form        = null;

  public Page1Request() {

    super("Berichten");
    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonSearch);
      addButton(buttonDel);

      form = new Page1RequestForm() {

        @Override
        protected void zoek() {
          onSearch();
        }
      };

      addComponent(form);

      table = new Page1RequestTable();
      tableLayout = new PageableTableLayout<>(table);

      addExpandComponent(tableLayout);

      onSearch();
    }

    super.event(event);
  }

  @Override
  public void onSearch() {

    form.commit();

    long dFrom = form.getBean().getPeriode().getdFrom();
    long dTo = form.getBean().getPeriode().getdTo();
    UsrWrapper usr = form.getBean().getGebruiker();

    if (form.getBean().getPeriode() instanceof Anders) {
      dFrom = form.getBean().getVan().getLongValue();
      dTo = form.getBean().getTm().getLongValue();
    }

    int cUsr = usr != null ? usr.getPk() : -1;
    String keyword = form.getBean().getZoeken();

    List<Integer> requests = RequestDao.getRequests(0, dFrom, dTo, cUsr, keyword);
    tableLayout.setResult(new PageableTableResult(requests, 20));
    super.onSearch();
  }

  @Override
  public void onDelete() {

    new PageableTableSelection<Integer>(tableLayout, PageableTableSelection.Action.VERWIJDEREN) {

      @Override
      protected void execute(List<Integer> requests) {
        RequestDao.removeAndCommit(requests, Request.class);
      }

      @Override
      protected void afterExecution() {
        onSearch();
      }
    };

    super.onDelete();
  }
}
