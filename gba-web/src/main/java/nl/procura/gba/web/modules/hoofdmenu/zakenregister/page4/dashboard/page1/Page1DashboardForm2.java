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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.dashboard.page1;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.dashboard.page1.Page1DashboardBean.OMSCHRIJVING;
import static nl.procura.standard.Globalfunctions.date2str;

import nl.procura.commons.core.utils.ProPlanningUtils.Period;
import nl.procura.gba.web.components.layouts.form.GbaForm;

public class Page1DashboardForm2 extends GbaForm<Page1DashboardBean> {

  public Page1DashboardForm2() {
    setOrder(OMSCHRIJVING);
    setReadThrough(true);
    setColumnWidths(WIDTH_130, "");
    setBean(new Page1DashboardBean());
  }

  public Page1DashboardForm2(Period period) {
    this();
    setOmschrijving(period);
  }

  @Override
  public Page1DashboardBean getNewBean() {
    return new Page1DashboardBean();
  }

  public void setOmschrijving(Period period) {
    getBean().setOmschrijving(date2str(period.getFrom()) + " t/m " + date2str(period.getTo()));
  }
}
