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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType;
import nl.procura.gba.jpa.personen.db.*;
import nl.procura.gba.jpa.personen.utils.GbaJpa;
import nl.procura.gba.web.services.TemporaryDatabase;
import nl.procura.gba.web.services.zaken.opschonen.VerwijderZakenOverzicht;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisType;

import lombok.SneakyThrows;

public class ZakenViewTest {

  @Before
  public void setUp() {
    TemporaryDatabase.ensureCleanMockDatabase();
  }

  @Test
  @SneakyThrows
  public void mustContainZaken() {
    EntityManager em = GbaJpa.getManager();

    em.getTransaction().begin();
    assertCount(em, 0L, "zaken_view");

    addUittreksels(em);
    addVerstrekkingsbeperkingen(em);
    addNaamgebruik(em);
    addVerhuizingen(em);
    addVog(em);
    show(em);
    addGpk(em);
    addReisdocument(em);
    addInhouding(em);
    addRijbewijs(em);
    addTerugmelding(em);
    addGeboorte(em);
    addErkenning(em);
    addHuwelijkGPS(em);
    addNaamskeuze(em);
    addOverlijdenGemeente(em);
    addLijkvinding(em);
    addOverlijdenBuitenland(em);
    addLevenloos(em);
    addIndicatie(em);
    addCorrespondentie(em);
    addGegevensverstrekking(em);
    addOmzettingGPS(em);
    addOntbindingHuwGPS(em);
    addInbox(em);
    addOnderzoek(em);
    addEersteInschrijving(em);
    addRisicoAnalyse(em);
    addPLMutatie(em);

    // Total
    assertCount(em, 37L, "zaken_view");

    em.getTransaction().rollback();
  }

  private void addUittreksels(EntityManager em) {
    assertZakenCount(0L, VerwijderZaakType.UITTREKSEL);
    addUittreksel(em, "pl", "1", LocalDate.now());
    addUittreksel(em, "pl", "2", toDate(VerwijderZaakType.UITTREKSEL)); // deletable
    assertZakenCount(1L, VerwijderZaakType.UITTREKSEL);
    assertZaak("2", VerwijderZaakType.UITTREKSEL);

    assertZakenCount(0L, VerwijderZaakType.FORMULIER);
    addUittreksel(em, "abc", "3", LocalDate.now());
    addUittreksel(em, "abc", "4", toDate(VerwijderZaakType.FORMULIER)); // deletable

    assertCount(em, 4L, "uitt_aanvr");
    assertZakenCount(1L, VerwijderZaakType.FORMULIER);
    assertZaak("4", VerwijderZaakType.FORMULIER);
  }

  private void addVerstrekkingsbeperkingen(EntityManager em) {
    assertZakenCount(0L, VerwijderZaakType.VERSTREKKINGSBEPERKING);

    LocalDate historic = toDate(VerwijderZaakType.VERSTREKKINGSBEPERKING);
    addVerstrekkingsbeperking(em, "20", LocalDate.now(), LocalDate.now());
    addVerstrekkingsbeperking(em, "21", LocalDate.now(), null);
    addVerstrekkingsbeperking(em, "22", historic, historic); // deletable
    assertZakenCount(1L, VerwijderZaakType.VERSTREKKINGSBEPERKING);
    addVerstrekkingsbeperking(em, "23", null, historic); // deletable

    assertCount(em, 4L, "geheimhouding");
    assertZakenCount(2L, VerwijderZaakType.VERSTREKKINGSBEPERKING);
    assertZaak("22", VerwijderZaakType.VERSTREKKINGSBEPERKING);
  }

  private void addNaamgebruik(EntityManager em) {
    assertZakenCount(0L, VerwijderZaakType.NAAMGEBRUIK);

    LocalDate historic = toDate(VerwijderZaakType.NAAMGEBRUIK);
    Naamgebruik naamgebruik = new Naamgebruik();
    naamgebruik.setZaakId("30");
    naamgebruik.setDIn(toDate(historic));
    naamgebruik.setDWijz(toDate(historic));
    em.persist(naamgebruik);

    assertCount(em, 1L, "naamgebruik");
    assertZakenCount(1L, VerwijderZaakType.NAAMGEBRUIK);
    assertZaak("30", VerwijderZaakType.NAAMGEBRUIK);
  }

