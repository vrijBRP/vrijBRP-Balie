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

import static java.util.stream.Collectors.toList;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.GERELATEERDE_BRP;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.GERELATEERDE_NIET_BRP;
import static nl.procura.gba.web.services.bs.registration.RelationMatchType.INAPPLICABLE;
import static nl.procura.gba.web.services.bs.registration.RelationMatchType.YES;
import static nl.procura.gba.web.services.bs.registration.SourceDocumentType.CUSTOM;
import static nl.procura.gba.web.services.bs.registration.SourceDocumentType.DUTCH;

import java.util.List;
import java.util.Optional;

import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.gba.jpa.personen.db.DossPersRelation;
import nl.procura.gba.jpa.personen.db.DossSourceDoc;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.RelationMatchType;
import nl.procura.gba.web.services.bs.registration.RelationType;
import nl.procura.gba.web.services.bs.registration.SourceDocumentType;

import lombok.Value;

/**
 * Class to hold the DossierPersoon objects together with the JPA entity DossPersRelation. The JPA entity doesn't have
 * a reference to DossierPersoon but to DossPer which doesn't contain all the information necessary.
 */
@Value
public class Relation {

  private final DossPersRelation relation;
  private final DossierPersoon   person;
  private final DossierPersoon   relatedPerson;

  public static Relation fromDossPersRelation(DossPersRelation relation, List<DossierPersoon> people) {
    DossierPersoon person = getPerson(people, relation.getPerson().getCDossPers());
    DossierPersoon relatedPerson = getPerson(people, relation.getRelatedPerson().getCDossPers());
    return new Relation(relation, person, relatedPerson);
  }

  private static DossierPersoon getPerson(List<DossierPersoon> people, Long id) {
    return people.stream()
        .filter(p -> p.getCDossPers().equals(id))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Persoon " + id + " niet gevonden in dossier"));
  }

  public static List<Relation> fromDossPersRelations(List<DossPersRelation> relations, List<DossierPersoon> people) {
    return relations.stream()
        .map(r -> fromDossPersRelation(r, people))
        .collect(toList());
  }

  public DossSourceDoc getSourceDoc() {
    return Optional.ofNullable(getRelation().getDossSourceDoc())
        .orElse(DossSourceDoc.newNotSetSourceDocument());
  }

  /**
   * The relation can be processed if:
   *
   * 1. there is a valid document and
   * 2. there is a complete match between an existing record on the related person BRP PL
   *    or there is no existing relation on the related person PL at all
   */
  public boolean isProcessable() {
    boolean isNotBrpPerson = person.getDossierPersoonType().is(GERELATEERDE_NIET_BRP);
    boolean isDocument = SourceDocumentType.valueOfCode(getSourceDoc().getDocType()).is(DUTCH, CUSTOM);
    boolean isMatch = RelationMatchType.valueOfCode(relation.getMatchType()).is(INAPPLICABLE, YES);
    return isNotBrpPerson || (isMatch && isDocument);
  }

  public RelationType getRelationType() {
    return RelationType.valueOfCode(relation.getRelationShipType());
  }

  public Naamformats getMatchName() {
    return new Naamformats(relation.getVoorn(),
        relation.getGeslachtsnaam(),
        relation.getVoorv(),
        relation.getTp(),
        "", null);
  }

  public RelationMatchType getRelationMatchType() {
    return RelationMatchType.valueOfCode(relation.getMatchType());
  }

  public boolean isMatchingBrpRelation() {
    return getRelationMatchType().is(YES) && getPerson().getDossierPersoonType().is(GERELATEERDE_BRP);
  }
}
