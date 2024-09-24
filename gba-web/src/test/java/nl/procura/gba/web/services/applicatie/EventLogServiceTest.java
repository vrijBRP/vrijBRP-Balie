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

package nl.procura.gba.web.services.applicatie;

import static nl.procura.gba.web.Assertions.assertException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

import org.junit.Before;
import org.junit.Test;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.dao.EventLogDao;
import nl.procura.gba.jpa.personen.dao.EventLogSearch;
import nl.procura.gba.jpa.personen.db.Doss;
import nl.procura.gba.jpa.personen.db.EventLog;
import nl.procura.gba.jpa.personen.db.EventObjectType;
import nl.procura.gba.jpa.personen.db.EventType;
import nl.procura.gba.jpa.personen.utils.GbaJpa;
import nl.procura.gba.web.services.ServicesMock;
import nl.procura.gba.web.services.TemporaryDatabase;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.geboorte.GeboorteService;

public class EventLogServiceTest {

  private EntityManager   em;
  private GeboorteService birthService;
  private EventLogService service;

  @Before
  public void setUp() {
    TemporaryDatabase.ensureCleanMockDatabase();
    GbaJpa gbaJpa = new GbaJpa();
    em = gbaJpa.entityManager();
    birthService = new ServicesMock().getGeboorteService();
    service = new EventLogService(new EventLogDao(gbaJpa));
    resetEventLog();
  }

  @Test
  public void persistMustFailWhenNullValues() {
    // given event with null values
    EventLog eventLog = new EventLog();
    // when
    em.getTransaction().begin();
    em.persist(eventLog);
    // then
    assertException(RollbackException.class, () -> em.getTransaction().commit());
  }

  //@Test // Disabled test because eventlogs are not added at this moment
  public void saveDossierMustResultInOneCreatedEventAndOneOrMoreSavedEvents() {
    // given
    Dossier zaak = (Dossier) birthService.getNewZaak();
    // when
    birthService.save(zaak);
    zaak.setDatumIngang(new DateTime(2020_06_23));
    birthService.save(zaak);
    // then
    List<EventLog> logs = em.createQuery("SELECT e FROM EventLog e"
        + " WHERE e.objectId = :objectId", EventLog.class)
        .setParameter("objectId", zaak.getZaakId())
        .getResultList();
    // birth declarations are saved multiple times :-(
    assertTrue(logs.size() > 1);
    EventLog log = logs.get(0);
    assertEquals(EventType.CREATED, log.getType());
    assertNotNull(log.getUserId());
    assertEquals(EventObjectType.ZAAK, log.getObjectType());
    assertNotNull(log.getTimeStamp());
    for (int i = 1; i < logs.size(); i++) {
      EventLog saved = logs.get(i);
      assertEquals(EventType.SAVED, saved.getType());
      assertNotNull(saved.getUserId());
      assertEquals(EventObjectType.ZAAK, saved.getObjectType());
      assertNotNull(saved.getTimeStamp());
    }
  }

  @Test
  public void getEventsMustReturnAllOrderById() {
    givenEntries();
    EventLogSearch search = EventLogSearch.builder().build();
    // when
    List<EventLog> events = service.getEvents(search);
    // then
    assertEquals(6, events.size());
    assertEventLog(1L, "a", events.get(0));
    assertEventLog(2L, "b", events.get(1));
    assertEventLog(3L, "a", events.get(2));
    assertEventLog(4L, "a", events.get(3));
    assertEventLog(5L, "c", events.get(4));
    assertEventLog(6L, "b", events.get(5));
  }

  @Test
  public void getEventsWithLastIdMustReturnFromLastId() {
    givenEntries();
    EventLogSearch search = EventLogSearch.builder()
        .lastId(3L)
        .build();
    // when
    List<EventLog> events = service.getEvents(search);
    // then
    assertEquals(3, events.size());
    assertEventLog(4L, "a", events.get(0));
    assertEventLog(5L, "c", events.get(1));
    assertEventLog(6L, "b", events.get(2));
  }

  @Test
  public void getEventsWithLastIdAndSizeMustReturnFromLastIdWithSize() {
    givenEntries();
    EventLogSearch search = EventLogSearch.builder()
        .lastId(1L)
        .size(2)
        .build();
    // when
    List<EventLog> events = service.getEvents(search);
    // then
    assertEquals(2, events.size());
    assertEventLog(2L, "b", events.get(0));
    assertEventLog(3L, "a", events.get(1));
  }

  private static void assertEventLog(Long expectedId, String expectedObjectId, EventLog actual) {
    assertEquals(expectedId, actual.getCEventLog());
    assertEquals(expectedObjectId, actual.getObjectId());
  }

  @Test
  public void getEventsWithObjectTypeMustReturnOnlyObjectType() {
    // there's only one type now: ZAAK
    givenEntries();
    EventLogSearch search = EventLogSearch.builder()
        .objectType(EventObjectType.valueOf("ZAAK"))
        .build();
    // when
    List<EventLog> events = service.getEvents(search);
    // then
    assertEquals(6, events.size());
  }

  private void givenEntries() {
    em.getTransaction().begin();
    em.persist(new EventLog(EventType.SAVED, -1L, EventObjectType.ZAAK, "a"));
    em.persist(new EventLog(EventType.SAVED, -1L, EventObjectType.ZAAK, "b"));
    em.persist(new EventLog(EventType.SAVED, -1L, EventObjectType.ZAAK, "a"));
    em.persist(new EventLog(EventType.SAVED, -1L, EventObjectType.ZAAK, "a"));
    em.persist(new EventLog(EventType.SAVED, -1L, EventObjectType.ZAAK, "c"));
    em.persist(new EventLog(EventType.SAVED, -1L, EventObjectType.ZAAK, "b"));
    em.getTransaction().commit();
  }

  @Test
  public void addEventMustWithinTransaction() {
    Doss doss = new Doss();
    assertException(IllegalStateException.class, () -> service.addEvent(doss, EventType.SAVED, null));
  }

  @Test
  public void addEventMustNotCreateWhenObjectIdIsNull() {
    Doss doss = new Doss();
    doss.setZaakId(null);
    // when
    em.getTransaction().begin();
    service.addEvent(doss, EventType.SAVED, null);
    em.getTransaction().commit();
    // then
    List<EventLog> events = service.getEvents(EventLogSearch.builder().build());
    assertEquals(0, events.size());
  }

  private void resetEventLog() {
    em.getTransaction().begin();
    em.createQuery("DELETE FROM EventLog e")
        .executeUpdate();
    em.createQuery("UPDATE Serial s SET s.val = 0 WHERE s.id = 'event_log'")
        .executeUpdate();
    em.getTransaction().commit();
  }
}
