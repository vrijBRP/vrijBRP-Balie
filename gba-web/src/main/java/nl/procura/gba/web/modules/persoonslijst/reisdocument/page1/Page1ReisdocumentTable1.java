/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.modules.persoonslijst.reisdocument.page1;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.gba.web.modules.persoonslijst.GBACategorieSoortTable;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentUtils;

public class Page1ReisdocumentTable1 extends GBACategorieSoortTable {

  public Page1ReisdocumentTable1(BasePLCat soort) {
    super(soort);
  }


  @Override
  public void setColumns() {
    addColumn("Vnr.", 50);
    addColumn("&nbsp;", 20).setUseHTML(true);
    addColumn("Nummer", 100);
    addColumn("Reisdocument");
    addColumn("Uitgifte", 90);
    addColumn("Einde", 90);
    addColumn("Inhouding/vermissing", 250);

    super.setColumns();
  }

  @Override
  public void setRecords() {
    for (BasePLSet set : getSoort().getSets()) {
      if (!set.getRecs().isEmpty()) {
        BasePLRec record = set.getRecs().get(0);

        String s_nummer = record.getElemVal(GBAElem.NR_NL_REISDOC)
            .getDescr();
        String s_reisdocument = record.getElemVal(GBAElem.SOORT_NL_REISDOC)
            .getDescr();
        String d_uitgifte = record.getElemVal(GBAElem.DATUM_UITGIFTE_NL_REISDOC)
            .getDescr();
        String d_einde = record.getElemVal(GBAElem.DATUM_EINDE_GELDIG_NL_REISDOC)
            .getDescr();

        boolean signalering = pos(record.getElemVal(
                GBAElem.SIG_MET_BETREK_TOT_VERSTREK_NL_REISDOC)
            .getCode());

        boolean buitenl_reisd = pos(
            record.getElemVal(GBAElem.AAND_BEZIT_BUITENL_REISDOC).getCode());

        if (emp(s_reisdocument)) {
          if (signalering) {
            s_reisdocument = "Signalering";
          } else if (buitenl_reisd) {
            s_reisdocument = "Buitenlands document";
          }
        }

        String s_aanduiding = ReisdocumentUtils.getInhoudingOmschrijving(record);

        if (signalering) {
          s_reisdocument = "Signalering";
        }

        if (!record.getElem(GBAElem.SOORT_NL_REISDOC).isAllowed()) {
          s_reisdocument = record.getElemVal(GBAElem.SOORT_NL_REISDOC)
              .getDescr();
          s_aanduiding = record.getElemVal(GBAElem.SOORT_NL_REISDOC)
              .getDescr();
        }

        Record r = addRecord(set);
        r.addValue(astr(set.getExtIndex()));
        r.addValue(getMutatieIcon(record));
        r.addValue(s_nummer);
        r.addValue(s_reisdocument);
        r.addValue(d_uitgifte);
        r.addValue(d_einde);
        r.addValue(s_aanduiding);
      }
    }
  }
}
