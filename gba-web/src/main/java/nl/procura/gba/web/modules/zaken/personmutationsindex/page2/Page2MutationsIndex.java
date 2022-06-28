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

package nl.procura.gba.web.modules.zaken.personmutationsindex.page2;

import static nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource.STANDAARD;

import nl.procura.bsm.rest.v1_0.objecten.gba.probev.mutations.MutationRestElement;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.zaken.personmutationsindex.page1.Page1MutationsIndex;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2MutationsIndex extends NormalPageTemplate {

  private final MutationRestElement mutation;

  public Page2MutationsIndex(MutationRestElement mutation) {
    this.mutation = mutation;
    setSpacing(true);
    addButton(buttonPrev);
    addButton(buttonNext);
    buttonNext.setCaption("Naar persoonslijst (F2)");
    addComponent(new Page2MutationsIndexForm(mutation));
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      getWindow().setWidth("600px");
      getWindow().center();
    }
    super.event(event);
  }

  @Override
  public void onNextPage() {
    getApplication().goToPl(getParentWindow(), "", STANDAARD, mutation.getAnr().getWaarde());
    getApplication().closeAllModalWindows(getApplication().getWindows());
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPage(Page1MutationsIndex.class);
  }
}
