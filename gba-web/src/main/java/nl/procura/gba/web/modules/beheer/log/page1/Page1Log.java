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

package nl.procura.gba.web.modules.beheer.log.page1;

import static ch.lambdaj.Lambda.on;

import java.util.List;

import nl.procura.gba.web.common.misc.ZaakPeriode;
import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.log.page2.Page2Log;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.telling.Vandaag;
import nl.procura.gba.web.services.beheer.log.InLogpoging;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

import ch.lambdaj.Lambda;

public class Page1Log extends NormalPageTemplate {

  private Page1LogTable table     = null;
  private LogLayout     logLayout = null;

  public Page1Log() {
    super("Overzicht van inlogpogingen");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonDel);

      table = new Page1LogTable() {

        @Override
        public void onDoubleClick(Record record) {
          selectRecord(record);
        }

        @Override
        public void setRecords() {

          setMultiSelect(true);

          try {
            ZaakPeriode periode;

            if (logLayout == null) {
              periode = new Vandaag();
            } else {
              periode = (ZaakPeriode) logLayout.getPeriodeVeld().getValue();
            }

            List<InLogpoging> list = getServices().getLogService()
                .getLogs(periode.getdFrom(), periode.getdTo());
            int nr = list.size();
            for (InLogpoging log : list) {
              addLog(nr, log);
              nr--;
            }
          } catch (Exception e) {
            getApplication().handleException(getWindow(), e);
          }
        }
      };

      addExpandComponent(table);

      logLayout = new LogLayout(table) {

        @Override
        protected void onPeriodeWijziging(ZaakPeriode periode) {
          table.init();
        }

        @Override
        protected void onSysteemAccountWijziging(Boolean toon) {
          table.setToonSysteemAccounts(toon);
          table.init();
        }
      };

      getButtonLayout().addComponent(logLayout);
    }

    super.event(event);
  }

  @Override
  public void onDelete() {

    new DeleteProcedure<InLogpoging>(table, true) {

      @Override
      protected void deleteValues(List<InLogpoging> inlogpogingen) {

        if (!table.isSelectedRecords() && inlogpogingen.size() > 0) {

          long min = Lambda.min(inlogpogingen, on(InLogpoging.class).getCode());
          long max = Lambda.max(inlogpogingen, on(InLogpoging.class).getCode());

          getServices().getLogService().delete(min, max);
        } else {

          getServices().getLogService().delete(inlogpogingen);
        }
      }
    };

    super.onDelete();
  }

  private void selectRecord(Record record) {

    getNavigation().goToPage(new Page2Log((InLogpoging) record.getObject()));
  }
}
