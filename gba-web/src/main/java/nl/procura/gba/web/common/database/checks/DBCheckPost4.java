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

import nl.procura.gba.jpa.personen.db.VogProfTab;
import nl.procura.gba.web.common.database.DBCheckTemplateLb;

import liquibase.database.Database;

/**
 * Check waarbij de ontbrekende COVOG tabellen worden toegevoegd
 * <p>
 * Screeningsprofielen
 */
public class DBCheckPost4 extends DBCheckTemplateLb {

  public DBCheckPost4(EntityManager entityManager, Database database, String type) {
    super(entityManager, database, type, "ontbrekende covog tabellen (profielen)");
  }

  @Override
  public void init() {

    add("-1", "-1", "", "-1", "-1");
    add("124", "24", "Integriteitsverklaring Beroepsvervoer", "20090501", "20120101");
    add("120", "20", "Ondernemersvergunning voor beroepsgoederenvervoer", "20031218", "20090501");
    add("130", "30", "Burgerluchtvaart", "20031218", "20080701");
    add("135", "35", "Emigratie, visum", "20031218", "20090501");
    add("155", "55", "Juridische dienstverlening", "20031218", "-1");
    add("185", "85", "Lidmaatschap schietvereniging", "20080701", "-1");
    add("195", "95", "Financiële dienstverlening", "20080701", "-1");

    // oud
    add("140", "40", "(Vakantie)gastgezinnen, pleeggezinnen en adoptie", "20031218", "20110430");
    add("150", "50", "Horeca; leidinggevende, bedrijfsleider en beheerder", "20031218", "20110430");
    add("125", "25", "Buitengewoon opsporingsambtenaar", "20031218", "20110430");
    add("145", "45",
        "Gezondheidszorg en welzijn van mens en dier; arts, verpleegkundige, tandarts, verloskundige, dierenarts, apotheker, paramedicus en thuishulp",
        "20031218", "20110430");
    add("160", "60",
        "Onderwijssector; directeur, (con)rector, onderwijzend personeel, (administratief) ondersteunend personeel, conciërge en schoonmaker",
        "20031218", "20110430");
    add("165", "65", "De aanvraag voor een chauffeurskaart als bedoeld in het Besluit personenvervoer 2000",
        "20031218", "20110430");
    add("170", "70",
        "De aanvraag voor een ondernemersvergunning voor taxivervoer als bedoeld in de Wet personenvervoer 2000",
        "20031218", "20110430");
    add("174", "74",
        "De aanvraag voor een vergunning voor het uitoefenen van het beroep van ondernemer in het personenvervoer met autobussen en touringcars",
        "20090501", "20110430");
    add("175", "75",
        "(Gezins)voogd bij voogdij-instellingen, reclasseringswerker, maatschappelijk werker en raadsonderzoeker",
        "20031218", "20110430");
    add("180", "80",
        "Beëdigd tolken/vertalers voor de inschrijving in het wettelijk register te Den Bosch (Belanghebbende is uitsluitend de Raad voor Rechtsbijstand te Den Bosch)",
        "20080701", "20110430");

    // nieuw
    add("200", "25", "(Buitengewoon) opsporingsambtenaar", "20110501", "-1");
    add("201", "40", "Vakantiegezinnen en adoptie", "20110501", "-1");
    add("202", "45", "Gezondheidszorg en welzijn van mens en dier", "20110501", "-1");
    add("203", "50", "Exploitatievergunning", "20110501", "-1");
    add("204", "60", "Onderwijs", "20110501", "-1");
    add("205", "65", "Taxibranche; chauffeurskaart", "20110501", "-1");
    add("206", "70", "Taxibranche; ondernemersvergunning", "20110501", "-1");
    add("207", "74", "Busondernemer", "20110501", "20120229");
    add("208", "75",
        "(Gezins)voogd bij voogdijinstellingen, reclasseringswerker, raadsonderzoeker en maatschappelijk werker",
        "20110501", "-1");
    add("209", "80", "Beëdigd tolken/vertalers", "20110501", "-1");
    add("210", "01", "Politieke ambtsdragers", "20110501", "-1");
    add("211", "06", "Visum en emigratie", "20110501", "-1");
    add("212", "96", "Onbekende functie", "20131201", "-1");
    add("213", "18", "Huisvestingsvergunning", "20170101", "-1");
  }

  private void add(String code, String vogProfTab, String oms, String dIn, String dEnd) {

    TypedQuery<VogProfTab> query = getEntityManager().createQuery(
        "select d from VogProfTab d where d.cVogProfTab = :code", VogProfTab.class);
    query.setParameter("code", along(code));

    for (VogProfTab rd : query.getResultList()) {
      rd.setCVogProfTab(along(code));
      rd.setVogProfTab(vogProfTab);
      rd.setOms(oms);
      rd.setDIn(toBigDecimal(dIn));
      rd.setDEnd(toBigDecimal(dEnd));
      getEntityManager().merge(rd);
      return;
    }

    VogProfTab rd = new VogProfTab();
    rd.setCVogProfTab(along(code));
    rd.setVogProfTab(vogProfTab);
    rd.setOms(oms);
    rd.setDIn(toBigDecimal(dIn));
    rd.setDEnd(toBigDecimal(dEnd));
    getEntityManager().merge(rd);
    count(1);
  }
}
