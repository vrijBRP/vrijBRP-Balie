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

package nl.procura.gba.web.modules.bs.registration.page40;

import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.gba.jpa.personen.db.DossPersRelation;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.modules.bs.registration.AbstractRegistrationPage;
import nl.procura.gba.web.modules.bs.registration.page40.relations.ExistingRelationWindow;
import nl.procura.gba.web.modules.bs.registration.page40.relations.NewRelationWindow;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.RelationService;
import nl.procura.gba.web.services.bs.registration.RelationType;
import nl.procura.gba.web.services.bs.registration.relations.Relation;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.Fieldset;

/**
 * First registration - relations
 */
public class Page40Registration extends AbstractRegistrationPage {

  private final Page40RegistrationLayout1 relLayout1;
  private final Page40RegistrationLayout2 relLayout2;
  private RelationService                 relationService;
  private final Button                    buttonAddRelation = new Button("Toevoegen", e -> onAddRelationClick());
  private final Button                    buttonDelRelation = new Button("Verwijderen", e -> onDeleteRelation());

  public Page40Registration() {
    super("Eerste inschrijving - relaties");
    relLayout1 = new Page40RegistrationLayout1(this::onEditRelation);
    relLayout2 = new Page40RegistrationLayout2();
  }

  @Override
  public void initPage() {
    super.initPage();
    addButton(buttonPrev);
    addButton(buttonNext);
    setInfo();

    OptieLayout optieLayout = new OptieLayout();
    optieLayout.getRight().addButton(buttonAddRelation, this);
    optieLayout.getRight().addButton(buttonDelRelation, this);
    optieLayout.getRight().setCaption("Opties");
    optieLayout.getRight().setWidth("170px");

    GbaTabsheet tabsheet = new GbaTabsheet();
    tabsheet.setExtraTopMargin();
    tabsheet.addStyleName("zoektab");
    tabsheet.addTab(relLayout1, "Lijst");
    tabsheet.addTab(relLayout2, "Matrix");
    optieLayout.getLeft().addComponent(new Fieldset("Relaties"));
    optieLayout.getLeft().addComponent(tabsheet);

    addExpandComponent(optieLayout);

    // in this method as dossier and services aren't available in the constructor
    relationService = getServices().getRelationService();
    updateTable();
  }

  private void setInfo() {
    setInfo("Ter informatie",
        "Voeg toe aan wie een in te schrijven persoon is gerelateerd en vul per scherm de gegevens aan. "
            + "Kies daarbij uit andere andere in te schrijven personen of gerelateerden (niet) in de BRP. "
            + "<br/>Als per in te schrijven persoon niet 2 keer een relatie \"is kind van\" is opgenomen, "
            + "dan worden bij de verwerking van de zaak bij die ontbrekende oudergegevens de standaardwaarden opgenomen.");
  }

  private void onEditRelation(Relation relation) {
    getApplication().getParentWindow().addWindow(new ExistingRelationWindow(relation, this::saveRelation));
  }

  private void onDeleteRelation() {
    List<DossPersRelation> relations = relLayout1.getTable().getSelectedRelations();
    StringBuilder msg = new StringBuilder();
    msg.append("Wilt u deze relatie verwijderen?");
    int width = 300;
    if (!relations.isEmpty() && RelationType.valueOfCode(relations.get(0).getRelationShipType()).isRelated()) {
      msg.append("<hr/><div><i>De gerelateerde relatie wordt ook verwijderd.</i></div>");
      width = 350;
    }
    getParentWindow().addWindow(new ConfirmDialog(msg.toString(), width) {

      @Override
      public void buttonYes() {
        relationService.remove(relLayout1.getTable().getSelectedRelations());
        updateTable();
        super.buttonYes();
      }
    });
  }

  private void onAddRelationClick() {
    getApplication().getParentWindow().addWindow(new NewRelationWindow(getDossier(), this::saveRelation));
  }

  private void saveRelation(Relation relation) {
    relationService.save(relation.getRelation());
    updateTable();
  }

  private void updateTable() {
    List<DossierPersoon> people = getDossier().getPersonen();
    List<Relation> relations = Relation.fromDossPersRelations(relationService.findByPeople(people), people);
    relLayout1.update(relations);
    relLayout2.update(people, relations);

    getProcessen().updateStatus();
    setInfo();
  }

  @Override
  public boolean checkPage() {
    if (super.checkPage()) {
      getServices().getZaakIdentificatieService().getDmsZaakId(getDossier());
      getServices().getRegistrationService().saveRegistration(getZaakDossier());
      return true;
    }
    return false;
  }
}
