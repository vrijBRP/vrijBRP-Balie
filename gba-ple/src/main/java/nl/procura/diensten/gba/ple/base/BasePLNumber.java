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

package nl.procura.diensten.gba.ple.base;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.PERSOON;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.VERW;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ANR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.BSN;
import static nl.procura.standard.Globalfunctions.aval;

import nl.procura.diensten.gba.ple.procura.arguments.PLNumber;
import nl.procura.validation.Anummer;
import nl.procura.validation.Bsn;

public class BasePLNumber extends PLNumber {

  public BasePLNumber(BasePL pl) {
    super(-1, -1, -1, -1, -1);
    for (BasePLCat cat : pl.getCats(PERSOON, VERW)) {
      for (BasePLSet set : cat.getSets()) {
        BasePLRec rec = set.getLatestRec();
        if (rec.isElem(BSN.getCode())) {
          Bsn bsn = new Bsn(rec.getElem(BSN).getValue().getVal());
          if (bsn.isCorrect()) {
            setBsn(bsn.getLongBsn());
          }
        } else if (rec.isElem(ANR.getCode())) {
          Anummer anr = new Anummer(rec.getElem(ANR).getValue().getVal());
          if (anr.isCorrect()) {
            setA1(aval(anr.getA1()));
            setA2(aval(anr.getA2()));
            setA3(aval(anr.getA3()));
          }
        }
      }
    }
  }
}
