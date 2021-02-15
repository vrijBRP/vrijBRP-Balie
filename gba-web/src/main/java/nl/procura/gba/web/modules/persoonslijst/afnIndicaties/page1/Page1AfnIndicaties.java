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

package nl.procura.gba.web.modules.persoonslijst.afnIndicaties.page1;

import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.gba.web.modules.persoonslijst.GBACategorieSoortTable;
import nl.procura.gba.web.modules.persoonslijst.overig.listpage.PlListPage;

public class Page1AfnIndicaties extends PlListPage {

  private final GBACategorieSoortTable table;

  public Page1AfnIndicaties(BasePLCat soort) {

    super("Afnemer indicaties");

    table = new GBACategorieSoortTable(soort) {

      @Override
      public void init() {

        super.init();
        setSelectable(false);
      }

      @Override
      public void setColumns() {

        addColumn("Vnr.", 50);
        addColumn("&nbsp;", 20).setUseHTML(true);
        addColumn("Indicatie", 100);
        addColumn("Naam");
        addColumn("Geldigheid", 90);

        setSelectable(false);

        super.setColumns();
      }

      @Override
      public void setRecords() {

        int i = 0;
        int volgnr;

        for (BasePLSet set : getSoort().getSets()) {

          BasePLRec record = set.getLatestRec();

          volgnr = getSoort().getSets().size() - i;
          String d_geld = record.getElemVal(GBAElem.INGANGSDAT_GELDIG).getDescr();

          Record r = addRecord(set);
          r.addValue(astr(volgnr));
          r.addValue(getMutatieIcon(record));
          r.addValue(record.getElemVal(GBAElem.AFNEMERSINDICATIE).getVal());
          r.addValue(record.getElemVal(GBAElem.AFNEMERSINDICATIE).getDescr());
          r.addValue(d_geld);
          i++;
        }
      }
    };

    addExpandComponent(table);
  }
}
