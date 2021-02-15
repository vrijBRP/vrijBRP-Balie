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

package nl.procura.gba.web.components.layouts.page.relaties;

import static nl.procura.gba.common.MiscUtils.setClass;

import com.vaadin.ui.CssLayout;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.overzicht.ModuleOverzichtZaken;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieLijst;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieType;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

/**
 * Inhoud van popupview voor relaties
 */
public class ZoekRelatiesLayout extends CssLayout {

  private final GbaTable table;

  public ZoekRelatiesLayout() {

    setWidth("400px");
    table = new GbaTable() {

      @Override
      public void onClick(Record record) {
        selectRecord(record);
      }

      @Override
      public void setColumns() {
        setSelectable(true);
        addColumn("Relatie", 80);
        addColumn("Persoon").setUseHTML(true);

        super.setColumns();
      }

      @Override
      public void setRecords() {
        GbaApplication app = getApplication();
        BasePLExt pl = app.getServices().getPersonenWsService().getOpslag().getHuidige();
        RelatieLijst rl = getApplication().getServices().getPersonenWsService().getRelatieLijst(pl, true);

        for (Relatie relatie : rl.getSortedRelaties()) {
          Record r = addRecord(relatie.getPl());
          String relatieType = relatie.getRelatieType().getOms();

          if (relatie.getRelatieType() == RelatieType.AANGEVER) {
            relatieType = "-";
          }

          r.addValue(relatieType);
          String persoon = relatie.getPl().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
          if (relatie.getPl().getPersoon().getStatus().isOverleden()) {
            persoon += setClass(false, " (overleden)");
          }
          r.addValue(persoon);
        }
      }
    };

    addComponent(table);
  }

  private void selectRecord(Record record) {
    GbaApplication app = (GbaApplication) getApplication();
    try {
      BasePLExt pl = (BasePLExt) record.getObject();
      app.goToPl(getWindow(), ModuleOverzichtZaken.NAME, PLEDatasource.STANDAARD,
          pl.getPersoon().getNummer().getVal());
    } catch (Exception e) {
      e.printStackTrace();
      app.handleException(getWindow(), e);
    }
  }
}
