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

package nl.procura.gba.web.modules.zaken.personmutations.page1;

import static nl.procura.gba.web.modules.zaken.personmutations.relatedcategories.PersonListRelationMutationHandler.getExisting;
import static nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie.SELECT_PL_MUTATIONS_GOEDKEUREN;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.zaken.personmutations.WindowPersonListRelationMutations;
import nl.procura.gba.web.modules.zaken.personmutations.page2.Page2PersonListMutations;
import nl.procura.gba.web.modules.zaken.personmutations.page5.Page5PersonListMutations;
import nl.procura.gba.web.modules.zaken.personmutations.relatedcategories.PersonListRelationMutation;
import nl.procura.gba.web.modules.zaken.personmutationsindex.WindowMutationsIndex;
import nl.procura.gba.web.services.beheer.personmutations.PersonListMutation;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1PersonListMutations extends NormalPageTemplate {

  private final Button                     buttonMutations     = new Button("Zaken gerelateerden");
  private final Button                     buttonMutationIndex = new Button("Mutatie overzicht (F10)");
  private Page1PersonListMutationsTable    table               = null;
  private List<PersonListRelationMutation> mutations           = new ArrayList<>();

  public Page1PersonListMutations() {
    setSpacing(true);
    addButton(buttonNew);
    addButton(buttonDel);
    addButton(buttonMutations);
    addButton(buttonMutationIndex, 1f);
    addButton(buttonClose);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      table = new Page1PersonListMutationsTable() {

        @Override
        public void onDoubleClick(Record record) {
          PersonListMutation mutation = record.getObject(PersonListMutation.class);
          getNavigation().goToPage(new Page5PersonListMutations(mutation));
        }
      };

      addComponent(new InfoLayout("Hieronder staan <b>alleen</b> de mutaties die via dit scherm zijn ingevoerd."));
      addExpandComponent(table);

      // Search for mutations
      PersonenWsService service = getServices().getPersonenWsService();
      String anr = service.getHuidige().getPersoon().getAnr().getVal();
      mutations = getExisting(anr, service::getPersoonslijst);
      buttonMutations.setCaption("Zaken gerelateerden (" + mutations.size() + ")");
      buttonMutationIndex.setVisible(getApplication().isProfielActie(SELECT_PL_MUTATIONS_GOEDKEUREN));

    } else if (event.isEvent(AfterReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonMutations) {
      getParentWindow().addWindow(new WindowPersonListRelationMutations(mutations));
    }
    if (button == buttonMutationIndex) {
      BasePLExt pl = getServices().getPersonenWsService().getHuidige();
      getParentWindow().addWindow(new WindowMutationsIndex(pl));
    }
    super.handleEvent(button, keyCode);
  }

  @Override
  public void onMenu() {
    buttonMutationIndex.click();
  }

  public void reload() {
    table.init();
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onDelete() {
    new DeleteProcedure<PersonListMutation>(table, false) {

      @Override
      public void deleteValue(PersonListMutation mutation) {
        getServices().getPersonListMutationService().delete(mutation);
      }
    };
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(Page2PersonListMutations.class);
    super.onNew();
  }
}
