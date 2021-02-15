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

package nl.procura.gba.web.modules.persoonslijst.lokAfn.page1;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.gba.web.modules.persoonslijst.GBACategorieSoortTable;
import nl.procura.gba.web.modules.persoonslijst.overig.listpage.PlListPage;

public class Page1LokAfn extends PlListPage {

  private final GBACategorieSoortTable table;

  public Page1LokAfn(BasePLCat soort) {

    super("Lokale afnemer indicaties");

    table = new GBACategorieSoortTable(soort) {

      @Override
      public void init() {

        super.init();
        setSelectable(false);
      }

      @Override
      public void setColumns() {

        addColumn("Aantekening", 150);
        addColumn("Omschrijving");
        addColumn("Opmerking");

        setSelectable(false);

        super.setColumns();
      }

      @Override
      public void setRecords() {

        for (BasePLSet set : getSoort().getSets()) {

          BasePLRec record = set.getLatestRec();

          Record r = addRecord(set);
          r.addValue(record.getElemVal(GBAElem.LOK_AFN_AANT_SOORT).getDescr());
          r.addValue(record.getElemVal(GBAElem.LOK_AFN_AANT_OMSCHR).getDescr());
          r.addValue(record.getElemVal(GBAElem.LOK_AFN_AANT_OPMERK).getDescr());
        }
      }
    };

    addExpandComponent(table);
  }
}
