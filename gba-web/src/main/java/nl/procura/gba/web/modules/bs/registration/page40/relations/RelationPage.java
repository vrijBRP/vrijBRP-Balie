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

package nl.procura.gba.web.modules.bs.registration.page40.relations;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.GERELATEERDE_BRP;
import static nl.procura.gba.web.services.bs.registration.RelationMatchType.INAPPLICABLE;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.util.List;
import java.util.function.Consumer;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.jpa.personen.db.DossPersRelation;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.bs.registration.page40.relations.matching.RelationsMatchInfo;
import nl.procura.gba.web.modules.bs.registration.page40.relations.matching.RelationsMatchLayout;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.RelationMatchType;
import nl.procura.gba.web.services.bs.registration.RelationService;
import nl.procura.gba.web.services.bs.registration.RelationType;
import nl.procura.gba.web.services.bs.registration.relations.Relation;
import nl.procura.commons.core.exceptions.ProException;

public class RelationPage extends NormalPageTemplate {

  private final RelationForm       relationForm;
  private final Consumer<Relation> nextPageListener;
  private RelationsMatchLayout     relativesLayout;

  public RelationPage(List<DossierPersoon> people, Consumer<Relation> nextPageListener) {
    super("Relatie toevoegen");
    this.nextPageListener = nextPageListener;
    buttonNext.addListener(this);
    buttonClose.addListener(this);
    getMainbuttons().add(buttonNext);
    getMainbuttons().add(buttonClose);
    setSpacing(true);
    relationForm = new RelationForm(people);
    relationForm.setRelatedPersonChangeListener(this::showRelatives);
    addComponent(relationForm);
  }

  @Override
  public void onNextPage() {
    super.onNextPage();
    relationForm.commit();
    if (relativesLayout != null) {
      relativesLayout.commit();
    }
    nextPageListener.accept(getEnteredRelation());
  }

  private Relation getEnteredRelation() {
    RelationBean relBean = relationForm.getBean();
    DossierPersoon person = (DossierPersoon) relBean.getPerson().getValue();
    RelationType relationType = (RelationType) relBean.getRelationshipType().getValue();

    DossierPersoon relatedPerson;
    if (relationType.isRelated()) {
      relatedPerson = (DossierPersoon) relBean.getRelatedPerson().getValue();
    } else {
      // If there is no relation, like with the 'no legal parent(s)' option, the
      // related person is filled with the registrant
      relatedPerson = person;
    }

    DossPersRelation dossPersRelation = new DossPersRelation(
        null,
        person,
        relationType.getCode(),
        relatedPerson,
        INAPPLICABLE.getCode());

    if (relativesLayout != null) {
      RelationsMatchInfo matchingInfo = relativesLayout.getMatchingInfo();
      if (matchingInfo.isApplicable()) {

        // Set the values that the user determines matches.
        dossPersRelation.setVoorn(matchingInfo.getFirstName().getValue2());
        dossPersRelation.setGeslachtsnaam(matchingInfo.getLastName().getValue2());
        dossPersRelation.setVoorv(matchingInfo.getPrefix().getValue2());
        dossPersRelation.setTp(matchingInfo.getTitle().getValue2());

        if (matchingInfo.isMatch()) {
          dossPersRelation.setMatchType(RelationMatchType.YES.getCode());
        } else {
          dossPersRelation.setMatchType(RelationMatchType.NO.getCode());
        }
      }
    }

    Relation relation = new Relation(dossPersRelation, person, relatedPerson);
    RelationService relationService = getServices().getRelationService();
    if (relationService.getRelations(relation).size() > 0) {
      throw new ProException(WARNING, "Deze relatie is al toegevoegd.");
    }
    DossPersRelation otherRelation = relationService.getInOtherRelation(relation);
    if (otherRelation != null) {
      RelationType type = RelationType.valueOfCode(otherRelation.getRelationShipType());
      if (type.isRelated()) {
        throw new ProException(WARNING, "Deze relatie is niet mogelijk. <br/>" +
            "Tussen deze personen is de relatie ''{0}'' al toevoegd.", type.getOms().toLowerCase());
      } else {
        throw new ProException(WARNING, "Deze relatie is niet mogelijk. <br/>" +
            "Relatie soort ''{0}'' is al toevoegd.", type.getOms().toLowerCase());
      }
    }
    return relation;
  }

  @Override
  public void onClose() {
    super.onClose();
    getWindow().closeWindow();
  }

  private void showRelatives(RelationType type, DossierPersoon person, DossierPersoon relatedPerson) {
    if (type != null && type.isRelated() && person != null && relatedPerson != null
        && relatedPerson.getDossierPersoonType().is(GERELATEERDE_BRP)) {

      // Find the personslist
      String bsn = relatedPerson.getBurgerServiceNummer().getStringValue();
      BasePLExt plRelatedPerson = getServices().getPersonenWsService().getPersoonslijst(bsn);
      RelationsMatchLayout newLayout = new RelationsMatchLayout(type, person, plRelatedPerson);
      replaceComponent(relativesLayout, newLayout);
      relativesLayout = newLayout;
    } else {
      if (relativesLayout != null) {
        removeComponent(relativesLayout);
        relativesLayout = null;
      }
    }

    getWindow().setHeight(null);
  }
}
