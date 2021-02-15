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

package nl.procura.gba.web.modules.zaken.personmutations.page6;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.persoonslijst.overzicht.ModuleOverzichtPersoonslijst;
import nl.procura.gba.web.modules.zaken.personmutations.relatedcategories.PersonListRelationMutation;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page6PersonListMutations extends NormalPageTemplate {

  private Page6PersonListMutationsTable    table = null;
  private List<PersonListRelationMutation> mutations;

  public Page6PersonListMutations(List<PersonListRelationMutation> mutations) {
    this.mutations = mutations;
    setSpacing(true);
    addButton(buttonClose);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      table = new Page6PersonListMutationsTable(mutations) {

        @Override
        public void onClick(Record record) {
          selectRecord(record.getObject(PersonListRelationMutation.class).getRelatedPL());
        }
      };
      addComponent(new InfoLayout("Hieronder staan de gemuteerde categorieën van de gerelateerden."));
      addExpandComponent(table);

    } else if (event.isEvent(AfterReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  private void selectRecord(BasePLExt pl) {
    GbaApplication app = getApplication();
    app.closeAllModalWindows(app.getParentWindow().getChildWindows());
    try {
      app.goToPl(app.getParentWindow(),
          ModuleOverzichtPersoonslijst.NAME,
          PLEDatasource.STANDAARD,
          pl.getPersoon().getNummer().getVal());
    } catch (Exception e) {
      e.printStackTrace();
      app.handleException(getWindow(), e);
    }
  }

  public void reload() {
    table.init();
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }
}
