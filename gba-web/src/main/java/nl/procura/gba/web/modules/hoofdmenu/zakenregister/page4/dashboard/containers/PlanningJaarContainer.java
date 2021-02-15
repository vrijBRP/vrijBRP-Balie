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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.dashboard.containers;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.getSystemDate;

import nl.procura.commons.core.utils.ProNumberUtils;
import nl.procura.commons.core.utils.ProPlanningUtils;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.dashboard.periodes.DashboardPeriode;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class PlanningJaarContainer extends ArrayListContainer {

  public PlanningJaarContainer() {

    setSort(false);

    int year = ProPlanningUtils.getYear(ProNumberUtils.toInt(getSystemDate()));

    for (int i = 0; i >= -2; i--) {
      int nextYear = year + i;
      addItem(new DashboardPeriode(nextYear, astr(nextYear)));
    }
  }
}
