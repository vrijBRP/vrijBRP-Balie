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

package nl.procura.gba.web.modules.bs.registration.page40.relations.parent;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.services.bs.algemeen.functies.BsDossierNaamgebruikUtils.getNormalizedNameWithAge;
import static nl.procura.gba.web.services.bs.registration.RelationType.PARENT_OF;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

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

public class ParentDataPage extends AbstractRelationPage {

  private static final List<ValidityDateType> DATE_TYPES = asList(ValidityDateType.DATE_OF_BIRTH,
      ValidityDateType.UNKNOWN, ValidityDateType.CUSTOM);

  private static final List<SourceDocumentType> DOCUMENT_TYPES = asList(SourceDocumentType.NONE,
      SourceDocumentType.DUTCH, SourceDocumentType.CUSTOM);

  private final ParentRelationForm parentRelationForm;

  public ParentDataPage(Relation relation) {
    super("Oudergegevens op persoonlijst kind", relation);

    RelativeForm relativeDetailsForm = new RelativeForm(relation.getRelatedPerson(), "Gegevens ouder");
    parentRelationForm = new ParentRelationForm(relation.getRelation());
    setSrcDocForm(new SourceDocumentForm(relation.getSourceDoc(), DOCUMENT_TYPES, DATE_TYPES, null));

    addComponent(relativeDetailsForm);
    if (relation.isMatchingBrpRelation()) {
      addComponent(new InfoLayout("De gerelateerde gegevens zullen worden aangevuld."));
      getSrcDocForm().setReadOnly(true, true);
    } else {
      addComponent(parentRelationForm);
      addComponent(getSrcDocForm());
    }
  }

  @Override
  public Relation validateForms() {

    DossPersRelation dossRel = getRelation().getRelation();

    if (!getSrcDocForm().isReadOnly()) {
      getSrcDocForm().commit();
      dossRel.setDossSourceDoc(getSrcDocForm().getValidatedValue());
    }

    if (!parentRelationForm.isReadOnly()) {
      parentRelationForm.commit();
      ParentDetailBean relDetailBean = parentRelationForm.getBean();
      dossRel.setCustomStartDate(toBigDecimal(-1L));
      dossRel.setStartDateType(relDetailBean.getStartOfRelation().getCode());

      if (RelationshipDateType.CUSTOM == relDetailBean.getStartOfRelation()) {
        dossRel.setCustomStartDate(relDetailBean.getDateOfRelationStart().toBigDecimal());
      }
    }
    return getRelation();
  }

  @Override
  public Relation getReverseRelation() {
    DossPersRelation dossPersRelation = DossPersRelation
        .reverse(getRelation().getRelation(), PARENT_OF.getCode());

    return new Relation(dossPersRelation,
        getRelation().getRelatedPerson(),
        getRelation().getPerson());
  }

  @Override
  public void setRelativeDetails() {
    getInfoLayout().appendLine(
        getNormalizedNameWithAge(getRelation().getRelatedPerson()) +
            " zal als ouder worden opgenomen bij "
            + getNormalizedNameWithAge(getRelation().getPerson()));
  }

  @Override
  public void disable(List<String> reasons) {
    parentRelationForm.setReadOnly(true);
    getSrcDocForm().setReadOnly(true, true);
    getInfoLayout().appendLine(getRelation().getPerson().getNaam().getNaam_naamgebruik_eerste_voornaam() +
        ", " + String.join(" ", reasons));
  }

  @Override
  public boolean isDisabled() {
    return !getSrcDocForm().isEnabled() && !parentRelationForm.isEnabled();
  }
}
