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

package nl.procura.gba.web.modules.tabellen.huwelijkslocatie.page3;

import java.util.List;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.tabellen.huwelijkslocatie.page4.Page4HuwelijksLocatie;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatieOptie.HuwelijksLocatieOptie;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page3HuwelijkLocaties extends NormalPageTemplate {

  private Table1 table1 = null;

  public Page3HuwelijkLocaties() {

    super("Overzicht van huwelijkslocatie opties");

    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonDel);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      table1 = new Table1();

      addExpandComponent(table1);
    } else if (event.isEvent(AfterReturn.class)) {

      table1.init();
    }

    super.event(event);
  }

  @Override
  public void onDelete() {

    new DeleteProcedure<HuwelijksLocatieOptie>(table1) {

      @Override
      public void deleteValue(HuwelijksLocatieOptie optie) {
        getServices().getHuwelijkService().delete(optie);
      }
    };
  }

  @Override
  public void onNew() {

    getNavigation().goToPage(new Page4HuwelijksLocatie(new HuwelijksLocatieOptie()));

    super.onNew();
  }

  @Override
  public void onPreviousPage() {

    getNavigation().goBackToPreviousPage();

    super.onPreviousPage();
  }

  public class Table1 extends GbaTable {

    @Override
    public void onDoubleClick(Record record) {

      getNavigation().goToPage(new Page4HuwelijksLocatie((HuwelijksLocatieOptie) record.getObject()));

      super.onDoubleClick(record);
    }

    @Override
    public void setColumns() {

      setSelectable(true);
      setMultiSelect(true);

      addColumn("Code", 30);
      addColumn("Volgnr.", 50);
      addColumn("Naam");
      addColumn("Type", 100);
      addColumn("Verplicht", 100);
    }

    @Override
    public void setRecords() {

      List<HuwelijksLocatieOptie> opties = getServices().getHuwelijkService().getHuwelijksLocatieOpties();

      for (HuwelijksLocatieOptie o : opties) {

        Record r = addRecord(o);

        r.addValue(o.getCodeHuwelijksLocatieOptie());
        r.addValue(o.getVnr());
        r.addValue(o.getHuwelijksLocatieOptieOms());
        r.addValue(o.getOptieType());
        r.addValue(o.isVerplichteOptie() ? "Ja" : "Nee");
      }

      super.setRecords();
    }
  }
}
