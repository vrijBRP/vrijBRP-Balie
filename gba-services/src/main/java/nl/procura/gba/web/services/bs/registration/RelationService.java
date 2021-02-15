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

package nl.procura.gba.web.services.bs.registration;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

import nl.procura.gba.jpa.personen.db.DossPer;
import nl.procura.gba.jpa.personen.db.DossPersRelation;
import nl.procura.gba.jpa.personen.db.DossPersRelation_;
import nl.procura.gba.jpa.personen.utils.GbaJpa;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.relations.Relation;

/**
 * Service for first registration relations between people.
 */
public class RelationService extends AbstractService {

  private static final long serialVersionUID = -5954589299784285076L;

  public RelationService() {
    super("Eerste inschrijving relaties");
  }

  @Transactional
  public void save(DossPersRelation relation) {
    saveEntity(relation);
  }

  @Transactional
  public void remove(DossPersRelation relation) {
    removeEntity(relation);
    DossPersRelation reversedRelation = getInOtherRelation(
        relation.getRelatedPerson(),
        RelationType.UNKNOWN,
        relation.getPerson());

    if (reversedRelation != null) {
      removeEntity(reversedRelation);
    }
  }

  public void remove(List<DossPersRelation> relations) {
    relations.forEach(this::remove);
  }

  /**
   * Return only the relations where the 'person' object matches
   */
  public List<DossPersRelation> findByPerson(DossierPersoon person) {
    return findByPeople(Collections.singletonList(person), true);
  }

  public List<DossPersRelation> findByPeople(Collection<DossierPersoon> people) {
    return findByPeople(people, false);
  }

  /**
   * Find relations by searching in both person and related person
   *
   */
  private List<DossPersRelation> findByPeople(Collection<DossierPersoon> people, boolean onlyPerson) {
    List<Long> peopleIds = people.stream()
        .map(DossPer::getCDossPers)
        .collect(toList());

    List<DossPersRelation> relations = new ArrayList<>();
    if (!peopleIds.isEmpty()) {
      EntityManager manager = GbaJpa.getManager();
      CriteriaBuilder builder = manager.getCriteriaBuilder();
      CriteriaQuery<DossPersRelation> query = builder.createQuery(DossPersRelation.class);
      Root<DossPersRelation> from = query.from(DossPersRelation.class);

      if (onlyPerson) {
        query.where(from.get(DossPersRelation_.person).in(peopleIds));
      } else {
        query
            .where(builder.or(from.get(DossPersRelation_.person).in(peopleIds),
                from.get(DossPersRelation_.relatedPerson).in(peopleIds)))
            .orderBy(builder.asc(from.get(DossPersRelation_.cDossRelative)));
      }
      relations.addAll(manager.createQuery(query).getResultList());
    }
    return relations;
  }

  public List<DossPersRelation> getRelations(Relation relation) {
    return getRelations(relation.getPerson(), relation.getRelationType(), relation.getRelatedPerson());
  }

  public List<DossPersRelation> getRelations(DossPer person, RelationType type) {
    return getRelations(person, type, null);
  }

  public List<DossPersRelation> getRelations(DossPer person,
      RelationType type,
      DossPer relatedPerson) {

    EntityManager manager = GbaJpa.getManager();
    CriteriaBuilder builder = manager.getCriteriaBuilder();
    CriteriaQuery<DossPersRelation> query = builder.createQuery(DossPersRelation.class);
    Root<DossPersRelation> from = query.from(DossPersRelation.class);

    List<Expression> preds = new ArrayList<>();
    preds.add(from.get(DossPersRelation_.person).in(person.getCDossPers()));
    preds.add(from.get(DossPersRelation_.relationShipType).in(type.getCode()));

    if (relatedPerson != null) {
      preds.add(from.get(DossPersRelation_.relatedPerson).in(relatedPerson.getCDossPers()));
    }
    query.select(from);
    query.where(preds.toArray(new Predicate[0]));
    return manager.createQuery(query).getResultList();
  }

  public DossPersRelation getInOtherRelation(Relation relation) {
    return getInOtherRelation(relation.getPerson(), relation.getRelationType(), relation.getRelatedPerson());
  }

  public DossPersRelation getInOtherRelation(DossPer person,
      RelationType type,
      DossPer relatedPerson) {

    EntityManager manager = GbaJpa.getManager();
    CriteriaBuilder builder = manager.getCriteriaBuilder();
    CriteriaQuery<DossPersRelation> query = builder.createQuery(DossPersRelation.class);
    Root<DossPersRelation> from = query.from(DossPersRelation.class);

    List<Expression> preds = new ArrayList<>();
    preds.add(from.get(DossPersRelation_.person).in(person.getCDossPers()));
    preds.add(builder.not(from.get(DossPersRelation_.relationShipType).in(type.getCode())));
    preds.add(from.get(DossPersRelation_.relatedPerson).in(relatedPerson.getCDossPers()));
    query.select(from);
    query.where(preds.toArray(new Predicate[0]));

    List<DossPersRelation> resultList = manager.createQuery(query).getResultList();
    return resultList.isEmpty() ? null : resultList.get(0);
  }
}
