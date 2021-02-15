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

package nl.procura.gba.web.modules.bs.registration.page40.relations.child;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.services.bs.algemeen.functies.BsDossierNaamgebruikUtils.getNormalizedNameWithAge;
import static nl.procura.gba.web.services.bs.registration.RelationType.CHILD_OF;
import static nl.procura.gba.web.services.bs.registration.SourceDocumentType.CUSTOM;
import static nl.procura.gba.web.services.bs.registration.SourceDocumentType.DUTCH;
import static nl.procura.gba.web.services.bs.registration.ValidityDateType.UNKNOWN;

import java.util.List;

import nl.procura.gba.jpa.personen.db.DossPersRelation;
import nl.procura.gba.web.modules.bs.registration.page40.relations.AbstractRelationPage;
import nl.procura.gba.web.modules.bs.registration.page40.relations.RelativeForm;
import nl.procura.gba.web.modules.zaken.common.SourceDocumentForm;
import nl.procura.gba.web.services.bs.registration.RelationshipDateType;
import nl.procura.gba.web.services.bs.registration.SourceDocumentType;
import nl.procura.gba.web.services.bs.registration.ValidityDateType;
import nl.procura.gba.web.services.bs.registration.relations.Relation;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public class ChildDataPage extends AbstractRelationPage {

  private static final List<ValidityDateType> DATE_TYPES = asList(ValidityDateType.DATE_OF_BIRTH,
      UNKNOWN, ValidityDateType.CUSTOM);

  private static final List<SourceDocumentType> DOCUMENT_TYPES = asList(SourceDocumentType.NONE,
      DUTCH, CUSTOM);

  public ChildDataPage(Relation relation) {
    super("Kindgegevens op persoonlijst ouder", relation);

    final RelativeForm relativeDetailsForm = new RelativeForm(relation.getRelatedPerson(), "Gegevens kind");
    setSrcDocForm(new SourceDocumentForm(relation.getSourceDoc(), DOCUMENT_TYPES, DATE_TYPES, null));

    if (relation.isMatchingBrpRelation()) {
      addComponent(new InfoLayout("De gerelateerde gegevens zullen worden aangevuld."));
      getSrcDocForm().setReadOnly(true, true);
    } else {
      addComponent(relativeDetailsForm);
      addComponent(getSrcDocForm());
    }
  }

  @Override
  public Relation validateForms() {
    DossPersRelation dossRel = getRelation().getRelation();
    dossRel.setStartDateType(RelationshipDateType.NOT_SET.getCode());

    if (!getSrcDocForm().isReadOnly()) {
      getSrcDocForm().commit();
      dossRel.setDossSourceDoc(getSrcDocForm().getValidatedValue());
    }
    return getRelation();
  }

  @Override
  public Relation getReverseRelation() {

    DossPersRelation dossPersRelation = DossPersRelation
        .reverse(getRelation().getRelation(), CHILD_OF.getCode());

    return new Relation(dossPersRelation,
        getRelation().getRelatedPerson(),
        getRelation().getPerson());
  }

  @Override
  public void setRelativeDetails() {
    getInfoLayout().appendLine(
        getNormalizedNameWithAge(getRelation().getRelatedPerson()) +
            " zal als kind worden opgenomen bij "
            + getNormalizedNameWithAge(getRelation().getPerson()));
  }

  @Override
  public void disable(List<String> reasons) {
    getSrcDocForm().setReadOnly(true, true);
    getInfoLayout().appendLine(getRelation().getPerson().getNaam().getNaam_naamgebruik_eerste_voornaam() + ", "
        + String.join(" ", reasons));
  }

  @Override
  public boolean isDisabled() {
    return getSrcDocForm().isReadOnly();
  }
}
