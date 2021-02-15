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

package nl.procura.gba.web.modules.bs.omzetting.overzicht.form1;

import static nl.procura.gba.common.MiscUtils.setClass;

import java.util.List;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.bs.algemeen.vereiste.DossierVereiste;
import nl.procura.gba.web.theme.GbaWebTheme;

public class OverzichtVereisteTable extends GbaTable {

  private final List<DossierVereiste> vereisten;

  public OverzichtVereisteTable(List<DossierVereiste> vereisten) {
    this.vereisten = vereisten;
  }

  @Override
  public void setColumns() {

    addStyleName(GbaWebTheme.TABLE.WORD_WRAP);

    addColumn("Betreft", 200);
    addColumn("Voldaan", 120).setUseHTML(true);
    addColumn("Naam", 250);
    addColumn("Toelichting").setUseHTML(true);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    for (DossierVereiste v : vereisten) {

      if (v.isHeeftVoldaan() && !v.isOverruled()) {
        continue;
      }

      String voldaan;
      String overruled = "";

      if (v.isOverruled()) {
        overruled = " (overruled)";
      }

      if (v.isHeeftVoldaan()) {
        voldaan = setClass(true, "<b>Ja </b>") + overruled;
      } else {
        voldaan = setClass(false, "<b>Nee </b>") + overruled;
      }

      Record r = addRecord(v);
      r.addValue(v.getDossierVereiste());
      r.addValue(voldaan);
      r.addValue(v.getNaam());
      r.addValue(v.getToelichting().replaceAll("\n", "<br/>"));
    }

    super.setRecords();
  }
}
