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

import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.formats.Geboorte;
import nl.procura.diensten.gba.ple.extensions.formats.Naam;

public class Cat2OuderExt {

  private final BasePLRec record;
  private final BasePLRec orecord;
  private BasePLValue     anr = null;
  private BasePLValue     bsn = null;

  public Cat2OuderExt(BasePLRec record, BasePLRec orecord) {
    this.record = record;
    this.orecord = orecord;
  }

  public Naam getNaam() {
    return new Naam(record);
  }

  public BasePLValue getGeslacht() {
    return record.getElemVal(GBAElem.GESLACHTSAAND);
  }

  public Geboorte getGeboorte() {
    return new Geboorte(record, orecord);
  }

  /**
   * Geeft het Bsn en bij ontbreken daarvan het A-nummer terug.
   */
  public BasePLValue getNummer() {
    if (pos(getBsn().getCode())) {
      return getBsn();
    }
    return getAnr();
  }

  public BasePLValue getAnr() {
    if (anr == null) {
      anr = record.getElemVal(GBAElem.ANR);
    }
    return anr;
  }

  public BasePLValue getBsn() {
    if (bsn == null) {
      bsn = record.getElemVal(GBAElem.BSN);
    }
    return bsn;
  }

  public BasePLRec getRecord() {
    return record;
  }
}
