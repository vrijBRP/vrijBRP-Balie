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

package nl.procura.gba.jpa.personen.dao.views;

import static nl.procura.gba.web.services.TemporaryDatabase.getConnection;
import static nl.procura.gba.web.services.TemporaryDatabase.getHsqlDatabase;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.*;
import nl.procura.gba.jpa.personen.utils.GbaJpa;
import nl.procura.gba.web.common.database.checks.*;
import nl.procura.gba.web.services.TemporaryDatabase;

import liquibase.database.core.HsqlDatabase;
import lombok.SneakyThrows;

public class DBCheckTests {

  @Before
  public void setUp() {
    TemporaryDatabase.ensureCleanMockDatabase();
  }

  @Test
  @SneakyThrows
  public void mustExecuteAllChecks() {
    HsqlDatabase database = getHsqlDatabase(getConnection());
    // Pre-liquibase checks
    new DBCheckPre1(GbaJpa.getManager(), database, "").execute();
    new DBCheckPre2(GbaJpa.getManager(), database, "").execute();
    // Post-liquibase checks
    new DBCheckPost1(GbaJpa.getManager(), database, "").execute();
    new DBCheckPost2(GbaJpa.getManager(), database, "").execute();
    new DBCheckPost3(GbaJpa.getManager(), database, "").execute();
    new DBCheckPost4(GbaJpa.getManager(), database, "").execute();
    new DBCheckPost5(GbaJpa.getManager(), database, "").execute();
    new DBCheckPost7(GbaJpa.getManager(), database, "").execute();
    new DBCheckPost7(GbaJpa.getManager(), database, "").execute();
    new DBCheckPost8(GbaJpa.getManager(), database, "").execute();
    new DBCheckPost9(GbaJpa.getManager(), database, "").execute();
  }

  @Test
  @SneakyThrows
  public void mustDeleteDanglingZaakIds() {
    HsqlDatabase database = getHsqlDatabase(getConnection());
    EntityManager em = GbaJpa.getManager();
    assertCleanDatabase(em);

    em.getTransaction().begin();

    addUittreksel(em, "1");
    addUittreksel(em, "2");
    assertCount(em, 2L, "uitt_aanvr");

    addZaakId(em, "systeem-x", "1", "1");
    addZaakId(em, "systeem-y", "3", "2");
    addZaakId(em, "systeem-z", "3", "3");
    assertCount(em, 3L, "zaak_id");

    DBCheckPost9 dbCheckPost9 = new DBCheckPost9(GbaJpa.getManager(), database, "");
    Assert.assertEquals(2L, dbCheckPost9.deleteDanglingZaakIdRecords());
    assertCount(em, 1L, "zaak_id");

    em.getTransaction().rollback();
  }

  @Test
  @SneakyThrows
  public void mustDeleteDanglingAantekeningRecords() {
    HsqlDatabase database = getHsqlDatabase(getConnection());
    EntityManager em = GbaJpa.getManager();
    assertCleanDatabase(em);

    em.getTransaction().begin();

    addUittreksel(em, "1");
    assertCount(em, 1L, "uitt_aanvr");

    addAantekening(em, "1");
    addAantekening(em, "2");

    DBCheckPost9 dbCheckPost9 = new DBCheckPost9(GbaJpa.getManager(), database, "");
    Assert.assertEquals(1L, dbCheckPost9.deleteDanglingAantekeningRecords());
    assertCount(em, 1L, "aantekening");

    em.getTransaction().rollback();
  }

  @Test
  @SneakyThrows
  public void mustDeleteDanglingZaakAttrRecords() {
    HsqlDatabase database = getHsqlDatabase(getConnection());
    EntityManager em = GbaJpa.getManager();
    assertCleanDatabase(em);

    em.getTransaction().begin();

    addUittreksel(em, "1");
    assertCount(em, 1L, "uitt_aanvr");

    addZaakAttr(em, "MIJN-OVERHEID", "1", "Omschrijving1");
    addZaakAttr(em, "MIJN-OVERHEID", "2", "Omschrijving1");

    DBCheckPost9 dbCheckPost9 = new DBCheckPost9(GbaJpa.getManager(), database, "");
    Assert.assertEquals(1L, dbCheckPost9.deleteDanglingZaakAttrRecords());
    assertCount(em, 1L, "zaak_attr");

    em.getTransaction().rollback();
  }

