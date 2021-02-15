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

import static nl.procura.commons.core.utils.ProNumberUtils.toInt;

import org.apache.commons.lang3.text.WordUtils;

import nl.procura.commons.core.utils.ProPlanningUtils;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.dashboard.periodes.DashboardPeriode;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class PlanningMaandContainer extends ArrayListContainer {

  public PlanningMaandContainer() {

    setSort(false);

    for (int i = 12; i >= 1; i--) {
      String month = String.format("%d%02d%02d", 2015, i, 1);
      addItem(new DashboardPeriode(i, WordUtils.capitalize(ProPlanningUtils.getMonthName(toInt(month)))));
    }
  }
}
