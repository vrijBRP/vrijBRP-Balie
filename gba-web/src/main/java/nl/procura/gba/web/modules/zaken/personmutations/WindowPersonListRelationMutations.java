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

package nl.procura.gba.web.modules.zaken.personmutations;

import java.util.List;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.zaken.ZakenModuleTemplate;
import nl.procura.gba.web.modules.zaken.personmutations.page6.Page6PersonListMutations;
import nl.procura.gba.web.modules.zaken.personmutations.relatedcategories.PersonListRelationMutation;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class WindowPersonListRelationMutations extends GbaModalWindow {

  private List<PersonListRelationMutation> mutations;

  public WindowPersonListRelationMutations(List<PersonListRelationMutation> mutations) {
    super("Gemuteerde categorieën van gerelateerden (Escape om te sluiten)", "1000px");
    this.mutations = mutations;
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new ModulePersonListMutations()));
  }

  public class ModulePersonListMutations extends ZakenModuleTemplate {

    @Override
    public void event(PageEvent event) {
      super.event(event);
      if (event.isEvent(InitPage.class)) {
        getPages().getNavigation().goToPage(new Page6PersonListMutations(mutations));
      }
    }
  }
}
