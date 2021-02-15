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

package nl.procura.gba.web.modules.beheer.profielen.page10.tab2.page1;

import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.profielen.page10.IndicatiesTab;
import nl.procura.gba.web.modules.beheer.profielen.page10.tab2.page2.Page2Indicaties;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningIndicatie;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page1Indicaties extends NormalPageTemplate {

  private Table1 table1 = null;

  public Page1Indicaties() {

    super("Overzicht van mogelijke indicaties");

    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonDel);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      table1 = new Table1();

      addExpandComponent(table1);

      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table1));
    } else if (event.isEvent(AfterReturn.class)) {

      table1.init();
    }

    super.event(event);
  }

  @Override
  public void onDelete() {

    new DeleteProcedure<PlAantekeningIndicatie>(table1) {

      @Override
      public void deleteValue(PlAantekeningIndicatie indicatie) {
        getServices().getAantekeningService().delete(indicatie);
      }

      @Override
      protected void beforeDelete(PlAantekeningIndicatie indicatie) {
        if (indicatie.isVrijeAantekening()) {
          throw new ProException(WARNING, "De vrije aantekening indicatie kan niet worden verwijderd");
        }
      }
    };
  }

  @Override
  public void onNew() {

    getNavigation().goToPage(new Page2Indicaties(new PlAantekeningIndicatie()));

    super.onNew();
  }

  @Override
  public void onPreviousPage() {
    VaadinUtils.getParent(this, IndicatiesTab.class).getNavigation().goBackToPreviousPage();
  }

  public class Table1 extends GbaTable {

    @Override
    public void onDoubleClick(Record record) {

      PlAantekeningIndicatie indicatie = (PlAantekeningIndicatie) record.getObject();

      if (indicatie.isVrijeAantekening()) {
        throw new ProException(WARNING, "De vrije aantekening indicatie kan niet worden gewijzigd");
      }

      getNavigation().goToPage(new Page2Indicaties(indicatie));

      super.onDoubleClick(record);
    }

    @Override
    public void setColumns() {

      setSelectable(true);
      setMultiSelect(true);

      addColumn("Omschrijving");
      addColumn("Indicatie", 150);
      addColumn("PROBEV code", 150);
    }

    @Override
    public void setRecords() {

      for (PlAantekeningIndicatie a : getServices().getAantekeningService().getAantekeningIndicaties()) {

        if (pos(a.getCAantekeningInd())) {

          Record r = addRecord(a);

          r.addValue(a.getOmschrijving());
          r.addValue(a.isAantekening() ? "N.v.t." : a.getIndicatie());
          r.addValue(a.isAantekening() ? "N.v.t." : a.getProbev());
        }
      }

      super.setRecords();
    }
  }
}
