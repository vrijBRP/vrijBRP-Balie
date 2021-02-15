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

package nl.procura.gba.web.components.layouts.page.zoekhistorie;

import static nl.procura.gba.web.services.beheer.persoonhistorie.PersoonHistorieType.HISTORIE;
import static nl.procura.standard.Globalfunctions.astr;

import java.util.List;

import com.vaadin.ui.CssLayout;

import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.persoonslijst.overzicht.ModuleOverzichtPersoonslijst;
import nl.procura.gba.web.services.beheer.persoonhistorie.PersoonHistorie;
import nl.procura.gba.web.services.beheer.persoonhistorie.PersoonHistorieService;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

/**
 * Inhoud van popupview voor historie
 */
public class ZoekHistorieLayout extends CssLayout {

  private final GbaTable table;

  public ZoekHistorieLayout() {

    setWidth("300px");

    table = new GbaTable() {

      @Override
      public void onClick(Record record) {
        selectRecord(record);
      }

      @Override
      public void setColumns() {

        setSelectable(true);

        addColumn("Nr", 20);
        addColumn("Persoon");

        super.setColumns();
      }

      @Override
      public void setRecords() {

        GbaApplication app = getApplication();

        PersoonHistorieService persoonHistorie = app.getServices().getPersoonHistorieService();
        List<PersoonHistorie> list = persoonHistorie.getPersoonHistorie(HISTORIE,
            getApplication().getServices().getGebruiker());

        int nr = list.size();

        for (PersoonHistorie ph : list) {

          Record r = addRecord(ph);
          r.addValue(nr);
          r.addValue(ph.getOmschrijving());

          nr--;
        }
      }
    };

    addComponent(table);
  }

  private void selectRecord(Record record) {

    GbaApplication app = (GbaApplication) getApplication();

    try {

      PersoonHistorie ph = (PersoonHistorie) record.getObject();
      app.goToPl(getWindow(), ModuleOverzichtPersoonslijst.NAME, PLEDatasource.STANDAARD, astr(ph.getNummer()));
    } catch (Exception e) {

      e.printStackTrace();
      app.handleException(getWindow(), e);
    }
  }
}
