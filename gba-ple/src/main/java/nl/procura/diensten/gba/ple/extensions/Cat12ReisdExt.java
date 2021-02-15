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

package nl.procura.diensten.gba.ple.extensions;

import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.ArrayList;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.zoekpersoon.objecten.Reisdocumentgegevens;

public class Cat12ReisdExt extends BasePLCatExt {

  public Cat12ReisdExt(BasePLExt ext) {
    super(ext);
  }

  public boolean heeftSignalering() {
    return getExt().getCat(GBACat.REISDOC).getSets().stream()
        .anyMatch(set -> pos(aval(set.getLatestRec()
            .getElemVal(GBAElem.SIG_MET_BETREK_TOT_VERSTREK_NL_REISDOC)
            .getVal())));
  }

  public String getAutoriteit(String nummerNederlandsDocument) {
    for (BasePLSet set : getExt().getCat(GBACat.REISDOC).getSets()) {
      String nummer = set.getLatestRec().getElem(GBAElem.NR_NL_REISDOC).getValue().getVal();
      if (nummer.equalsIgnoreCase(nummerNederlandsDocument)) {
        BasePLElem element = set.getLatestRec().getElem(GBAElem.AUTORIT_VAN_AFGIFTE_NL_REISDOC);
        return element.getValue().getDescr();
      }
    }
    return "";
  }

  public List<Reisdocumentgegevens> getReisdocumentGegevens() {
    return new ArrayList<>();
  }
}