  private void addVerhuizingen(EntityManager em) {
    LocalDate verhuizing1 = toDate(VerwijderZaakType.BINNENVERHUIZING);
    LocalDate verhuizing2 = toDate(VerwijderZaakType.BUITENVERHUIZING);
    LocalDate verhuizing3 = toDate(VerwijderZaakType.EMIGRATIE);
    LocalDate verhuizing4 = toDate(VerwijderZaakType.HERVESTIGING);

    assertZakenCount(0L, VerwijderZaakType.BINNENVERHUIZING);
    addVerhuizing(em, "40", verhuizing1, verhuizing1, VerhuisType.BINNENGEMEENTELIJK);
    assertZakenCount(1L, VerwijderZaakType.BINNENVERHUIZING);

    assertZakenCount(0L, VerwijderZaakType.BUITENVERHUIZING);
    addVerhuizing(em, "41", verhuizing2, verhuizing2, VerhuisType.INTERGEMEENTELIJK);
    assertZakenCount(1L, VerwijderZaakType.BINNENVERHUIZING);

    assertZakenCount(0L, VerwijderZaakType.EMIGRATIE);
    addVerhuizing(em, "42", verhuizing3, verhuizing3, VerhuisType.EMIGRATIE);
    assertZakenCount(1L, VerwijderZaakType.EMIGRATIE);

    assertZakenCount(0L, VerwijderZaakType.HERVESTIGING);
    addVerhuizing(em, "43", verhuizing4, verhuizing4, VerhuisType.HERVESTIGING);
    assertZakenCount(1L, VerwijderZaakType.HERVESTIGING);

    assertCount(em, 4L, "bvh_park");
    assertZaak("40", VerwijderZaakType.BINNENVERHUIZING);
    assertZaak("41", VerwijderZaakType.BUITENVERHUIZING);
    assertZaak("42", VerwijderZaakType.EMIGRATIE);
    assertZaak("43", VerwijderZaakType.HERVESTIGING);
  }

  private void addVog(EntityManager em) {
    assertZakenCount(0L, VerwijderZaakType.VOG);

    VogAanvr vogAanvr = new VogAanvr();
    vogAanvr.setZaakId("50");
    vogAanvr.setDAanvr(toDate(toDate(VerwijderZaakType.VOG)));
    em.persist(vogAanvr);

    assertCount(em, 1L, "vog_aanvr");
    assertZakenCount(1L, VerwijderZaakType.VOG);
    assertZaak("50", VerwijderZaakType.VOG);
  }

  private void addGpk(EntityManager em) {
    assertZakenCount(0L, VerwijderZaakType.GPK);
    Gpk gpk = new Gpk();
    gpk.setZaakId("60");
    gpk.setDAanvr(toDate(toDate(VerwijderZaakType.GPK)));
    em.persist(gpk);
    assertCount(em, 1L, "gpk");
    assertZakenCount(1L, VerwijderZaakType.GPK);
    assertZaak("60", VerwijderZaakType.GPK);
  }

  private void addReisdocument(EntityManager em) {
    assertZakenCount(0L, VerwijderZaakType.REISDOCUMENT);
    Rdm01 rdm01 = new Rdm01();
    rdm01.setZaakId("70");
    rdm01.setDAanvr(toDate(toDate(VerwijderZaakType.REISDOCUMENT)));
    em.persist(rdm01);

    assertCount(em, 1L, "rdm01");
    assertZakenCount(1L, VerwijderZaakType.REISDOCUMENT);
    assertZaak("70", VerwijderZaakType.REISDOCUMENT);
  }

  private void addInhouding(EntityManager em) {
    assertZakenCount(0L, VerwijderZaakType.INHOUDING_VERMISSING);
    DocInh docInh = new DocInh();
    DocInhPK id = new DocInhPK();
    id.setAnr("123");
    id.setNrNlDoc("456");
    docInh.setVersionTs(1L);
    docInh.setId(id);
    docInh.setZaakId("80");
    docInh.setDInneming(toDate(toDate(VerwijderZaakType.INHOUDING_VERMISSING)).longValue());
    em.persist(docInh);

    assertCount(em, 1L, "doc_inh");
    assertZakenCount(1L, VerwijderZaakType.INHOUDING_VERMISSING);
    assertZaak("80", VerwijderZaakType.INHOUDING_VERMISSING);
  }

  private void addRijbewijs(EntityManager em) {
    assertZakenCount(0L, VerwijderZaakType.RIJBEWIJS);
    Nrd nrd = new Nrd();
    nrd.setZaakId("90");
    nrd.setDAanvr(toDate(toDate(VerwijderZaakType.RIJBEWIJS)));
    em.persist(nrd);

    assertCount(em, 1L, "nrd");
    assertZakenCount(1L, VerwijderZaakType.RIJBEWIJS);
    assertZaak("90", VerwijderZaakType.RIJBEWIJS);
  }

