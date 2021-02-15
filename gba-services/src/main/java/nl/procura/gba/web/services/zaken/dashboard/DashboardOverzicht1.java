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

package nl.procura.gba.web.services.zaken.dashboard;

import nl.procura.gba.jpa.personen.dao.views.OfficialDashboardDao;

public class DashboardOverzicht1 extends DashboardOverzicht {

  public DashboardOverzicht1(String periode, long datumIngang, long datumEinde, String bron, String leverancier) {
    super(periode, datumIngang, datumEinde, bron, leverancier);

    add(getTelling11());
    add(getTelling12());
    add(getTelling13());
    add(getTelling14());
    add(getTelling21());
    add(getTelling22());
    add(getTelling31());
    add(getTelling311());
    add(getTelling32());
    add(getTelling321());
    add(getTelling33());
    add(getTelling34());
    add(getTelling341());
    add(getTelling35());
    add(getTelling351());
    add(getTelling41());
    add(getTelling411());
    add(getTelling42());
    add(getTelling43());
    add(getTelling431());
    add(getTelling51());
    add(getTelling61());
    add(getTelling62());
    add(getTelling63());
    add(getTelling71());
    add(getTelling72());
    add(getTelling73());
    add(getTelling102());
    add(getTelling103());
    add(getTelling104());
    add(getTelling105());
  }

  private DashboardTelling getTelling102() {
    return new DashboardTelling("10.2", "Aantal aangevraagde onderzoeken",
        OfficialDashboardDao.getOnderzoeken102(getPeriode()));
  }

  private DashboardTelling getTelling103() {
    return new DashboardTelling("10.3", "Aantal uitgevoerde onderzoeken",
        OfficialDashboardDao.getOnderzoeken103(getPeriode()));
  }

  private DashboardTelling getTelling104() {
    return new DashboardLegeTelling("10.4", "Aantal ontvangen terugmeldingen BRP");
  }

  private DashboardTelling getTelling105() {
    return new DashboardTelling("10.5", "Aantal verstuurde terugmeldingen BRP",
        OfficialDashboardDao.getTerugmeldingen105(getPeriode()));
  }

  private DashboardTelling getTelling11() {
    return new DashboardLegeTelling("1.1", "Uittreksel Burgerlijke Stand");
  }

  private DashboardTelling getTelling12() {
    return new DashboardTelling("1.2", "Geboorteaangifte", OfficialDashboardDao.getGeboorten12(getPeriode()));
  }

  private DashboardTelling getTelling13() {
    return new DashboardTelling("1.3", "Erkenning kind", OfficialDashboardDao.getErkenningen13(getPeriode()));
  }

  private DashboardTelling getTelling14() {
    return new DashboardTelling("1.4", "Overlijdensaangifte", OfficialDashboardDao.getOverlijden14(getPeriode()));
  }

  private DashboardTelling getTelling21() {
    return new DashboardTelling("2.1", "Huwelijk en/of partnerschap",
        OfficialDashboardDao.getHuwelijken21(getPeriode()));
  }

  private DashboardTelling getTelling22() {
    return new DashboardTelling("2.2", "Ontbinden huwelijk en/of partnerschap",
        OfficialDashboardDao.getOntbindingen22(getPeriode()));
  }

  private DashboardTelling getTelling31() {
    return new DashboardTelling("3.1", "Uittreksel BRP",
        OfficialDashboardDao.getUittreksels31(false, getPeriode()));
  }

  private DashboardTelling getTelling311() {
    return new DashboardTelling("3.1.1", "Uittreksel BRP (Digitale aanvragen)",
        OfficialDashboardDao.getUittreksels31(true, getPeriode()));
  }

  private DashboardTelling getTelling32() {
    return new DashboardTelling("3.2", "Bewijs van in leven zijn",
        OfficialDashboardDao.getBewijsInLeven32(false, getPeriode()));
  }

  private DashboardTelling getTelling321() {
    return new DashboardTelling("3.2.1", "Bewijs van in leven zijn (Digitale aanvragen)",
        OfficialDashboardDao.getBewijsInLeven32(true, getPeriode()));
  }

  private DashboardTelling getTelling33() {
    return new DashboardTelling("3.3", "Verklaring omtrent gedrag", OfficialDashboardDao.getVog33(getPeriode()));
  }

  private DashboardTelling getTelling34() {
    return new DashboardTelling("3.4", "Naamgebruik", OfficialDashboardDao.getNaamgebruik34(false, getPeriode()));
  }

  private DashboardTelling getTelling341() {
    return new DashboardTelling("3.4.1", "Naamgebruik (Digitale aanvragen)",
        OfficialDashboardDao.getNaamgebruik34(true, getPeriode()));
  }

  private DashboardTelling getTelling35() {
    return new DashboardTelling("3.5", "Geheimhoudingsverzoek",
        OfficialDashboardDao.getGeheimhouding35(false, getPeriode()));
  }

  private DashboardTelling getTelling351() {
    return new DashboardTelling("3.5.1", "Geheimhoudingsverzoek (Digitale aanvragen)",
        OfficialDashboardDao.getGeheimhouding35(true, getPeriode()));
  }

  private DashboardTelling getTelling41() {
    return new DashboardTelling("4.1", "Verhuizing binnen Nederland",
        OfficialDashboardDao.getVerhuizingen41(false, getPeriode()));
  }

  private DashboardTelling getTelling411() {
    return new DashboardTelling("4.1.1", "Verhuizing binnen Nederland (Digitale aanvragen)",
        OfficialDashboardDao.getVerhuizingen41(true, getPeriode()));
  }

  private DashboardTelling getTelling42() {
    return new DashboardTelling("4.2", "Vestiging vanuit het buitenland",
        OfficialDashboardDao.getVerhuizingen42(getPeriode()));
  }

  private DashboardTelling getTelling43() {
    return new DashboardTelling("4.3", "Vertrek naar het buitenland",
        OfficialDashboardDao.getVerhuizingen43(false, getPeriode()));
  }

  private DashboardTelling getTelling431() {
    return new DashboardTelling("4.3.1", "Vertrek naar het buitenland (Digitale aanvragen)",
        OfficialDashboardDao.getVerhuizingen43(true, getPeriode()));
  }

  private DashboardTelling getTelling51() {
    return new DashboardLegeTelling("5.1", "Aanvraag Nederlanderschap");
  }

  private DashboardTelling getTelling61() {
    return new DashboardTelling("6.1", "Reisdocument aanvragen",
        OfficialDashboardDao.getReisdocumenten61(getPeriode()));
  }

  private DashboardTelling getTelling62() {
    return new DashboardTelling("6.2", "Reisdocument afhalen",
        OfficialDashboardDao.getReisdocumenten62(getPeriode()));
  }

  private DashboardTelling getTelling63() {
    return new DashboardTelling("6.3", "Vermissing reisdocument",
        OfficialDashboardDao.getReisdocumenten63(getPeriode()));
  }

  private DashboardTelling getTelling71() {
    return new DashboardTelling("7.1", "Rijbewijs aanvragen", OfficialDashboardDao.getRijbewijzen71(getPeriode()));
  }

  private DashboardTelling getTelling72() {
    return new DashboardTelling("7.2", "Rijbewijs afhalen", OfficialDashboardDao.getRijbewijzen72(getPeriode()));
  }

  private DashboardTelling getTelling73() {
    return new DashboardTelling("7.3", "Vermissing rijbewijs", OfficialDashboardDao.getRijbewijzen73(getPeriode()));
  }
}
