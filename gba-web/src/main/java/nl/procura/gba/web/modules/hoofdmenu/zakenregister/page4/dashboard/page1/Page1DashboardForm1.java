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

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.dashboard.page1.Page1DashboardBean.*;

import nl.procura.commons.core.utils.ProPlanningUtils.PlanningPeriode;
import nl.procura.gba.web.components.layouts.form.GbaForm;

public class Page1DashboardForm1 extends GbaForm<Page1DashboardBean> {

  public Page1DashboardForm1() {

    setOrder(PERIODE, JAAR, KWARTAAL, MAAND, DAG);
    setColumnWidths(WIDTH_130, "");
    setBean(new Page1DashboardBean());
  }

  @Override
  public void afterSetBean() {

    ValueChangeListener listener = (ValueChangeListener) event -> {

      setVisible(false, JAAR, MAAND, KWARTAAL, DAG);

      PlanningPeriode type = (PlanningPeriode) event.getProperty().getValue();

      if (type != null) {

        switch (type) {

          case YEAR:
            setVisible(true, JAAR);
            break;

          case QUARTER:
            setVisible(true, JAAR, KWARTAAL);
            break;

          case MONTH:
            setVisible(true, JAAR, MAAND);
            break;

          case DAY:
            setVisible(true, DAG);
            break;

          default:
            break;
        }
      }

      repaint();
    };

    getField(PERIODE).addListener(listener);

    super.afterSetBean();
  }

  @Override
  public Page1DashboardBean getNewBean() {
    return new Page1DashboardBean();
  }

  private void setVisible(boolean visible, String... fields) {
    for (String field : fields) {
      getField(field).setVisible(visible);
    }
  }
}
