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

package nl.procura.gba.web.services.bs.registration.relations;

import static nl.procura.gba.web.services.bs.registration.RelationType.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;

import java.math.BigDecimal;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.DossPersRelation;
import nl.procura.gba.jpa.personen.db.DossSourceDoc;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.RelationType;
import nl.procura.gba.web.services.bs.registration.RelationshipDateType;
import nl.procura.gba.web.services.bs.registration.ValidityDateType;
import nl.procura.standard.exceptions.ProException;

public class RelationDateStartUtils {

  /**
   * Returns the 'real' date from the source document
   */
  public static DateTime getValidityDate(
      DossSourceDoc sourceDoc,
      DossierPersoon person) {

    DateTime dateTime;
    ValidityDateType dateType = ValidityDateType.valueOfCode(sourceDoc.getValidityDateType());
    switch (dateType) {
      case DATE_OF_BIRTH:
        dateTime = new DateTime(person.getDGeb());
        break;
      case UNKNOWN:
        dateTime = new DateTime(BigDecimal.valueOf(0L)); // 0 = unknown. It is a valid BRP value
        break;
      case CUSTOM:
        dateTime = new DateTime(sourceDoc.getValidityDate());
        break;
      default:
        dateTime = new DateTime();
        break;
    }
    if (dateTime.getLongDate() < 0) {
      throw new ProException(ERROR, "De datum ingang geldigheid ({0}) is niet bekend.",
          dateType.getDescription().toLowerCase());
    }
    return dateTime;
  }

  /**
   * Returns the 'real' date from the relation
   */
  public static DateTime getRelationDate(
      DossPersRelation relation,
      RelationType relationType) {

    DateTime dateTime;
    RelationshipDateType dateType = RelationshipDateType.valueOfCode(relation.getStartDateType());
    switch (dateType) {
      case DATE_OF_BIRTH:
        dateTime = getBirthDate(relation, relationType);
        break;
      case UNKNOWN:
        dateTime = new DateTime(BigDecimal.valueOf(0L)); // 0 = unknown. It is a valid BRP value
        break;
      case CUSTOM:
        dateTime = new DateTime(relation.getCustomStartDate());
        break;
      default:
        dateTime = new DateTime();
        break;
    }
    if (dateTime.getLongDate() < 0) {
      throw new ProException(ERROR, "De datum ingang geldigheid ({0}) is niet bekend.",
          dateType.getDescription().toLowerCase());
    }
    return dateTime;
  }

  /**
   * Returns the 'real' date from the relation source document
   */
  public static DateTime getValidityDate(
      DossSourceDoc sourceDoc,
      DossPersRelation relation,
      RelationType relationType) {

    DateTime dateTime;
    ValidityDateType dateType = ValidityDateType.valueOfCode(sourceDoc.getValidityDateType());
    switch (dateType) {
      case DATE_OF_BIRTH:
        dateTime = getBirthDate(relation, relationType);
        break;
      case COMMITMENT_START_DATE:
        dateTime = new DateTime(relation.getCustomStartDate());
        break;
      case COMMITMENT_END_DATE:
        dateTime = new DateTime(relation.getCustomEndDate());
        break;
      case UNKNOWN:
        dateTime = new DateTime(BigDecimal.valueOf(0L)); // 0 = unknown. It is a valid BRP value
        break;
      case CUSTOM:
        dateTime = new DateTime(sourceDoc.getValidityDate());
        break;
      default:
      case NOT_SET:
        dateTime = new DateTime();
    }
    if (dateTime.getLongDate() < 0) {
      throw new ProException(ERROR, "De datum ingang geldigheid ({0}) is niet bekend.",
          dateType.getDescription().toLowerCase());
    }
    return dateTime;
  }

  /**
   * Returns the childbirth date.
   * The child can be the person or relatedperson depending on the relationType.
   *
   * child 'is child of' parent
   * parent 'is parent of' child
   *
   */
  public static DateTime getBirthDate(DossPersRelation rel, RelationType relationType) {
    if (relationType.is(CHILD_OF, ONLY_1_LEGAL_PARENT, NO_LEGAL_PARENTS)) {
      return new DateTime(rel.getPerson().getDGeb());
    } else if (relationType.is(PARENT_OF)) {
      return new DateTime(rel.getRelatedPerson().getDGeb());
    }
    return new DateTime(BigDecimal.valueOf(-1L));
  }
}