  private void addTerugmelding(EntityManager em) {
    assertZakenCount(0L, VerwijderZaakType.TERUGMELDING);
    Terugmelding tmv = new Terugmelding();
    tmv.setZaakId("100");
    tmv.setDIn(toDate(toDate(VerwijderZaakType.TERUGMELDING)));
    tmv.setLocation(em.find(Location.class, 0L));
    tmv.setUsrToev(em.find(Usr.class, 0L));
    tmv.setUsrAfh(em.find(Usr.class, 0L));
    em.persist(tmv);

    assertCount(em, 1L, "terugmelding");
    assertZakenCount(1L, VerwijderZaakType.TERUGMELDING);
    assertZaak("100", VerwijderZaakType.TERUGMELDING);
  }

  private void addGeboorte(EntityManager em) {
    addDossier(em, "110", VerwijderZaakType.GEBOORTE, 1L);
  }

  private void addErkenning(EntityManager em) {
    addDossier(em, "120", VerwijderZaakType.ERKENNING, 2L);
  }

  private void addHuwelijkGPS(EntityManager em) {
    addDossier(em, "130", VerwijderZaakType.HUW_GPS, 3L);
  }

  private void addNaamskeuze(EntityManager em) {
    addDossier(em, "140", VerwijderZaakType.NAAMSKEUZE, 4L);
  }

  private void addOverlijdenGemeente(EntityManager em) {
    addDossier(em, "150", VerwijderZaakType.OVERLIJDEN_GEMEENTE, 5L);
  }

  private void addLijkvinding(EntityManager em) {
    addDossier(em, "160", VerwijderZaakType.LIJKVINDING, 6L);
  }

  private void addOverlijdenBuitenland(EntityManager em) {
    addDossier(em, "170", VerwijderZaakType.OVERLIJDEN_BUITENLAND, 7L);
  }

  private void addLevenloos(EntityManager em) {
    addDossier(em, "180", VerwijderZaakType.LEVENLOOS, 8L);
  }

  private void addIndicatie(EntityManager em) {
    assertZakenCount(0L, VerwijderZaakType.INDICATIE);
    Indicatie indicatie = new Indicatie();
    indicatie.setZaakId("190");
    indicatie.setDIn(toDate(toDate(VerwijderZaakType.INDICATIE)));
    indicatie.setLocation(em.find(Location.class, 0L));
    indicatie.setUsr(em.find(Usr.class, 0L));
    em.persist(indicatie);

    assertCount(em, 1L, "indicatie");
    assertZakenCount(1L, VerwijderZaakType.INDICATIE);
    assertZaak("190", VerwijderZaakType.INDICATIE);
  }

  private void addCorrespondentie(EntityManager em) {
    assertZakenCount(0L, VerwijderZaakType.CORRESPONDENTIE);

    Correspondentie correspondentie = new Correspondentie();
    correspondentie.setZaakId("200");
    correspondentie.setDIn(toDate(toDate(VerwijderZaakType.CORRESPONDENTIE)));
    correspondentie.setLocation(em.find(Location.class, 0L));
    correspondentie.setUsr(em.find(Usr.class, 0L));
    em.persist(correspondentie);

    assertCount(em, 1L, "correspondentie");
    assertZakenCount(1L, VerwijderZaakType.CORRESPONDENTIE);
    assertZaak("200", VerwijderZaakType.CORRESPONDENTIE);
  }

  private void addGegevensverstrekking(EntityManager em) {
    assertZakenCount(0L, VerwijderZaakType.GEGEVENSVERSTREKKING);
    Gv gv = new Gv();
    gv.setZaakId("210");
    gv.setDIn(toDate(toDate(VerwijderZaakType.GEGEVENSVERSTREKKING)));
    gv.setLocation(em.find(Location.class, 0L));
    gv.setUsr(em.find(Usr.class, 0L));
    em.persist(gv);

    assertCount(em, 1L, "gv");
    assertZakenCount(1L, VerwijderZaakType.GEGEVENSVERSTREKKING);
    assertZaak("210", VerwijderZaakType.GEGEVENSVERSTREKKING);
  }

  private void addOmzettingGPS(EntityManager em) {
    addDossier(em, "220", VerwijderZaakType.OMZETTING_GPS, 9L);
  }

  private void addOntbindingHuwGPS(EntityManager em) {
    addDossier(em, "230", VerwijderZaakType.ONTBINDING_HUW_GPS, 10L);
  }

  private void addInbox(EntityManager em) {
    assertZakenCount(0L, VerwijderZaakType.INBOX);
    Inbox inbox = new Inbox();
    inbox.setZaakId("240");
    inbox.setdInvoer(toDate(toDate(VerwijderZaakType.INBOX)));
    inbox.setdIngang(toDate(toDate(VerwijderZaakType.INBOX)));
    inbox.setLocation(em.find(Location.class, 0L));
    inbox.setUsr(em.find(Usr.class, 0L));
    em.persist(inbox);

    assertCount(em, 1L, "inbox");
    assertZakenCount(1L, VerwijderZaakType.INBOX);
    assertZaak("240", VerwijderZaakType.INBOX);
  }

