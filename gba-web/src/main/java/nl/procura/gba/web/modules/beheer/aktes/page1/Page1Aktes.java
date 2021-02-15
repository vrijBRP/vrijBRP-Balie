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

package nl.procura.gba.web.modules.beheer.aktes.page1;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.aktes.page2.Page2Aktes;
import nl.procura.gba.web.modules.tabellen.huwelijkslocatie.page3.Page3HuwelijkLocaties;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteCategorie;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1Aktes extends NormalPageTemplate {

  private Table1 table1 = null;

  public Page1Aktes() {

    super("Overzicht van akte-categorieën");

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

    new DeleteProcedure<DossierAkteCategorie>(table1) {

      @Override
      public void deleteValue(DossierAkteCategorie categorie) {

        getServices().getAkteService().deleteRegisterCategorie(categorie);
      }
    };
  }

  @Override
  public void onNew() {

    getNavigation().goToPage(new Page2Aktes(new DossierAkteCategorie()));

    super.onNew();
  }

  @Override
  public void onNextPage() {

    getNavigation().goToPage(Page3HuwelijkLocaties.class);

    super.onNextPage();
  }

  public class Table1 extends GbaTable {

    @Override
    public void onDoubleClick(Record record) {

      getNavigation().goToPage(new Page2Aktes((DossierAkteCategorie) record.getObject()));

      super.onDoubleClick(record);
    }

    @Override
    public void setColumns() {

      setSelectable(true);
      setMultiSelect(true);

      addColumn("Code", 30);
      addColumn("Naam");
    }

    @Override
    public void setRecords() {

      for (DossierAkteCategorie l : getServices().getAkteService().getAkteRegisterCategorieen()) {

        Record r = addRecord(l);

        r.addValue(l.getCode());
        r.addValue(l.getCategorie());
      }

      super.setRecords();
    }
  }
}
