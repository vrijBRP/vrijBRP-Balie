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
import static nl.procura.gba.web.services.bs.registration.RelationType.ONLY_1_LEGAL_PARENT;
import static nl.procura.gba.web.services.bs.registration.SourceDocumentType.*;
import static nl.procura.gba.web.services.bs.registration.ValidityDateType.DATE_OF_BIRTH;
import static nl.procura.gba.web.services.bs.registration.ValidityDateType.UNKNOWN;

import java.util.List;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.jpa.personen.db.DossPersRelation;
import nl.procura.gba.web.modules.bs.registration.page40.relations.AbstractRelationPage;
import nl.procura.gba.web.modules.zaken.common.SourceDocumentForm;
import nl.procura.gba.web.services.bs.registration.RelationshipDateType;
import nl.procura.gba.web.services.bs.registration.SourceDocumentType;
import nl.procura.gba.web.services.bs.registration.ValidityDateType;
import nl.procura.gba.web.services.bs.registration.relations.Relation;

public class NoParentDataPage
    extends AbstractRelationPage {

  private static final List<ValidityDateType> DATE_TYPES = asList(DATE_OF_BIRTH,
      UNKNOWN, ValidityDateType.CUSTOM);

  private static final List<SourceDocumentType> DOCUMENT_TYPES = asList(NONE, DUTCH, CUSTOM);

  public NoParentDataPage(Relation relation) {
    super("Juridisch geen oudergegevens op persoonlijst kind", relation);
    setSrcDocForm(new SourceDocumentForm(relation.getSourceDoc(), DOCUMENT_TYPES, DATE_TYPES, value -> {}));
    addComponent(getSrcDocForm());
  }

  @Override
  public Relation validateForms() {
    getSrcDocForm().commit();
    DossPersRelation dossRel = getRelation().getRelation();
    dossRel.setStartDateType(RelationshipDateType.NOT_SET.getCode());
    dossRel.setDossSourceDoc(getSrcDocForm().getValidatedValue());
    return getRelation();
  }

  @Override
  public Relation getReverseRelation() {
    return null;
  }

  @Override
  public void setRelativeDetails() {
    if (getRelation().getRelationType().is(ONLY_1_LEGAL_PARENT)) {
      getInfoLayout().appendLine("Er zal één oudercategorie gevuld worden met deze gegevens bij "
          + getNormalizedNameWithAge(getRelation().getRelatedPerson()));
    } else {
      getInfoLayout().appendLine("Beide oudercategorieën zullen gevuld worden met deze gegevens bij "
          + getNormalizedNameWithAge(getRelation().getRelatedPerson()));
    }
  }

  @Override
  public void disable(List<String> reasons) {
    getSrcDocForm().setReadOnly(true, true);
    getInfoLayout().appendLine("<hr/>" + MiscUtils.setClass(false, String.join("<br/>", reasons)));
  }

  @Override
  public boolean isDisabled() {
    return !getSrcDocForm().isEnabled();
  }
}
