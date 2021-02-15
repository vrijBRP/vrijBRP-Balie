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

import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.commons.core.utils.ProPlanningUtils;
import nl.procura.commons.core.utils.ProPlanningUtils.Period;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.dashboard.page2.Page2Dashboard;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.dashboard.DashboardOverzicht1;
import nl.procura.gba.web.services.zaken.dashboard.DashboardOverzicht2;
import nl.procura.gba.web.services.zaken.dashboard.DashboardTelling;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1Dashboard extends NormalPageTemplate {

  private Page1DashboardForm1 form1;
  private Page1DashboardForm2 form2;
  private Table1              table1;
  private Table2              table2;
  private Period              periode = null;

  public Page1Dashboard() {

    super("Dashboard");
    setSpacing(true);
    addButton(buttonSearch);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      table1 = new Table1();
      table2 = new Table2();
      form1 = new Page1DashboardForm1();
      form2 = new Page1DashboardForm2();

      addComponent(form1);
      addComponent(new Fieldset("Dashboard aantallen"));
      addComponent(new HLayout(form2).widthFull());

      table1.setHeight("98%");
      table2.setHeight("98%");

      GbaTabsheet tabsheet = new GbaTabsheet();
      tabsheet.setSizeFull();
      tabsheet.setExtraTopMargin();
      tabsheet.addTab(table1, "Officiële cijfers");
      tabsheet.addTab(table2, "Overige cijfers");
      addExpandComponent(tabsheet);
    }

    super.event(event);
  }

  @Override
  public void onEnter() {
    onSearch();
  }

  @Override
  public void onSearch() {

    form1.commit();

    switch (form1.getBean().getPeriode()) {

      case YEAR:
        periode = ProPlanningUtils.getYearPeriod(getJaar());
        break;

      case QUARTER:
        periode = ProPlanningUtils.getQuarterPeriod(getJaar(), getKwartaal());
        break;

      case MONTH:
        periode = ProPlanningUtils.getMonthPeriod(getJaar(), getMaand());
        break;

      case DAY:
        periode = ProPlanningUtils.getDayPeriod(getDag());
        break;

      case UNKNOWN:
      default:
        break;
    }

    table1.init();
    table2.init();

    super.onSearch();
  }

  private int getDag() {
    return (int) form1.getBean().getDag().getLongValue();
  }

  private int getJaar() {
    return form1.getBean().getJaar().getPeriode();
  }

  private int getKwartaal() {
    return form1.getBean().getKwartaal().getPeriode();
  }

  private int getMaand() {
    return form1.getBean().getMaand().getPeriode();
  }

  public class Table1 extends GbaTable {

    public Table1() {
    }

    @Override
    public void onClick(Record record) {

      getNavigation().goToPage(new Page2Dashboard(periode, record.getObject(DashboardTelling.class)));

      super.onClick(record);
    }

    @Override
    public void setColumns() {

      setClickable(true);

      addColumn("Code", 100);
      addColumn("Omschrijving");
      addColumn("Aantallen");

      super.setColumns();
    }

    @Override
    public void setRecords() {

      if (periode != null) {

        String periodeOms = periode.toString();
        int van = periode.getFrom();
        int tm = periode.getTo();

        form2.setOmschrijving(periode);
        form2.repaint();

        String bron = getServices().getParameterService().getParm(ParameterConstant.MIDOFFICE_DASHBOARD_BRONNEN);

        String leverancier = getServices().getParameterService()
            .getParm(ParameterConstant.MIDOFFICE_DASHBOARD_LEVERANCIERS);

        DashboardOverzicht1 overzicht = new DashboardOverzicht1(periodeOms, van, tm, bron, leverancier);

        for (DashboardTelling t : overzicht.getTellingen()) {

          Record record = addRecord(t);
          record.addValue(t.getKey());
          record.addValue(t.getOms());
          record.addValue(astr(t.getAantal() < 0 ? "N.v.t." : t.getAantal()));
        }
      }

      super.setRecords();
    }
  }

  public class Table2 extends GbaTable {

    public Table2() {
    }

    @Override
    public void onClick(Record record) {

      getNavigation().goToPage(new Page2Dashboard(periode, record.getObject(DashboardTelling.class)));

      super.onClick(record);
    }

    @Override
    public void setColumns() {

      setClickable(true);

      addColumn("Code", 100);
      addColumn("Omschrijving");
      addColumn("Aantallen");

      super.setColumns();
    }

    @Override
    public void setRecords() {

      if (periode != null) {

        String periodeOms = periode.toString();
        int van = periode.getFrom();
        int tm = periode.getTo();

        form2.setOmschrijving(periode);
        form2.repaint();

        String bron = getServices().getParameterService().getParm(ParameterConstant.MIDOFFICE_DASHBOARD_BRONNEN);

        String leverancier = getServices().getParameterService()
            .getParm(ParameterConstant.MIDOFFICE_DASHBOARD_LEVERANCIERS);

        DashboardOverzicht2 overzicht = new DashboardOverzicht2(periodeOms, van, tm, bron, leverancier);

        for (DashboardTelling t : overzicht.getTellingen()) {

          Record record = addRecord(t);
          record.addValue(t.getKey());
          record.addValue(t.getOms());
          record.addValue(astr(t.getAantal() < 0 ? "N.v.t." : t.getAantal()));
        }
      }

      super.setRecords();
    }
  }
}
