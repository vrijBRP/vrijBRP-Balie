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

import static com.vaadin.event.ShortcutAction.KeyCode.F6;
import static com.vaadin.event.ShortcutAction.KeyCode.F8;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.HUW_GPS;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.KINDEREN;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_1;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_2;
import static nl.procura.gba.web.modules.zaken.personmutations.overview.PersonMutationOverviewBean.CAT;
import static nl.procura.gba.web.modules.zaken.personmutations.overview.PersonMutationOverviewBean.OPERATION;
import static nl.procura.gba.web.modules.zaken.personmutations.overview.PersonMutationOverviewBean.RECORD;
import static nl.procura.gba.web.modules.zaken.personmutations.overview.PersonMutationOverviewBean.SET;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.ADD_SET;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAGroup;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLList;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.jpa.personen.db.PlMutRec;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.QuickSearchPersonWindow;
import nl.procura.gba.web.modules.zaken.personmutations.overview.PersonMutationOverviewForm;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElem;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElemSorter;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElems;
import nl.procura.gba.web.modules.zaken.personmutations.page4.Page4PersonListMutations;
import nl.procura.gba.web.services.beheer.personmutations.PersonListMutation;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page3PersonListMutations extends NormalPageTemplate {

  private Page3PersonListMutationsLayout layout;
  private final PersonListMutation       mutation;
  private final PersonListMutElems       elems;

  public Page3PersonListMutations(PersonListMutation mutation) {
    super("Nieuwe mutatie toevoegen");
    this.mutation = mutation;
    this.elems = getElems(mutation, mutation.getBasisPersoon(), getBasePLSet(mutation));
    create();
  }

  public Page3PersonListMutations(PersonListMutElems elems, PersonListMutation mutation) {
    super("Nieuwe mutatie toevoegen");
    this.mutation = mutation;
    this.elems = elems;
    create();
  }

  private void create() {
    setHeight("800px");
    addButton(buttonPrev);

    if (isRelativeMutation(mutation)) {
      addButton(buttonNext);
      addButton(buttonSearch);
      buttonSearch.setCaption("Zoek persoon");
    } else {
      addButton(buttonNext);
    }

    addButton(new PersonButton(), 1f);
    addButton(buttonClose);

    addComponent(new Fieldset("Gegevens"));
    addComponent(new PersonMutationOverviewForm(mutation, CAT, RECORD, SET, OPERATION));

    layout = new Page3PersonListMutationsLayout(elems, mutation);
    addExpandComponent(layout);
  }

  private PersonListMutElems getElems(PersonListMutation mutation, BasePLExt pl, BasePLSet basePLSet) {
    PersonListMutElems elems = new PersonListMutElems();
    BasePLRec record = basePLSet.getCurrentRec();
    for (BasePLElem elem : record.getElems()) {
      for (PlMutRec mutationRecord : mutation.getPlMutRecs()) {
        if (elem.getElemCode() == mutationRecord.getId().getElem()) {
          PersonListMutElem mutElem = new PersonListMutElem(pl, record, elem, mutation.getActionType());
          mutElem.setDefaultValue(() -> new FieldValue(mutationRecord.getValNew(), mutationRecord.getValNewDescr()));
          elems.add(mutElem);
        }
      }
    }
    elems.sort(new PersonListMutElemSorter());
    return elems;
  }

  private BasePLSet getBasePLSet(PersonListMutation mutation) {
    BasePLList<BasePLSet> sets = mutation.getBasisPersoon().getCat(mutation.getCatType()).getSets();
    if (sets.isEmpty() || mutation.getActionType().is(ADD_SET)) {
      return getEmptySet(mutation.getCatType(), mutation.getSet().intValue());
    }
    return sets
        .stream()
        .filter(set -> set.getIntIndex() == mutation.getSet().intValue())
        .findFirst()
        .orElseThrow(() -> new ProException("Geen set met code " + mutation.getSet()));
  }

  private BasePLSet getEmptySet(GBACat category, int setCode) {
    BasePLSet set = new BasePLSet(category, setCode);
    BasePLRec record = new BasePLRec(category, set, GBARecStatus.CURRENT);
    record.setIndex(1);
    for (GBAGroupElements.GBAGroupElem type : GBAGroupElements.getByCat(category.getCode())) {
      BasePLElem gbaElement = new BasePLElem(type.getCat().getCode(), type.getElem().getCode(), "");
      record.getElems().add(gbaElement);
    }
    set.getRecs().add(record);
    return set;
  }

  @Override
  protected void initPage() {
    if (isRelativeMutation(mutation) && elems.isAllBlank(GBAGroup.IDNUMMERS)) {
      onSearch();
    }
    setHeight(getWindow().getBrowserWindowHeight() - 50, UNITS_PIXELS);
    super.initPage();
  }

  @Override
  public void event(PageEvent event) {
    super.event(event);
    if (!elems.isEmpty()) { // Focus on first field
      elems.get(0).getField().focus();
    }
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
  public void handleEvent(Button button, int keyCode) {
    if (isKeyCode(button, keyCode, F6)) {
      if (elems.getFocusedElement() != null) {
        Component tableComponent = elems.getFocusedElement().getTableComponent();
        if (tableComponent instanceof CustomValueLayout) {
          ((CustomValueLayout) tableComponent).setDefault();
        }
      }
    }
    if (isKeyCode(button, keyCode, F8)) {
      if (elems.getFocusedElement() != null) {
        Field field = elems.getFocusedElement().getField();
        field.setValue(null);
        field.focus();
      }
    }
    super.handleEvent(button, keyCode);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  private boolean isRelativeMutation(PersonListMutation mutation) {
    return mutation.getCatType().is(OUDER_1, OUDER_2, HUW_GPS, KINDEREN);
  }
}
