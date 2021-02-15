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

import nl.procura.gba.jpa.personen.dao.views.UnOfficialDashboardDao;

public class DashboardOverzicht2 extends DashboardOverzicht {

  public DashboardOverzicht2(String periode, long datumIngang, long datumEinde, String bron, String leverancier) {
    super(periode, datumIngang, datumEinde, bron, leverancier);

    add(getTelling121());
    add(getTelling122());
    add(getTelling123());
    add(getTelling124());
    add(getTelling125());
    add(getTelling126());
    add(getTelling127());
    add(getTelling128());
    add(getTelling129());
    add(getTelling131());
    add(getTelling132());
    add(getTelling133());
    add(getTelling141());
    add(getTelling142());
    add(getTelling143());
    add(getTelling144());
    add(getTelling145());
  }

  private DashboardTelling getTelling121() {
    return new DashboardTelling("12.1", "Aantal aanvragen van verstrekkingen",
        UnOfficialDashboardDao.getGv121(getPeriode()));
  }

  private DashboardTelling getTelling122() {
    return new DashboardTelling("12.2", "Aantal verstrekkingen totaal",
        UnOfficialDashboardDao.getGv122(getPeriode()));
  }

  private DashboardTelling getTelling123() {
    return new DashboardTelling("12.3", "Aantal verstrekkingen met als grondslag: Overheidsorgaan art. 3.5",
        UnOfficialDashboardDao.getGv123(getPeriode()));
  }

  private DashboardTelling getTelling124() {
    return new DashboardTelling("12.4", "Aantal verstrekkingen met als grondslag: Derde art. 3.6",
        UnOfficialDashboardDao.getGv124(getPeriode()));
  }

  private DashboardTelling getTelling125() {
    return new DashboardTelling("12.5", "Aantal verstrekkingen met als grondslag: Derde art. 3.9",
        UnOfficialDashboardDao.getGv125(getPeriode()));
  }

  private DashboardTelling getTelling126() {
    return new DashboardTelling("12.6", "Aantal verstrekkingsaanvragen met een belangenafweging",
        UnOfficialDashboardDao.getGv126(getPeriode()));
  }

  private DashboardTelling getTelling127() {
    return new DashboardTelling("12.7", "Aantal verstrekkingen na belangenafweging",
        UnOfficialDashboardDao.getGv127(getPeriode()));
  }

  private DashboardTelling getTelling128() {
    return new DashboardTelling("12.8", "Aantal weigeringen van verstrekkingsaanvragen na belangenafweging",
        UnOfficialDashboardDao.getGv128(getPeriode()));
  }

  private DashboardTelling getTelling129() {
    return new DashboardTelling("12.9", "Aantal directe weigeringen van verstrekkingsaanvragen",
        UnOfficialDashboardDao.getGv129(getPeriode()));
  }

  private DashboardTelling getTelling131() {
    return new DashboardTelling("13.1", "Aantal inschrijvingen als resultaat van adresonderzoek",
        UnOfficialDashboardDao.getAddressInv131(getPeriode()));
  }

  private DashboardTelling getTelling132() {
    return new DashboardTelling("13.2", "Aantal uitschrijvingen als resultaat van adresonderzoek",
        UnOfficialDashboardDao.getAddressInv132(getPeriode()));
  }

  private DashboardTelling getTelling133() {
    return new DashboardTelling("13.3", "Aantal verhuizingen als resultaat van adresonderzoek",
        UnOfficialDashboardDao.getAddressInv133(getPeriode()));
  }

  private DashboardTelling getTelling141() {
    return new DashboardTelling("14.1", "Aantal uitgevoerde risicoanalyses",
        UnOfficialDashboardDao.getRiskAnalysis141(getPeriode()));
  }

  private DashboardTelling getTelling142() {
    return new DashboardTelling("14.2", "Aantal risicoanalyses beneden drempelwaarde",
        UnOfficialDashboardDao.getRiskAnalysis142(getPeriode()));
  }

  private DashboardTelling getTelling143() {
    return new DashboardTelling("14.3", "Aantal risicoanalyses boven drempelwaarde",
        UnOfficialDashboardDao.getRiskAnalysis143(getPeriode()));
  }

  private DashboardTelling getTelling144() {
    return new DashboardTelling("14.4", "Aantal risicoanalyses boven drempelwaarde en " +
        "gekoppelde zaak alsnog verwerkt",
        UnOfficialDashboardDao.getRiskAnalysis144(getPeriode()));
  }

  private DashboardTelling getTelling145() {
    return new DashboardTelling("14.5", "Aantal risicoanalyses boven drempelwaarde en " +
        "gekoppelde zaak niet verwerkt (geweigerd / geannuleerd)",
        UnOfficialDashboardDao.getRiskAnalysis145(getPeriode()));
  }
}
