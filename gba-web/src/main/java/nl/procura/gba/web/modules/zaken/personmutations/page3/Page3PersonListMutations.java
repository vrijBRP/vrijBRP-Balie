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

package nl.procura.gba.web.modules.zaken.personmutations.page3;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.*;
import static nl.procura.gba.web.modules.zaken.personmutations.overview.PersonMutationOverviewBean.*;

import nl.procura.burgerzaken.gba.core.enums.GBAGroup;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.QuickSearchPersonWindow;
import nl.procura.gba.web.modules.zaken.personmutations.overview.PersonMutationOverviewForm;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElems;
import nl.procura.gba.web.modules.zaken.personmutations.page4.Page4PersonListMutations;
import nl.procura.gba.web.services.beheer.personmutations.PersonListMutation;
import nl.procura.vaadin.component.layout.Fieldset;

public class Page3PersonListMutations extends NormalPageTemplate {

  private final Page3PersonListMutationsLayout layout;
  private final PersonListMutation             mutation;
  private final PersonListMutElems             elements;

  public Page3PersonListMutations(PersonListMutElems elements, PersonListMutation mutation) {
    super("Nieuwe mutatie toevoegen");
    this.mutation = mutation;
    this.elements = elements;
    setHeight("800px");

    addButton(buttonPrev);
    addButton(buttonNext, 1f);

    if (isRelativeMutation(mutation)) {
      addButton(buttonNext);
      addButton(buttonSearch, 1f);
      buttonSearch.setCaption("Zoek persoon");
    } else {
      addButton(buttonNext, 1f);
    }

    addButton(buttonClose);

    addComponent(new Fieldset("Gegevens"));
    addComponent(new PersonMutationOverviewForm(mutation, CAT, RECORD, SET, OPERATION));

    layout = new Page3PersonListMutationsLayout(elements, mutation);
    addComponent(layout);
    setExpandRatio(layout, 1.0F);
  }

  @Override
  protected void initPage() {
    if (isRelativeMutation(mutation) && elements.isAllBlank(GBAGroup.IDNUMMERS)) {
      onSearch();
    }
    super.initPage();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onNextPage() {
    getNavigation().goToPage(new Page4PersonListMutations(mutation, layout.getNewRecords()));
    super.onNextPage();
  }

  @Override
  public void onSearch() {
    getParentWindow().addWindow(new QuickSearchPersonWindow(layout::updatePl));
    super.onSearch();
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  private boolean isRelativeMutation(PersonListMutation mutation) {
    return mutation.getCatType().is(OUDER_1, OUDER_2, HUW_GPS, KINDEREN);
  }
}
