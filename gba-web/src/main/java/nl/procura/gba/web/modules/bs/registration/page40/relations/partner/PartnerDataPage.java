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

package nl.procura.gba.web.modules.bs.registration.page40.relations.partner;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.services.bs.algemeen.functies.BsDossierNaamgebruikUtils.getNormalizedNameWithAge;
import static nl.procura.gba.web.services.bs.registration.RelationType.PARTNER_OF;
import static nl.procura.gba.web.services.bs.registration.SourceDocumentType.*;
import static nl.procura.gba.web.services.bs.registration.SourceDocumentType.CUSTOM;
import static nl.procura.gba.web.services.bs.registration.ValidityDateType.*;

import java.util.List;

import nl.procura.gba.jpa.personen.db.DossPersRelation;
import nl.procura.gba.jpa.personen.db.DossSourceDoc;
import nl.procura.gba.web.modules.bs.registration.page40.relations.AbstractRelationPage;
import nl.procura.gba.web.modules.bs.registration.page40.relations.RelativeForm;
import nl.procura.gba.web.modules.zaken.common.SourceDocumentForm;
import nl.procura.gba.web.services.bs.registration.SourceDocumentType;
import nl.procura.gba.web.services.bs.registration.ValidityDateType;
import nl.procura.gba.web.services.bs.registration.relations.Relation;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public class PartnerDataPage extends AbstractRelationPage {

  private static final List<ValidityDateType> DATE_TYPES = asList(
      COMMITMENT_START_DATE, COMMITMENT_END_DATE,
      UNKNOWN, ValidityDateType.CUSTOM);

  private static final List<SourceDocumentType> DOCUMENT_TYPES = asList(NONE, DUTCH, CUSTOM);

  private final PartnerRelationForm1 partnerDetailsForm1;
  private final PartnerRelationForm2 partnerDetailsForm2;

  public PartnerDataPage(Relation relation) {
    super("Partnergegevens", relation);

    final RelativeForm personDetailsForm = new RelativeForm(relation.getRelatedPerson(), "Gegevens partner");
    partnerDetailsForm1 = new PartnerRelationForm1(relation.getRelation());
    partnerDetailsForm2 = new PartnerRelationForm2(relation.getRelation());

    setSrcDocForm(new SourceDocumentForm(relation.getSourceDoc(), DOCUMENT_TYPES, DATE_TYPES, null));

    addComponent(personDetailsForm);
    if (relation.isMatchingBrpRelation()) {
      addComponent(new InfoLayout("De gerelateerde gegevens zullen worden aangevuld."));
      getSrcDocForm().setReadOnly(true, true);
    } else {
      addComponent(new HLayout(partnerDetailsForm1).addExpandComponent(partnerDetailsForm2).widthFull());
      addComponent(getSrcDocForm());
    }
  }

  @Override
  public Relation validateForms() {

    partnerDetailsForm1.commit();
    partnerDetailsForm2.commit();

    DossPersRelation dossRel = getRelation().getRelation();

    partnerDetailsForm1.saveRelation(dossRel);
    partnerDetailsForm2.saveRelation(dossRel);

    if (!getSrcDocForm().isReadOnly()) {
      DossSourceDoc sourceDoc = getSrcDocForm().getValidatedValue();
      dossRel.setDossSourceDoc(sourceDoc);
    }

    return getRelation();
  }

  @Override
  public Relation getReverseRelation() {

    DossPersRelation dossPersRelation = DossPersRelation
        .reverse(getRelation().getRelation(), PARTNER_OF.getCode());

    return new Relation(dossPersRelation,
        getRelation().getRelatedPerson(),
        getRelation().getPerson());
  }

  @Override
  public void setRelativeDetails() {
    getInfoLayout().appendLine(getNormalizedNameWithAge(getRelation().getRelatedPerson()) +
        " zal als partner worden opgenomen bij " +
        getNormalizedNameWithAge(getRelation().getPerson()));
  }

  @Override
  public void disable(List<String> reasons) {
    partnerDetailsForm1.setReadOnly(true);
    partnerDetailsForm2.setReadOnly(true);
    getSrcDocForm().setReadOnly(true, true);
    getInfoLayout().appendLine(getRelation().getPerson().getNaam().getNaam_naamgebruik_eerste_voornaam()
        + ", " + String.join(" ", reasons));
  }

  @Override
  public boolean isDisabled() {
    return !getSrcDocForm().isEnabled() && !partnerDetailsForm1.isEnabled();
  }
}
