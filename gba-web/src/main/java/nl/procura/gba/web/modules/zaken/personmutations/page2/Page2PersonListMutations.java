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
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.zaken.personmutations.page2.containers.ContainerItem;
import nl.procura.gba.web.modules.zaken.personmutations.page3.Page3PersonListMutations;
import nl.procura.gba.web.modules.zaken.personmutations.page4.Page4PersonListMutations;
import nl.procura.gba.web.modules.zaken.personmutations.page7.Page7PersonListMutations;
import nl.procura.gba.web.services.beheer.personmutations.PersonListActionType;
import nl.procura.gba.web.services.beheer.personmutations.PersonListMutation;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.theme.twee.ProcuraTheme.ICOON_24;

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

    addComponent(form);
    addExpandComponent(layout);
    setHeight(getWindow().getBrowserWindowHeight() - 50, UNITS_PIXELS);

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
    BasePLExt huidige = personenWsService.getHuidige();

    if (huidige.heeftVerwijzing()) {
      PLEArgs args = new PLEArgs();
      args.setShowArchives(true);
      args.setDatasource(PLEDatasource.PROCURA);
      args.addNummer(huidige.getPersoon().getNummer().getVal());

      PersonenWsService personenWs = getServices().getPersonenWsService();
      return personenWs.getPersoonslijsten(args, false).getBasisPLWrappers().get(0);

    } else {
      return personenWsService.getPersoonslijst(huidige);
    }
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
    }

    PersonListMutElems elems = layout.getTable().getElementRecords();
    if (elems.isEmpty()) {
      throw new ProException(NO_RESULTS, WARNING, "Er zijn geen BRP elementen geselecteerd");
    }

    if (!action.getItem().isSuperuser() && getPl().getCat(GBACat.INSCHR)
        .getLatestRec()
        .getElemVal(GBAElem.OMSCHR_REDEN_OPSCH_BIJHOUD)
        .isNotBlank()) {

      getApplication().getParentWindow()
          .addWindow(new ConfirmDialog("Deze persoonslijst is opgeschort",
              "Alleen wijzigingen met een datum geldigheid vóór de datum opschorting zijn toegestaan. Doorgaan?",
              "400px", ICOON_24.WARNING) {

            @Override
            public void buttonYes() {
              doNextPage(category, action, set, record, elems);
              super.buttonYes();
            }
          });

    } else {
      doNextPage(category, action, set, record, elems);
    }

    super.onNextPage();
  }

  private void doNextPage(ContainerItem<GBACat> category,
      ContainerItem<PersonListActionType> action,
      ContainerItem<BasePLSet> set,
      ContainerItem<BasePLRec> record,
      PersonListMutElems elems) {

    // Create a new mutation record
    PersonListMutation mutation = getServices().getPersonListMutationService().getNewZaak();
    mutation.setAction(action.getItem());
    mutation.setCat(category.getItem());
    mutation.setSet(set.getItem());
    mutation.setDescrSet(set.toString());
    mutation.setDescrRec(record.toString());

    if (action.getItem().isSkipElements()) {
      getNavigation().goToPage(new Page4PersonListMutations(mutation, elems));

    } else {
      getNavigation().goToPage(new Page3PersonListMutations(elems, mutation));
    }
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
    removeComponent(bcmCheckResultLayout);
    addComponent(newBcmCheckResultLayout, getComponentIndex(form));
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
