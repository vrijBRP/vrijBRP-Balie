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

package nl.procura.gba.web.modules.bs.common.pages.broerzuspage;

import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.Cat2OuderExt;
import nl.procura.diensten.gba.ple.extensions.Cat9KindExt;
import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Geboorte
 */

public class BsBroerZusPage extends ButtonPageTemplate {

  private final BasePLExt moeder;
  private Table1          table1 = null;

  public BsBroerZusPage(BasePLExt moeder) {

    this.moeder = moeder;

    H2 h2 = new H2("De kinderen van de moeder");

    addButton(buttonClose);
    getButtonLayout().addComponent(h2, getButtonLayout().getComponentIndex(buttonClose));
    getButtonLayout().setExpandRatio(h2, 1f);
    getButtonLayout().setWidth("100%");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setSpacing(true);

      setInfo("De andere kinderen van " + moeder.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());

      setTable1(new Table1());

      addComponent(getTable1());
    }

    super.event(event);
  }

  public Table1 getTable1() {
    return table1;
  }

  public void setTable1(Table1 table1) {
    this.table1 = table1;
  }

  @Override
  public void onClose() {

    getWindow().closeWindow();
  }

  public class Table1 extends GbaTable {

    @Override
    public void setColumns() {

      addColumn("Nr", 30);
      addColumn("Kind").setUseHTML(true);
      addColumn("Vader").setUseHTML(true);
      addColumn("Geboortedatum", 120);

      super.setColumns();
    }

    @Override
    public void setRecords() {

      int nr = 0;

      for (Cat9KindExt kind : moeder.getKinderen().getKinderen()) {

        BasePLExt kindPl = getServices().getPersonenWsService().getPersoonslijst(
            kind.getBsn().getVal());

        nr++;

        Record r = addRecord(kind);

        r.addValue(nr);
        r.addValue(kind.getNaam().getNaamNaamgebruikEersteVoornaam());

        String vader = "";

        for (Cat2OuderExt ouder : kindPl.getOuders().getOuders()) {
          if (Geslacht.MAN.getAfkorting().equalsIgnoreCase(ouder.getGeslacht().getVal())) {
            vader = ouder.getNaam().getNaamNaamgebruikEersteVoornaam();
          }
        }

        r.addValue(fil(vader) ? vader : "Geen vader gevonden");
        r.addValue(kind.getGeboorte().getDatumLeeftijd());
      }

      super.setRecords();
    }
  }
}
