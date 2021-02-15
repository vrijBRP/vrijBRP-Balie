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

package nl.procura.gba.web.modules.bs.common.pages.zoekpage;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.PLResultComposite;
import nl.procura.gba.web.components.layouts.table.GbaTable;

public class BsZoekTable extends GbaTable {

  @Override
  public void setColumns() {

    setSelectable(true);

    addColumn("Nr", 30);
    addColumn("Naam", 200);
    addColumn("Geslacht", 60);
    addColumn("Geboortedatum", 100);
    addColumn("Adres");
    addColumn("Status", 250).setUseHTML(true);

    super.setColumns();
  }

  public void setResult(PLResultComposite result) {

    getRecords().clear();

    int i = 0;
    for (BasePLExt pl : result.getBasisPLWrappers()) {

      i++;
      Record row = addRecord(pl);
      row.addValue(astr(i));
      row.addValue(pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());
      row.addValue(pl.getPersoon().getGeslacht().getDescr());
      row.addValue(pl.getPersoon().getGeboorte().getDatumLeeftijd());
      row.addValue(pl.getVerblijfplaats().getAdres().getAdresPcWplGem());
      row.addValue(setClass(false, pl.getPersoon().getStatus().getOpsomming()));
    }

    reloadRecords();
  }
}
