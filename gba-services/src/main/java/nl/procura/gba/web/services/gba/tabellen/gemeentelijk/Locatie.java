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

package nl.procura.gba.web.services.gba.tabellen.gemeentelijk;

import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.diensten.gba.ple.procura.utils.diacrits.Diacs;

public class Locatie extends GemeentelijkeTabel {

  public Locatie() {
    super(GBATable.LOCATIE);
    setDiacrietVeld(Diacs.LOCATIE);
    setSql("select x.cLocatie, x.locatie, x.diac from Locatie x where x.cLocatie > 0 order by x.locatie");
  }

  @Override
  public String[] format(String[] row) {

    setCode(row[0]);
    setWaarde(row[1]);
    setDiacriet(pos(row[2]));

    return row;
  }
}
