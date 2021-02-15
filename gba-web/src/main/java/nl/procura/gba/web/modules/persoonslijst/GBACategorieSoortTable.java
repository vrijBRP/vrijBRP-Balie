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

package nl.procura.gba.web.modules.persoonslijst;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.layouts.table.GbaTable;

public class GBACategorieSoortTable extends GbaTable {

  private BasePLCat soort;

  public GBACategorieSoortTable(BasePLCat soort) {

    setSelectable(true);
    setSoort(soort);
    setTableListener(value -> {

      // Dubbele x'en samenvoegen
      return MiscUtils.trimAllowed(astr(value));
    });
  }

  public BasePLCat getSoort() {
    return soort;
  }

  public void setSoort(BasePLCat soort) {
    this.soort = soort;
  }

  protected String getMutatieIcon(BasePLRec record) {

    if (record.getStatus() == GBARecStatus.MUTATION) {
      return setClass("mutatie", "M");
    }

    return "";
  }
}
