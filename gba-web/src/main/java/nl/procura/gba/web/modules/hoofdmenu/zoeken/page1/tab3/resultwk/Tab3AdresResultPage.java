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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab3.resultwk;

import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.diensten.gba.wk.extensions.WKResultWrapper;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab3.Tab3Page;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab3.resultpl.Tab3PlResultPage;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.window.Message;

public class Tab3AdresResultPage extends Tab3Page {

  private GbaTable        table = null;
  private WKResultWrapper result;

  public Tab3AdresResultPage(Tab3Page parentPage, WKResultWrapper newResult) {

    super(parentPage, "Woningkaart: overzicht adressen");

    setResult(newResult);

    addButton(buttonPrev);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addForm();

      table = new GbaTable() {

        @Override
        public void onClick(Record record) {
          selectRow((BaseWKExt) record.getObject());
        }

        @Override
        public void setColumns() {

          setSelectable(true);
          setSelectFirst(true);

          addColumn("Nr.", 50);
          addColumn("G.v.b.", 80);
          addColumn("Woningsoort", 150);
          addColumn("Adres");

          super.setColumns();
        }

        @Override
        public void setRecords() {

          int i = 0;

          for (BaseWKExt wk : result.getBasisWkWrappers()) {

            i++;
            Record r = addRecord(wk);
            r.addValue(astr(i));
            r.addValue(wk.isGeschikvoorBewoning() ? "Ja" : "Nee");
            r.addValue(wk.getBasisWk().getWoning().getDescr());
            r.addValue(wk.getAdres());
          }
        }
      };

      addExpandComponent(table);

      table.focus();
    } else if (event.isEvent(AfterBackwardReturn.class)) {

      table.init();
    }

    super.event(event);
  }

  public WKResultWrapper getResult() {
    return result;
  }

  public void setResult(WKResultWrapper result) {
    this.result = result;
  }

  @Override
  public void onEnter() {

    if (table.getRecord() != null) {
      selectRow((BaseWKExt) table.getRecord().getObject());
    }
  }

  private void selectRow(BaseWKExt wk) {
    try {
      if (wk != null) {
        WKResultWrapper result = getApplication().getServices().getPersonenWsService().getAdres(wk);
        if (result.getBasisWkWrappers().size() > 0) {
          getNavigation().goToPage(new Tab3PlResultPage(this, result));
        } else {
          new Message(getWindow(), "Geen zoekresultaten", Message.TYPE_WARNING_MESSAGE);
        }
      }
    } catch (Exception e) {
      getApplication().handleException(getWindow(), e);
    }
  }
}
