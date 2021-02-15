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

package nl.procura.gba.jpa.personen.dao;

import static nl.procura.gba.jpa.personen.db.EventLog_.C_EVENT_LOG;
import static nl.procura.gba.jpa.personen.db.EventLog_.OBJECT_TYPE;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.jpa.personen.db.EventLog;
import nl.procura.gba.jpa.personen.db.EventLogEntity;
import nl.procura.gba.jpa.personen.db.EventType;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

public class EventLogDao {

  private static final Logger LOGGER              = LoggerFactory.getLogger(EventLogDao.class);
  private static final int    DEFAULT_MAX_RESULTS = 50;

  private final GbaJpa gbaJpa;

  @Inject
  public EventLogDao(GbaJpa gbaJpa) {
    this.gbaJpa = gbaJpa;
  }

  public void add(EventLogEntity entity, EventType eventType, Long userId) {
    EntityManager em = gbaJpa.entityManager();
    if (!em.getTransaction().isActive()) {
      throw new IllegalStateException("Logging an event must be within a transaction");
    }
    String objectId = entity.getObjectId();
    if (objectId == null) {
      LOGGER.warn("Cannot write event log for entity without object id, entity {}", entity);
      return;
    }
    em.persist(new EventLog(eventType, userId, entity.getObjectType(), objectId));
  }

  public List<EventLog> find(EventLogSearch search) {
    EntityManager em = gbaJpa.entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<EventLog> query = cb.createQuery(EventLog.class);
    Root<EventLog> from = query.from(EventLog.class);
    query.orderBy(cb.asc(from.get(C_EVENT_LOG)));
    // parameters
    search.lastId().ifPresent(lastId -> query.where(cb.greaterThan(from.get(C_EVENT_LOG), lastId)));
    search.objectType().ifPresent(objectType -> query.where(cb.equal(from.get(OBJECT_TYPE), objectType)));

    return em.createQuery(query)
        .setMaxResults(search.size().orElse(DEFAULT_MAX_RESULTS))
        .getResultList();
  }

}
