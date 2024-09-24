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

package nl.procura.gba.web.common.misc;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.burgerzaken.gba.core.validators.DatVal;
import nl.procura.burgerzaken.gba.core.validators.Val;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.standard.ProcuraDate;
import nl.procura.validation.Anr;
import nl.procura.validation.Bsn;

public class GBAElem2FieldConverter {

  public String getFormatValue(int catCode, int elementCode, String value) {

    GBAGroupElements.GBAGroupElem pleE = GBAGroupElements.getByCat(catCode, elementCode);

    Val val = pleE.getElem().getVal();
    GBATable tabel = pleE.getElem().getTable();

    if (pleE.getElem() == GBAElem.BSN) {
      return new Bsn(value).getFormatBsn();

    } else if (pleE.getElem() == GBAElem.ANR) {
      return new Anr(value).getFormatAnummer();

    } else if ((tabel != GBATable.ONBEKEND) && tabel.isNational()) {
      return GbaTables.get(tabel).get(value).getDescription();

    } else if (val instanceof DatVal) {
      return new ProcuraDate(value).getFormatDate();
    }

    return value;
  }
}
