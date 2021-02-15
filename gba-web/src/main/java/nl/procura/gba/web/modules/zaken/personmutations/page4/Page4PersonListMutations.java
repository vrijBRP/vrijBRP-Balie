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

package nl.procura.gba.web.modules.zaken.personmutations.page4;

import static nl.procura.gba.web.modules.zaken.personmutations.PersonListMutationsTravelDocUtils.getInfoLayout;
import static nl.procura.gba.web.modules.zaken.personmutations.overview.PersonMutationOverviewBean.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.commons.core.utils.ProStringUtils;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.jpa.personen.db.PlMutRec;
import nl.procura.gba.jpa.personen.db.PlMutRecPK;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.verwerken.BsmVerwerkingWindow;
import nl.procura.gba.web.modules.zaken.personmutations.overview.PersonMutationOverviewForm;
import nl.procura.gba.web.modules.zaken.personmutations.page1.Page1PersonListMutations;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElem;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElems;
import nl.procura.gba.web.modules.zaken.personmutations.relatedcategories.PersonListRelationMutation;
import nl.procura.gba.web.modules.zaken.personmutations.relatedcategories.PersonListRelationMutationHandler;
import nl.procura.gba.web.services.beheer.personmutations.PersonListMutation;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.Fieldset;

public class Page4PersonListMutations extends NormalPageTemplate {

  private final PersonListMutElems mutations;
  private final PersonListMutation mutation;

  private Page4PersonListMutationsLayout layout;

  public Page4PersonListMutations(PersonListMutation mutation, PersonListMutElems mutations) {
    super("Nieuwe mutatie toevoegen");
    this.mutations = mutations;
    this.mutation = mutation;
    setHeight("800px");
  }

  @Override
  protected void initPage() {

    addButton(buttonPrev);

    ZaakStatusType initieleStatus = getServices().getZaakStatusService().getInitieleStatus(mutation);
    System.out.println(initieleStatus);
    if (ZaakStatusType.OPGENOMEN == initieleStatus) {
      buttonNext.setCaption("Opslaan en verwerken (F2)");
      buttonSave.setCaption("Alleen opslaan (F9)");
      addButton(buttonNext);
    } else {
      buttonSave.setCaption("Opslaan (F9)");
    }

    addButton(buttonSave, 1f);
    addButton(buttonClose);

    addComponent(new Fieldset("Gegevens"));
    getInfoLayout(mutations).ifPresent(this::addComponent);
    addComponent(new PersonMutationOverviewForm(mutation, CAT, RECORD, SET, OPERATION));

    PersonenWsService personenWsService = getServices().getPersonenWsService();
    List<PersonListRelationMutation> list = new ArrayList<>();
    if (mutation.getCatType().is(GBACat.PERSOON)) {
      String anr = personenWsService.getHuidige().getPersoon().getAnr().getVal();
      list.addAll(PersonListRelationMutationHandler.getNew(anr, personenWsService::getPersoonslijst));
    }

    addComponent(new Fieldset("Wijzigingen in gerelateerde persoonslijsten",
        new Page4PersonRelationMutationsTable(list)));

    layout = new Page4PersonListMutationsLayout(mutations);
    addComponent(layout);
    setExpandRatio(layout, 1.0F);

    super.initPage();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onNextPage() {
    saveMutation();
    getParentWindow().addWindow(new ConfirmDialog("Wilt u direct de mutatie verwerken?") {

      @Override
      public void buttonYes() {
        super.buttonYes();
        processMutation();
        returnToList();
      }

      @Override
      public void buttonNo() {
        super.buttonNo();
        returnToList();
      }
    });
  }

  @Override
  public void onSave() {
    saveMutation();
    returnToList();
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  private void processMutation() {
    getApplication().getParentWindow().addWindow(new BsmVerwerkingWindow<PersonListMutation>(mutation) {

      @Override
      public void reload() {
        getGbaApplication().reloadCurrentPersonList();
        getNavigation().getPage(Page1PersonListMutations.class).reload();
      }
    });
  }

  private void saveMutation() {
    layout.getForm().commit();
    String explanation = layout.getForm().getBean().getExplanation();
    mutation.setExplanation(ProStringUtils.toString(explanation));

    // Add elements
    List<PlMutRec> mutationRecords = new ArrayList<>();
    for (PersonListMutElem record : mutations) {
      PlMutRec mutationRecord = new PlMutRec();
      mutationRecord.setId(new PlMutRecPK(record.getElemType().getCode()));
      mutationRecord.setValOrg(record.getCurrentValue().getVal());
      mutationRecord.setValOrgDescr(record.getCurrentValue().getDescr());
      mutationRecord.setValNew(record.getNewValue());
      mutationRecord.setValNewDescr(record.getNewDescription());
      mutationRecord.setChanged(BigDecimal.valueOf(record.isChanged() ? 1 : 0));
      mutationRecords.add(mutationRecord);
    }

    // Save the mutation
    getServices().getPersonListMutationService().save(mutation, mutationRecords);
  }

  private void returnToList() {
    getNavigation().removeOtherPages();
    getNavigation().goToPage(Page1PersonListMutations.class);
  }
}
