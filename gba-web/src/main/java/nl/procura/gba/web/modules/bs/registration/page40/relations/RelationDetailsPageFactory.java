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

import static nl.procura.burgerzaken.gba.core.enums.GBACat.HUW_GPS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.*;
import static nl.procura.gba.web.services.bs.algemeen.functies.BsDossierNaamgebruikUtils.getNormalizedNameWithAge;
import static nl.procura.gba.web.services.bs.registration.RelationType.*;
import static nl.procura.gba.web.services.bs.registration.ValidityDateType.*;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.Cat2OuderExt;
import nl.procura.diensten.gba.ple.extensions.Cat9KindExt;
import nl.procura.gba.jpa.personen.db.DossSourceDoc;
import nl.procura.gba.web.modules.bs.registration.page40.relations.child.ChildDataPage;
import nl.procura.gba.web.modules.bs.registration.page40.relations.parent.NoParentDataPage;
import nl.procura.gba.web.modules.bs.registration.page40.relations.parent.ParentDataPage;
import nl.procura.gba.web.modules.bs.registration.page40.relations.partner.PartnerDataPage;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.RelationType;
import nl.procura.gba.web.services.bs.registration.SourceDocumentType;
import nl.procura.gba.web.services.bs.registration.ValidityDateType;
import nl.procura.gba.web.services.bs.registration.relations.Relation;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class RelationDetailsPageFactory {

  private final PersonenWsService personenWsService;

  public RelationDetailsPageFactory(PersonenWsService personenWsService) {
    this.personenWsService = personenWsService;
  }

  /**
   * Create a page for the given relation.
   * <p>
   * When a person is parent of the related person, the <b>child</b> data page is returned.
   * So the <b>child</b> data is entered on the personal data list (persoonslijst) of the person.
   * <p>
   * When a person is child of the related person, the <b>parent</b> data page is returned.
   * So the <b>parent</b> data is entered on the personal data list (persoonslijst) of the person.
   * <p>
   * When a person is partner of the related person, the partner data page is returned.
   */
  public AbstractRelationPage create(Relation relation) {
    AbstractRelationPage page = createDataPage(relation);
    List<String> disabledReasons = getDisabledReasons(relation.getPerson());
    if (disabledReasons.isEmpty()) {
      page.setRelativeDetails();
      page.updateWarnings();
    } else {
      page.disable(disabledReasons);
    }
    return page;
  }

  private AbstractRelationPage createDataPage(Relation relation) {
    RelationType type = RelationType.valueOfCode(relation.getRelation().getRelationShipType());
    switch (type) {
      case PARENT_OF:
        return new ChildDataPage(prefillValues(relation));
      case PARTNER_OF:
        return new PartnerDataPage(prefillValues(relation));
      case CHILD_OF:
        return new ParentDataPage(prefillValues(relation));
      case ONLY_1_LEGAL_PARENT:
      case NO_LEGAL_PARENTS:
        return new NoParentDataPage(relation);
      default:
        throw new IllegalStateException();
    }
  }

  /**
   * If there is a relation between a registrant and BRP person
   * then the relationship values are prefilled
   */
  private Relation prefillValues(Relation relation) {

    if (!pos(relation.getRelation().getCDossRelative())) {
      DossierPersoon relatedPerson = relation.getRelatedPerson();

      if (relatedPerson.getDossierPersoonType().is(DossierPersoonType.GERELATEERDE_BRP)) {
        BsnFieldValue bsn = relatedPerson.getBurgerServiceNummer();
        AnrFieldValue anr = relatedPerson.getAnummer();

        if (bsn.isCorrect() || anr.isCorrect()) {
          BasePLExt pl = personenWsService.getPersoonslijst(bsn.getStringValue(), anr.getStringValue());

          if (relation.getRelationType().is(PARTNER_OF)) {
            for (BasePLSet set : pl.getCat(HUW_GPS).getSets()) {
              if (isName(relation, set.getLatestRec())) {
                BasePLRec recH = pl.getHuwelijk().getSluiting(set, "");
                BasePLRec recO = pl.getHuwelijk().getOntbinding(set, "");
                setMarriageFields(relation, recH);
                setDivorceFields(relation, recO);
              }
            }
          } else if (relation.getRelationType().is(PARENT_OF)) {
            for (Cat2OuderExt parent : pl.getOuders().getOuders()) {
              if (isName(relation, parent.getRecord())) {
                long date = along(parent.getRecord()
                    .getElemVal(DATUM_INGANG_FAM_RECHT_BETREK).getVal());
                setSourceDocumentFields(relation, parent.getRecord(), DATE_OF_BIRTH, date);
              }
            }
          } else if (relation.getRelationType().is(CHILD_OF)) {
            for (Cat9KindExt kind : pl.getKinderen().getKinderen()) {
              if (isName(relation, kind.getRecord())) {
                long date = along(kind.getRecord()
                    .getElemVal(INGANGSDAT_GELDIG).getVal());
                setSourceDocumentFields(relation, kind.getRecord(), DATE_OF_BIRTH, date);
              }
            }
          }
        }
      }
    }
    return relation;
  }

  private boolean isName(Relation relation, BasePLRec record) {
    String firstName = record.getElemVal(VOORNAMEN).getVal();
    String lastName = record.getElemVal(GESLACHTSNAAM).getVal();
    String prefix = record.getElemVal(VOORV_GESLACHTSNAAM).getVal();
    String tp = record.getElemVal(TITEL_PREDIKAAT).getVal();
    boolean isLastName = Objects.equals(relation.getRelation().getGeslachtsnaam(), lastName);
    boolean isFirstName = Objects.equals(relation.getRelation().getVoorn(), firstName);
    boolean isPrefix = Objects.equals(relation.getRelation().getVoorv(), prefix);
    boolean isTp = Objects.equals(relation.getRelation().getTp(), tp);
    return isFirstName && isLastName && isPrefix && isTp;
  }

  /**
   * If there is a marriage than prefill them
   */
  private void setMarriageFields(Relation relation, BasePLRec rec) {
    long date = along(rec.getElemVal(DATUM_VERBINTENIS).getVal());
    String type = rec.getElemVal(SOORT_VERBINTENIS).getVal();
    String country = rec.getElemVal(LAND_VERBINTENIS).getVal();
    String place = rec.getElemVal(PLAATS_VERBINTENIS).getVal();

    if (date >= 0) {
      relation.getRelation().setCustomStartDate(toBigDecimal(date));
      relation.getRelation().setCommitmentType(type);
      relation.getRelation().setStartDateCountry(country);
      relation.getRelation().setStartDateMunicipality(place);

      setSourceDocumentFields(relation, rec, COMMITMENT_START_DATE, date);
    }
  }

  /**
   * If there is a divorce then prefill them
   */
  private void setDivorceFields(Relation relation, BasePLRec rec) {
    long date = along(rec.getElemVal(DATUM_ONTBINDING).getVal());
    String reason = rec.getElemVal(REDEN_ONTBINDING).getVal();
    String country = rec.getElemVal(LAND_ONTBINDING).getVal();
    String place = rec.getElemVal(PLAATS_ONTBINDING).getVal();

    if (date >= 0) {
      relation.getRelation().setCustomEndDate(toBigDecimal(date));
      relation.getRelation().setEndReason(reason);
      relation.getRelation().setEndDateCountry(country);
      relation.getRelation().setEndDateMunicipality(place);

      setSourceDocumentFields(relation, rec, COMMITMENT_END_DATE, date);
    }
  }

  /**
   * Update the source fields
   */
  private void setSourceDocumentFields(Relation relation,
      BasePLRec rec,
      ValidityDateType validityDateType,
      long date) {

    String gReg = rec.getElemVal(REGIST_GEM_AKTE).getVal();
    String akteNr = rec.getElemVal(AKTENR).getVal();

    String gOntl = rec.getElemVal(GEMEENTE_DOC).getVal();
    String dOntl = rec.getElemVal(DATUM_DOC).getVal();
    String docOnl = rec.getElemVal(BESCHRIJVING_DOC).getVal();

    long dGeld = along(rec.getElemVal(INGANGSDAT_GELDIG).getVal());

    DossSourceDoc dossSourceDoc = DossSourceDoc.newNotSetSourceDocument();
    if (dGeld == date) {
      dossSourceDoc.setValidityDateType(validityDateType.getCode());
    } else {
      dossSourceDoc.setValidityDateType(ValidityDateType.CUSTOM.getCode());
      dossSourceDoc.setValidityDate(toBigDecimal(dOntl));
    }

    if (StringUtils.isNotBlank(akteNr)) {
      dossSourceDoc.setDocType(SourceDocumentType.DUTCH.getCode());
      dossSourceDoc.setDocMun(gReg);
      dossSourceDoc.setDocNumber(akteNr);
    } else {
      dossSourceDoc.setDocType(SourceDocumentType.CUSTOM.getCode());
      dossSourceDoc.setDocMun(gOntl);
      dossSourceDoc.setDocDescr(docOnl);
    }
    relation.getRelation().setDossSourceDoc(dossSourceDoc);
  }

  private List<String> getDisabledReasons(DossierPersoon person) {
    List<String> disabledReasons = new ArrayList<>();
    if (person.getBurgerServiceNummer().isCorrect() || person.getAnummer().isCorrect()) {
      BasePLExt personalList = personenWsService.getPersoonslijst(
          person.getBurgerServiceNummer().getStringValue(),
          person.getAnummer().getStringValue());
      if (personalList.getPersoon().getStatus().isOpgeschort()) {
        disabledReasons.add("heeft een opgeschorte persoonlijst. " +
            "De persoonlijst van " + getNormalizedNameWithAge(person)
            + " zal niet worden bijgewerkt.");
      }
      if (personalList.getPersoon().getStatus().isBlokkering()) {
        disabledReasons.add("heeft een geblokkeerde persoonlijst. " +
            "De persoonlijst van " + getNormalizedNameWithAge(person)
            + " zal niet worden bijgewerkt.");
      }
    } else {
      disabledReasons.add("staat niet ingeschreven in de BRP. Deze relatie zal geen verwerking hebben in de BRP.");
    }
    return disabledReasons;
  }
}
