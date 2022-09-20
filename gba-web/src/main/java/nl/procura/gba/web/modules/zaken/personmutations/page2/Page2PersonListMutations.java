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

package nl.procura.gba.web.modules.zaken.personmutations.page2;

import static com.vaadin.event.ShortcutAction.KeyCode.F4;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.NO_ACTION_INCORRECT_HIST;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.NO_RESULTS;

import com.vaadin.ui.Button;
import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.zaken.personmutations.page2.containers.ContainerItem;
import nl.procura.gba.web.modules.zaken.personmutations.page3.Page3PersonListMutations;
import nl.procura.gba.web.modules.zaken.personmutations.page4.Page4PersonListMutations;
import nl.procura.gba.web.modules.zaken.personmutations.page7.Page7PersonListMutations;
import nl.procura.gba.web.services.beheer.personmutations.PersonListActionType;
import nl.procura.gba.web.services.beheer.personmutations.PersonListMutation;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.standard.exceptions.ProException;

public class Page2PersonListMutations extends NormalPageTemplate {

  private Page2PersonListMutationsForm   form;
  private Page2PersonListMutationsLayout layout;
  private Page2BCMCheckResultLayout      bcmCheckResultLayout;
  private final Button                   buttonBCMCheck = new Button("BCM Controle (F4)");

  public Page2PersonListMutations() {
    super("Nieuwe mutatie toevoegen");
    setHeight("800px");
  }

  @Override
  protected void initPage() {
    addButton(buttonPrev);
    addButton(buttonNext);
    addButton(buttonPrint);
    addButton(buttonBCMCheck, 1f);
    addButton(buttonClose);

    layout = new Page2PersonListMutationsLayout();
    form = new Page2PersonListMutationsForm(getNewPL(), layout);
    bcmCheckResultLayout = new Page2BCMCheckResultLayout();

    addComponent(bcmCheckResultLayout);
    bcmCheckResultLayout.setVisible(false);
    addComponent(form);
    addComponent(layout);
    setExpandRatio(layout, 1.0F);

    super.initPage();
  }

  /**
   * Gets of new instanceof of PL
   */
  private BasePLExt getNewPL() {
    BasePLExt pl = getPL();
    clearCommitmentValues(pl);
    return pl;
  }

  private BasePLExt getPL() {
    PersonenWsService personenWsService = getApplication().getServices().getPersonenWsService();
    personenWsService.getOpslag().clear();
    return personenWsService.getPersoonslijst(personenWsService.getHuidige());
  }

  /**
   * Bij het samenstelling van de PL worden de sluiting
   * vanuit praktische redenen toegevoegd aan de ontbindsgegevens, maar die groepen
   * mogen niet samen voorkomen.
   */
  private void clearCommitmentValues(BasePLExt pl) {
    for (BasePLSet set : pl.getCat(GBACat.HUW_GPS).getSets()) {
      for (BasePLRec rec : set.getRecs()) {
        if (!rec.getElem(GBAElem.REDEN_ONTBINDING).isEmpty()) {
          rec.getElem(GBAElem.DATUM_VERBINTENIS).setValue(new BasePLValue(""));
          rec.getElem(GBAElem.PLAATS_VERBINTENIS).setValue(new BasePLValue(""));
          rec.getElem(GBAElem.LAND_VERBINTENIS).setValue(new BasePLValue(""));
        }
      }
    }
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onNextPage() {

    form.commit();

    ContainerItem<GBACat> category = form.getCatValue();
    ContainerItem<PersonListActionType> action = form.getActValue();
    ContainerItem<BasePLSet> set = form.getSetValue();
    ContainerItem<BasePLRec> record = form.getRecValue();

    if (category == null) {
      throw new ProException(INFO, "Geen categorie geselecteerd");

    } else if (action == null) {
      throw new ProException(INFO, "Geen actie geselecteerd");

    } else if (set == null) {
      throw new ProException(INFO, "Geen gegevensset geselecteerd");

    } else if (record == null) {
      throw new ProException(INFO, "Geen record geselecteerd");

    } else if (NO_ACTION_INCORRECT_HIST == action.getItem()) {
      throw new ProException(INFO, action.getItem().getDescription());
    }

    if (!action.getItem().isSuperuser() && getPl().getCat(GBACat.INSCHR)
        .getLatestRec()
        .getElemVal(GBAElem.OMSCHR_REDEN_OPSCH_BIJHOUD)
        .isNotBlank()) {
      throw new ProException(WARNING, "Deze actie is niet mogelijk omdat de persoonslijst is opgeschort");
    }

    // Create a new mutation record
    PersonListMutation mutation = getServices().getPersonListMutationService().getNewZaak();
    mutation.setAction(action.getItem());
    mutation.setCat(category.getItem());
    mutation.setSet(set.getItem());
    mutation.setDescrSet(set.toString());
    mutation.setDescrRec(record.toString());

    PersonListMutElems elements = layout.getTable().getElementRecords();
    if (elements.isEmpty()) {
      throw new ProException(NO_RESULTS, WARNING, "Er zijn geen BRP elementen geselecteerd");
    }

    if (action.getItem().is(PersonListActionType.DELETE_MUT)) {
      getNavigation().goToPage(new Page4PersonListMutations(mutation, new PersonListMutElems()));

    } else {
      getNavigation().goToPage(new Page3PersonListMutations(elements, mutation));
    }

    super.onNextPage();
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (isKeyCode(button, keyCode, F4, buttonBCMCheck)) {
      onBCMCheck();
    } else {
      super.handleEvent(button, keyCode);
    }
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onPrint() {
    getNavigation().goToPage(new Page7PersonListMutations(toPrintData()));
  }

  public void onBCMCheck() {
    Page2BCMCheckResultLayout newBcmCheckResultLayout = new BCMCheckResultLayoutBuilder(
        getPL().getPersoon().getAnr().getDescr(),
        getApplication()).getBcmCheckResultLayout();
    replaceComponent(bcmCheckResultLayout, newBcmCheckResultLayout);
    bcmCheckResultLayout = newBcmCheckResultLayout;
  }

  private PersonListPrintData toPrintData() {
    form.commit();
    BasePLSet set = form.getSetValue().getItem();
    return new PersonListPrintData(getPL(), set,
        bcmCheckResultLayout.getHeader(),
        bcmCheckResultLayout.getTable().getElementRecords());
  }

  public Page2PersonListMutationsLayout getLayout() {
    return layout;
  }
}
