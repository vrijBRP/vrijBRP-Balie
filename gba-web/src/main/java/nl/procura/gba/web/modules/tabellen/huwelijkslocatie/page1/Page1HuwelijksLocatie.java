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

package nl.procura.gba.web.modules.tabellen.huwelijkslocatie.page1;

import java.util.List;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.fields.GeldigheidField;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.tabellen.huwelijkslocatie.page2.Page2HuwelijksLocatie;
import nl.procura.gba.web.modules.tabellen.huwelijkslocatie.page3.Page3HuwelijkLocaties;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatie;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

import ch.lambdaj.Lambda;

public class Page1HuwelijksLocatie extends NormalPageTemplate {

  private final Button    buttonOpties = new Button("Locatie opties (F2)");
  private Table1          table1       = null;
  private GeldigheidField geldigheidField;

  public Page1HuwelijksLocatie() {

    super("Overzicht van huwelijkslocaties");

    addButton(buttonNew);
    addButton(buttonDel);
    addButton(buttonOpties);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      geldigheidField = new GeldigheidField() {

        @Override
        public void onChangeValue(GeldigheidStatus value) {
          table1.init();
        }
      };

      table1 = new Table1();
      addExpandComponent(table1);

      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table1));
      getButtonLayout().addComponent(geldigheidField);
      getButtonLayout().align(geldigheidField, Alignment.MIDDLE_RIGHT);
    } else if (event.isEvent(AfterReturn.class)) {

      table1.init();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonOpties) {
      onNextPage();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onDelete() {

    new DeleteProcedure<HuwelijksLocatie>(table1) {

      @Override
      public void deleteValue(HuwelijksLocatie locatie) {

        getServices().getHuwelijkService().delete(locatie);
      }
    };
  }

  @Override
  public void onNew() {

    getNavigation().goToPage(new Page2HuwelijksLocatie(new HuwelijksLocatie()));

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

      getNavigation().goToPage(new Page2HuwelijksLocatie((HuwelijksLocatie) record.getObject()));

      super.onDoubleClick(record);
    }

    @Override
    public void setColumns() {

      setSelectable(true);
      setMultiSelect(true);
      addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

      addColumn("Naam", 400).setUseHTML(true);
      addColumn("Soort", 100);
      addColumn("Toelichting");
      addColumn("Opties", 400);
    }

    @Override
    public void setRecords() {

      List<HuwelijksLocatie> locaties = getServices().getHuwelijkService().getHuwelijksLocaties(
          geldigheidField.getValue());
      for (HuwelijksLocatie l : locaties) {

        Record r = addRecord(l);

        r.addValue(GeldigheidStatus.getHtml(l.getHuwelijksLocatie(), l));
        r.addValue(l.getLocatieSoort());
        r.addValue(l.getToelichting());
        r.addValue(l.getOpties().isEmpty() ? "Geen opties"
            : Lambda.joinFrom(
                l.getOpties()).getHuwelijksLocatieOptie());
      }

      super.setRecords();
    }
  }
}
