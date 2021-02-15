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

package nl.procura.gba.web.modules.zaken.tmv.page7;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.modules.zaken.tmv.layouts.TmvTabPage;
import nl.procura.gba.web.modules.zaken.tmv.layouts.reacties.TmvReactieLayout;
import nl.procura.gba.web.modules.zaken.tmv.page8.Page8Tmv;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingReactie;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page7Tmv extends TmvTabPage {

  private Page7TmvTable table = null;

  public Page7Tmv(TerugmeldingAanvraag tmv) {

    super(tmv);
    setMargin(false);

    addButton(buttonNew);
    addButton(buttonDel);

  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      table = new Page7TmvTable(getTmv());
      addComponent(new Page7TmvForm1(getTmv()));
      addComponent(table);
    } else if (event.isEvent(AfterReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void onDelete() {

    new DeleteProcedure<TerugmeldingReactie>(table) {

      @Override
      public void afterDelete() {
        VaadinUtils.getParent(Page7Tmv.this, TmvReactieLayout.class).onReload();
      }

      @Override
      public void deleteValue(TerugmeldingReactie reactie) {

        getServices().getTerugmeldingService().deleteReactie(reactie);
        getServices().getTerugmeldingService().herlaad(getTmv());
      }
    };
  }

  @Override
  public void onEnter() {

    Record r = table.getSelectedRecord();

    if (r != null) {
      table.onDoubleClick(r);
    }

    super.onEnter();
  }

  @Override
  public void onNew() {

    getNavigation().goToPage(new Page8Tmv(getTmv()));

    super.onNew();
  }
}
