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

package nl.procura.gba.web.modules.persoonslijst.kind.page1;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.*;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.modules.persoonslijst.GBACategorieSoortTable;
import nl.procura.gba.web.modules.persoonslijst.kind.page2.Page2Kind;
import nl.procura.gba.web.modules.persoonslijst.overig.listpage.PlListPage;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Page1Kind extends PlListPage {

  private final GBACategorieSoortTable table;

  public Page1Kind(BasePLCat soort) {

    super("Kinderen");

    table = new GBACategorieSoortTable(soort) {

      @Override
      public void onClick(Record record) {
        selectRecord(record);
      }

      @Override
      public void setColumns() {

        addColumn("Vnr.", 50);
        addColumn("&nbsp;", 20).setUseHTML(true);
        addColumn("Geboren", 120);
        addColumn("Geslacht", 70);
        addColumn("Kind").setUseHTML(true);

        super.setColumns();
      }

      @Override
      public void setRecords() {

        for (BasePLSet set : getSoort().getSets()) {

          BasePLRec record = set.getLatestRec();

          String s_geslachtsnaam = record.getElemVal(GBAElem.GESLACHTSNAAM).getDescr();
          String s_voornaam = record.getElemVal(GBAElem.VOORNAMEN).getDescr();
          String s_voorvoegsel = record.getElemVal(GBAElem.VOORV_GESLACHTSNAAM).getDescr();

          String s_naam = s_geslachtsnaam;

          if (!s_voornaam.isEmpty()) {
            s_naam += ", " + s_voornaam;
          }

          if (!s_voorvoegsel.isEmpty()) {
            s_naam += ", " + s_voorvoegsel;
          }

          String bsn = record.getElemVal(GBAElem.BSN).getCode();
          String s_geslacht = "";
          String s_geboorte = "";

          if (fil(bsn)) {
            BasePLExt kind = getPl(bsn);
            if (kind != null) {
              if (kind.getPersoon().getStatus().isOverleden()) {
                s_naam = trim(s_naam) + setClass(GbaWebTheme.TEXT.RED, " (overleden)");
              }

              s_geslacht = kind.getPersoon().getGeslacht().getDescr();
              s_geboorte = kind.getPersoon().getGeboorte().getDatumLeeftijd();
            }
          } else {
            BasePLElem elementWBetrekking = record.getElem(GBAElem.REG_BETREKK);
            if ("L".equals(elementWBetrekking.getValue().getVal())) {
              s_naam = trim(s_naam) + setClass(GbaWebTheme.TEXT.RED, " (levenloos geboren)");
            }
          }

          Record r = addRecord(set);
          r.addValue(astr(set.getExtIndex()));
          r.addValue(getMutatieIcon(record));
          r.addValue(s_geboorte);
          r.addValue(s_geslacht);
          r.addValue(s_naam);
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

    Page2Kind page2 = new Page2Kind((BasePLSet) record.getObject());
    getNavigation().goToPage(page2);
  }
}
