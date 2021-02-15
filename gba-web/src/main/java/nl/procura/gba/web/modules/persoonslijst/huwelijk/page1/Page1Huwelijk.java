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

package nl.procura.gba.web.modules.persoonslijst.huwelijk.page1;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.trim;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.modules.persoonslijst.GBACategorieSoortTable;
import nl.procura.gba.web.modules.persoonslijst.huwelijk.page2.Page2Huwelijk;
import nl.procura.gba.web.modules.persoonslijst.overig.listpage.PlListPage;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Page1Huwelijk extends PlListPage {

  private final GBACategorieSoortTable table;

  public Page1Huwelijk(BasePLCat soort) {

    super("Huwelijk / GPS");

    table = new GBACategorieSoortTable(soort) {

      @Override
      public void onClick(Record record) {
        selectRecord(record);
      }

      @Override
      public void setColumns() {

        addColumn("Vnr.", 50);
        addColumn("&nbsp;", 20).setUseHTML(true);
        addColumn("Partner").setUseHTML(true);
        addColumn("Geslacht", 70);
        addColumn("Soort", 120);
        addColumn("Sluiting", 90);
        addColumn("Ontbinding", 100);

        super.setColumns();
      }

      @Override
      public void setRecords() {

        for (BasePLSet set : getSoort().getSets()) {
          BasePLRec record = set.getLatestRec();
          String s_geslachtsnaam = record.getElemVal(GBAElem.GESLACHTSNAAM).getDescr();
          String s_voornaam = record.getElemVal(GBAElem.VOORNAMEN).getDescr();
          String s_voorvoegsel = record.getElemVal(GBAElem.VOORV_GESLACHTSNAAM).getDescr();

          String s_naam = s_geslachtsnaam + ", " + s_voornaam;
          String s_geslacht = record.getElemVal(GBAElem.GESLACHTSAAND).getDescr();
          String c_soort = record.getElemVal(GBAElem.SOORT_VERBINTENIS).getCode();
          String s_soort = record.getElemVal(GBAElem.SOORT_VERBINTENIS).getDescr();

          if (c_soort.equals("P")) {
            s_soort = "GPS";
          }

          if (getPl().getHuwelijk().isOmzetting(set)) {
            s_soort += " (omz.)";
          }

          String bsn = record.getElemVal(GBAElem.BSN).getCode();

          if (fil(bsn)) {
            BasePLExt partner = getPl(bsn);

            if (!s_voorvoegsel.isEmpty()) {
              s_naam += ", " + s_voorvoegsel;
            }

            if ((partner != null) && partner.getPersoon().getStatus().isOverleden()) {
              s_naam += setClass(GbaWebTheme.TEXT.RED, " (overleden)");
            }
          }

          if (set.isMostRecentSet() && soort.hasMultipleSets()) {
            s_naam += " <b>(meest recente verbintenis)</b>";
          }

          Record r = addRecord(set);
          r.addValue(set.getExtIndex());
          r.addValue(getMutatieIcon(record));
          r.addValue(trim(s_naam));
          r.addValue(s_geslacht);
          r.addValue(s_soort);

          BasePLRec huw = set.getLatestRec();

          String dHuw = huw.getElemVal(
              GBAElem.DATUM_VERBINTENIS).getDescr();
          String dOntb = huw.getElemVal(
              GBAElem.DATUM_ONTBINDING).getDescr();
          String rOntb = huw.getElemVal(
              GBAElem.REDEN_ONTBINDING).getCode();

          r.addValue(dHuw);
          if (!rOntb.isEmpty()) {
            dOntb += " (" + rOntb + ")";
          }
          r.addValue(dOntb);
        }
      }
    };

    addExpandComponent(table);
  }

  @Override
  public void onEnter() {

    if (table.getRecord() != null) {

      selectRecord(table.getRecord());
    }
  }

  private void selectRecord(Record record) {
    Page2Huwelijk page2 = new Page2Huwelijk((BasePLSet) record.getObject());
    getNavigation().goToPage(page2);
  }
}