  private void addOnderzoek(EntityManager em) {
    addDossier(em, "250", VerwijderZaakType.ONDERZOEK, 11L);
  }

  private void addEersteInschrijving(EntityManager em) {
    addDossier(em, "260", VerwijderZaakType.EERSTE_INSCHRIJVING, 12L);
  }

  private void addRisicoAnalyse(EntityManager em) {
    addDossier(em, "270", VerwijderZaakType.RISICOANALYSE, 13L);
  }

  private void addPLMutatie(EntityManager em) {
    assertZakenCount(0L, VerwijderZaakType.PL_MUTATIE);
    PlMut plMut = new PlMut();
    plMut.setZaakId("280");
    plMut.setDIn(toDate(toDate(VerwijderZaakType.PL_MUTATIE)));
    plMut.setLocation(em.find(Location.class, 0L));
    plMut.setUsr(em.find(Usr.class, 0L));
    em.persist(plMut);

    assertCount(em, 1L, "pl_mut");
    assertZakenCount(1L, VerwijderZaakType.PL_MUTATIE);
    assertZaak("280", VerwijderZaakType.PL_MUTATIE);
  }

  private void addDossier(EntityManager em, String zaakId, VerwijderZaakType type, long expectedCount) {
    assertZakenCount(0L, type);
    Doss doss = new Doss();
    doss.setZaakId(zaakId);
    doss.setTypeDoss(BigDecimal.valueOf(type.getZaakType().getCode()));
    doss.setDAanvr(toDate(toDate(type))); // Invoer
    doss.setDIn(toDate(toDate(type))); // Ingang
    em.persist(doss);

    assertCount(em, expectedCount, "doss");
    assertZakenCount(1L, type);
    assertZaak(zaakId, type);
  }

  private LocalDate toDate(VerwijderZaakType type) {
    return LocalDate.now().minusYears(new VerwijderZakenOverzicht()
        .getActie(type).getVerwijderActie().getBewaarTermijnInJaren());
  }

  private void assertZakenCount(long expectedCount, VerwijderZaakType type) {
    Assert.assertEquals(expectedCount, new VerwijderZakenOverzicht()
        .getActie(type).getVerwijderActie().getAantal());
  }

  private void assertZaak(String zaakId, VerwijderZaakType type) {
    Assert.assertEquals(zaakId, new VerwijderZakenOverzicht()
        .getActie(type).getVerwijderActie().getResultaten(50).get(0).getZaakId());
  }

  private static void addUittreksel(EntityManager em, String documentType, String zaakId, LocalDate localDate) {
    Document document = new Document();
    document.setType(documentType);
    em.persist(document);

    UittAanvr uittAanvr = new UittAanvr();
    uittAanvr.setZaakId(zaakId);
    uittAanvr.setDAanvr(toDate(localDate));
    uittAanvr.setDocument(document);

    em.persist(uittAanvr);
  }

  private static void addVerstrekkingsbeperking(EntityManager em, String zaakId, LocalDate dOpname, LocalDate dIngang) {
    Geheimhouding geheimhouding = new Geheimhouding();
    geheimhouding.setZaakId(zaakId);
    geheimhouding.setDIn(toDate(dOpname));
    geheimhouding.setDWijz(toDate(dIngang));
    em.persist(geheimhouding);
  }

  private static void addVerhuizing(EntityManager em, String zaakId, LocalDate dOpname, LocalDate dIngang,
      VerhuisType type) {
    BvhPark bvhPark = new BvhPark();
    bvhPark.setZaakId(zaakId);
    bvhPark.setVerhuisType(BigDecimal.valueOf(type.getCode()));
    bvhPark.setDOpn(toDate(dOpname));
    bvhPark.setDAanv(toDate(dIngang));
    em.persist(bvhPark);
  }

  private void show(EntityManager em) {
    for (Object record : em.createNativeQuery("select * from zaken_view").getResultList()) {
      Object[] fields = (Object[]) record;
      System.out.println(Arrays.asList(fields));
    }
  }

  private void assertCount(EntityManager em, long count, String table) {
    Assert.assertEquals(count, em.createNativeQuery("select count(*) from " + table).getSingleResult());
  }

  private static BigDecimal toDate(LocalDate date) {
    if (date != null) {
      return BigDecimal.valueOf(Long.parseLong(date.format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
    }
    return BigDecimal.valueOf(-1L);
  }
}
