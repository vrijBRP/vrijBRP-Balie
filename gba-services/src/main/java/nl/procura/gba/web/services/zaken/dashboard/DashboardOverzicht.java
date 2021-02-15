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

import static nl.procura.standard.Globalfunctions.emp;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.jpa.personen.dao.views.DashboardDao.DashboardPeriode;
import nl.procura.gba.web.common.misc.ZaakPeriode;

public abstract class DashboardOverzicht {

  private final List<DashboardTelling> tellingen = new ArrayList<>();
  private DashboardPeriode             periode;

  public DashboardOverzicht(String periode, long datumIngang, long datumEinde, String bron, String leverancier) {

    // placeholders voor als bron én leverancier leeg zijn
    if (emp(bron) && emp(leverancier)) {
      bron = "XXXXXX";
      leverancier = bron;
    }

    ZaakPeriode zaakPeriode = new ZaakPeriode(periode, datumIngang, datumEinde);
    this.periode = new DashboardPeriode(zaakPeriode.getdFrom(), zaakPeriode.getdTo(), bron, leverancier);
  }

  public void add(DashboardTelling dbt) {
    tellingen.add(dbt);
  }

  public List<DashboardTelling> getTellingen() {
    return tellingen;
  }

  public DashboardPeriode getPeriode() {
    return periode;
  }
}
