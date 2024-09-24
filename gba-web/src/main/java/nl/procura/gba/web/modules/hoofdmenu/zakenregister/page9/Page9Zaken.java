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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page9;

import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterPage;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controle;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controles;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTableFilterLayout;

public class Page9Zaken extends ZakenregisterPage<Zaak> {

  private Table1 table = null;

  public Page9Zaken() {
    super(null, "Zakenregister: controles");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonNext);
      buttonNext.setCaption("Voer controles uit (F2)");

      setInfo("Voert controles uit op diensten van de applicatie.");

      table = new Table1();
      getButtonLayout().addComponent(new IndexedTableFilterLayout(table));
      addExpandComponent(table);
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {

    table.check();

    super.onNextPage();
  }

  class Table1 extends Page9ZakenTable {

    private final Controles controles = new Controles();

    public void check() {

      controles.clear();

      ServiceControlesTask task = new ServiceControlesTask(getApplication(), controles) {

        @Override
        public void onFinished() {
          recheck();
        }
      };

      getWindow().addWindow(new ServiceControlesWindow(task));
    }

    public void recheck() {
      init();
      if (controles.isEmpty()) {
        infoMessage("De controles hebben niet geleid tot wijzigingen aan zaken.");
      }
    }

    @Override
    public void setRecords() {

      int nr = 0;
      for (Controle controle : controles) {

        nr++;
        Record r = addRecord(controle);
        r.addValue(nr);
        r.addValue(controle.isGewijzigd() ? "Ja" : "Nee");
        r.addValue(controle.getId());
        r.addValue(controle.getOnderwerp());
        r.addValue(controle.getOmschrijving());
        r.addValue(controle.getOpmerkingenString());
      }

      super.setRecords();
    }
  }
}
