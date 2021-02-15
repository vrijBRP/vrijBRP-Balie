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

package nl.procura.gba.web.common.database.checks;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import nl.procura.gba.jpa.personen.db.VogFuncTab;
import nl.procura.gba.web.common.database.DBCheckTemplateLb;

import liquibase.database.Database;

/**
 * Check waarbij de ontbrekende COVOG tabellen worden toegevoegd
 * <p>
 * Functieaspecten
 */
public class DBCheckPost3 extends DBCheckTemplateLb {

  public DBCheckPost3(EntityManager entityManager, Database database, String type) {
    super(entityManager, database, type, "ontbrekende covog tabellen (functies)");
  }

  @Override
  public void init() {

    add("-1", "", "", "-1", "-1");
    add("112", "12", "Met gevoelige informatie omgaan", "20030701", "20111130");
    add("113", "13", "Kennis dragen van veiligheidssystemen, controlemechanismen en verificatieprocessen",
        "20030701", "-1");
    add("121", "21", "Met contante en girale waarden omgaan", "20030701", "20111130");
    add("122", "22", "Budgetbevoegdheid hebben", "20030701", "-1");
    add("134", "34",
        "Het samenstellen, bewerken en het vervaardigen van goederen/ producten/ grondstoffen (onder meer t.b.v. medicijnen, voeding en edelmetalen)",
        "20030701", "20111130");
    add("135", "35", "Het uitschrijven van recepten (medicinaal gebruik)", "20030701", "20111130");
    add("136", "36", "Het bewaken van productieprocessen", "20030701", "-1");
    add("141", "41", "Het verlenen van diensten (advies, beveiliging, schoonmaak, catering, etc.)", "20030701",
        "20111130");
    add("163", "63", "(Rijdend) vervoer waarbij personen worden vervoerd", "20030701", "-1");
    add("184", "84",
        "Belast zijn met de zorg voor minderjarigen en/of de zorg voor personen die in een (tijdelijke) afhankelijkheidssituatie verkeren",
        "20030701", "20111130");

    // oud
    add("111", "11",
        "Bevoegd zijn om systemen te raadplegen en/of te bewerken waarin vertrouwelijke gegevens zijn opgeslagen",
        "20030701", "20110430");
    add("114", "14", "Toegang hebben tot besturingssystemen van programma's", "20030701", "20110731");
    add("131", "31", "Het verschaffen, aanschaffen en beheren van goederen en producten", "20030701", "20110731");
    add("132", "32", "Het laden, lossen, inpakken en opslaan van goederen en producten", "20030701", "20110731");
    add("133", "33", "Het verkopen van goederen en producten", "20030701", "20110731");
    add("142", "42", "Bevoegd zijn om besluiten te nemen tot het verlenen en/of inhuren van diensten", "20030701",
        "20110731");
    add("153", "53", "Beslissen over offertes (het voeren van onderhandelingen en het afsluiten van contracten)",
        "20030701", "20110430");
    add("161", "61",
        "Het instellen/ monteren/ repareren/ onderhouden/ ombouwen/ bedienen van (productie)machines en/of apparaten, voertuigen en/of (lucht)vaartuigen",
        "20030701", "20110430");
    add("162", "62",
        "(Rijdend) vervoer waarbij goederen, producten, post en pakketten worden getransporteerd en/of bezorgd",
        "20030701", "20110430");
    add("151", "51", "Contact hebben met leveranciers", "20030701", "20110731");
    add("152", "52", "Aanbestedingen doen", "20030701", "20110731");
    add("171", "71", "Personen die vanuit hun functie mensen en/of organisatie aansturen (leidinggevenden)",
        "20030701", "20110430");
    add("181", "81", "Belast zijn met de zorg voor het welzijn en de veiligheid van mensen en dieren", "20030701",
        "20110731");
    add("182", "82",
        "Een één op één relatie hebben (verschil in macht), waarbij er sprake is van een (tijdelijke) afhankelijkheid",
        "20030701", "20110731");
    add("183", "83",
        "Het voorhanden hebben van stoffen, objecten, voorwerpen en dergelijke die, bij oneigenlijk of onjuist gebruik, een risico vormen voor het welzijn en de veiligheid van mensen en/of dieren",
        "20030701", "20110731");

    // nieuw
    add("200", "11",
        "Bevoegdheid hebben tot het raadplegen en/of bewerken van systemen waarin vertrouwelijke gegevens zijn opgeslagen",
        "20110501", "20111130");
    add("201", "62",
        "(Rijdend) vervoer waarbij goederen, producten, post en pakketten worden getransporteerd en/of bezorgd anders dan door middel van een heftruck",
        "20110501", "20111201");
    add("202", "61",
        "Het onderhouden/ ombouwen/ bedienen van (productie)machines en/of apparaten, voertuigen en/of luchtvaartuigen",
        "20110501", "-1");
    add("203", "53",
        "Beslissen over offertes (het voeren van onderhandelingen en het afsluiten van contracten) en het doen van aanbestedingen",
        "20110501", "-1");
    add("204", "71", "Personen die vanuit hun functie mensen en/of organisatie aansturen (of een deel daarvan)",
        "20110501", "20111201");
    add("205", "37", "Het beschikken over goederen en producten", "20110501", "20111130");
    add("206", "38",
        "Het voorhanden hebben van stoffen, objecten en voorwerpen e.d., die bij oneigenlijk of onjuist gebruik, een risico vormen voor mens (en dier)",
        "20110501", "-1");
    add("207", "43", "Het verlenen van diensten in de persoonlijke leefomgeving", "20110501", "-1");

    // 01-12-2011
    add("208", "11", "Bevoegdheid hebben tot het raadplegen en/of bewerken van systemen", "20111201", "-1");
    add("209", "12", "Met gevoelige/vertrouwelijke informatie omgaan", "20111201", "-1");
    add("211", "21", "Met contante en/of girale gelden en/of (digitale) waardepapieren omgaan", "20111201", "-1");
    add("212", "34",
        "Het samenstellen, bewerken en het vervaardigen van goederen/ producten/ grondstoffen (onder meer t.b.v. medicijnen, voeding en edelmetalen)",
        "20111201", "20120301");
    add("213", "35", "Het uitschrijven van recepten (medicinaal gebruik)", "20111201", "20120301");
    add("214", "37", "Het beschikken over goederen", "20111201", "-1");
    add("215", "41", "Het verlenen van diensten (advies, beveiliging, schoonmaak, catering, onderhoud, etc.)",
        "20111201", "-1");
    add("216", "62",
        "(Rijdend) vervoer waarbij goederen, producten, post en pakketten worden getransporteerd en/of bezorgd, anders dan het intern transport binnen een bedrijf",
        "20111201", "-1");
    add("217", "71", "Personen die vanuit hun functie mensen en/of een organisatie (of een deel daarvan) aansturen",
        "20111201", "-1");
    add("218", "84", "Belast zijn met de zorg voor minderjarigen", "20111201", "-1");
    add("219", "85", "Belast zijn met de zorg voor (hulpbehoevende) personen, zoals ouderen en gehandicapten",
        "20111201", "-1");
    add("220", "86", "Kinderopvang", "20120814", "-1");

    // 01-06-2015
    add("221", "15", "Burgerluchtvaart (alleen aanvinken in combinatie met andere functieaspecten)", "20150601",
        "20160701");
  }

  private void add(String code, String vogFuncTab, String oms, String dIn, String dEnd) {

    TypedQuery<VogFuncTab> query = getEntityManager().createQuery(
        "select d from VogFuncTab d where d.cVogFuncTab = :code", VogFuncTab.class);
    query.setParameter("code", along(code));

    for (VogFuncTab rd : query.getResultList()) {
      rd.setCVogFuncTab(along(code));
      rd.setVogFuncTab(vogFuncTab);
      rd.setOms(oms);
      rd.setDIn(toBigDecimal(dIn));
      rd.setDEnd(toBigDecimal(dEnd));
      getEntityManager().merge(rd);
      return;
    }

    VogFuncTab rd = new VogFuncTab();
    rd.setCVogFuncTab(along(code));
    rd.setVogFuncTab(vogFuncTab);
    rd.setOms(oms);
    rd.setDIn(toBigDecimal(dIn));
    rd.setDEnd(toBigDecimal(dEnd));
    getEntityManager().merge(rd);
    count(1);
  }
}
