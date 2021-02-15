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

package nl.procura.gba.web.modules.persoonslijst.nationaliteit.page1;

import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.formats.Nationaliteit;
import nl.procura.gba.web.modules.persoonslijst.GBACategorieSoortTable;
import nl.procura.gba.web.modules.persoonslijst.nationaliteit.page2.Page2Nationaliteit;
import nl.procura.gba.web.modules.persoonslijst.overig.listpage.PlListPage;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Page1Nationaliteit extends PlListPage {

  private final GBACategorieSoortTable table;

  public Page1Nationaliteit(BasePLCat soort) {

    super("Nationaliteit");

    table = new GBACategorieSoortTable(soort) {

      @Override
      public void onClick(Record record) {
        selectRecord(record);
      }

      @Override
      public void setColumns() {

        addColumn("Vnr.", 50);
        addColumn("&nbsp;", 20).setUseHTML(true);
        addColumn("Nationaliteit");
        addColumn("Datum geldigheid", 150);

        super.setColumns();
      }

      @Override
      public void setRecords() {

        for (BasePLSet set : getSoort().getSets()) {

          BasePLRec record = set.getLatestRec();

          Record r = addRecord(set);
          r.addValue(astr(set.getExtIndex()));
          r.addValue(getMutatieIcon(record));
          r.addValue(new Nationaliteit().getNationaliteit(record));
          r.addValue(record.getElemVal(GBAElem.INGANGSDAT_GELDIG).getDescr());
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

    Page2Nationaliteit page2 = new Page2Nationaliteit((BasePLSet) record.getObject());
    getNavigation().goToPage(page2);
  }
}