  @Test
  @SneakyThrows
  public void mustDeleteDanglingZaakRelRecords() {
    HsqlDatabase database = getHsqlDatabase(getConnection());
    EntityManager em = GbaJpa.getManager();
    assertCleanDatabase(em);

    em.getTransaction().begin();

    addUittreksel(em, "1");
    addUittreksel(em, "2");
    assertCount(em, 2L, "uitt_aanvr");

    addZaakId(em, "ABC", "10", "xxx");
    addZaakId(em, "ABC", "yyy", "11");
    assertCount(em, 2L, "zaak_id");

    // Both found
    addZaakRel(em, "1", "2");
    addZaakRel(em, "1", "10");
    addZaakRel(em, "10", "11");
    addZaakRel(em, "2", "10");

    // One found
    addZaakRel(em, "1", "zz");
    addZaakRel(em, "zz", "2");

    // None found
    addZaakRel(em, "aa", "bb");
    addZaakRel(em, "cc", "dd");

    assertCount(em, 8L, "zaak_rel");

    DBCheckPost9 dbCheckPost9 = new DBCheckPost9(GbaJpa.getManager(), database, "");
    Assert.assertEquals(4L, dbCheckPost9.deleteDanglingZaakRelRecords());
    assertCount(em, 4L, "zaak_rel");

    em.getTransaction().rollback();
  }

  @Test
  @SneakyThrows
  public void mutDeleteDanglingPresentievraagRecords() {
    HsqlDatabase database = getHsqlDatabase(getConnection());
    EntityManager em = GbaJpa.getManager();
    assertCleanDatabase(em);

    em.getTransaction().begin();

    addUittreksel(em, "1");
    addUittreksel(em, "2");
    assertCount(em, 2L, "uitt_aanvr");

    addPresentievraag(em, "1", LocalDate.now());
    addPresentievraag(em, "2", LocalDate.now());
    addPresentievraag(em, "3", LocalDate.now()); // Delete
    addPresentievraag(em, "", LocalDate.now()); // Not old enough to delete
    addPresentievraag(em, "", LocalDate.now().minusYears(1)); // Delete
    assertCount(em, 5L, "presentievraag");

    DBCheckPost9 dbCheckPost9 = new DBCheckPost9(GbaJpa.getManager(), database, "");
    Assert.assertEquals(2L, dbCheckPost9.deleteDanglingPresentievraagRecords());
    assertCount(em, 3L, "presentievraag");

    em.getTransaction().rollback();
  }

  private void assertCleanDatabase(EntityManager em) {
    assertCount(em, 0L, "zaak_id");
    assertCount(em, 0L, "zaak_attr");
    assertCount(em, 0L, "zaak_rel");
  }

  private void assertCount(EntityManager em, long count, String table) {
    Assert.assertEquals(count, em.createNativeQuery("select count(*) from " + table).getSingleResult());
  }

  private void addUittreksel(EntityManager em, String zaakId) {
    Document document = new Document();
    document.setType("pl");
    em.persist(document);

    UittAanvr uittAanvr = new UittAanvr();
    uittAanvr.setZaakId(zaakId);
    uittAanvr.setDAanvr(BigDecimal.valueOf(20200101));
    uittAanvr.setDocument(document);

    em.persist(uittAanvr);
  }

  private void addZaakId(EntityManager em, String type, String intern_id, String externId) {
    ZaakId zaakId = new ZaakId();
    ZaakIdPK pk = new ZaakIdPK();
    pk.setInternId(intern_id);
    pk.setType(type);
    zaakId.setId(pk);
    zaakId.setExternId(externId);
    em.persist(zaakId);
  }

  private void addZaakAttr(EntityManager em, String attr, String zaakId, String oms) {
    ZaakAttr zaakAttr = new ZaakAttr();
    ZaakAttrPK pk = new ZaakAttrPK();
    pk.setZaakAttr(attr);
    pk.setZaakId(zaakId);
    zaakAttr.setId(pk);
    zaakAttr.setOms(oms);
    em.persist(zaakAttr);
  }

  private void addAantekening(EntityManager em, String zaakId) {
    Aantekening aantekening = new Aantekening();
    aantekening.setZaakId(zaakId);
    aantekening.setBsn(BigDecimal.valueOf(1L));
    em.persist(aantekening);
  }

  private void addZaakRel(EntityManager em, String zaakId, String zaakIdRel) {
    ZaakRel zaakRel = new ZaakRel();
    ZaakRelPK id = new ZaakRelPK();
    id.setZaakId(zaakId);
    id.setZaakIdRel(zaakIdRel);
    zaakRel.setId(id);
    zaakRel.setzType(BigDecimal.valueOf(1));
    zaakRel.setzTypeRel(BigDecimal.valueOf(1));
    em.persist(zaakRel);
  }

  private void addPresentievraag(EntityManager em, String zaakId, LocalDate localDate) {
    PresVraag pv = new PresVraag();
    pv.setLocation(em.find(Location.class, 0L));
    pv.setZaakId(zaakId);
    pv.setdIn(toBigDecimal(localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
    pv.settIn(toBigDecimal(new DateTime().getLongTime()));
    em.persist(pv);
  }
}
