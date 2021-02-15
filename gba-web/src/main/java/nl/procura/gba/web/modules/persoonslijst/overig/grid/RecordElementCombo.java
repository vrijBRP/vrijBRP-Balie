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

package nl.procura.gba.web.modules.persoonslijst.overig.grid;

import org.apache.commons.lang3.StringUtils;

import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;

public class RecordElementCombo {

  private BasePLRec                     gbaRecord  = null;
  private BasePLSet                     gbaSet     = null;
  private GBAGroupElements.GBAGroupElem pleElement = null;
  private BasePLElem                    gbaElement = null;

  public RecordElementCombo(BasePLRec gbaRecord, BasePLElem gbaElement) {

    setGbaSet(gbaRecord.getSet());
    setGbaRecord(gbaRecord);
    setGbaElement(gbaElement);
    setPleElement(GBAGroupElements.getByCat(gbaElement.getCatCode(), gbaElement.getElemCode()));
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (!(obj instanceof RecordElementCombo)) {
      return false;
    }

    RecordElementCombo other = (RecordElementCombo) obj;

    if (other.getGbaElement().getCatCode() != getGbaElement().getCatCode()) {
      return false;
    } else if (other.getGbaElement().getElemCode() != getGbaElement().getElemCode()) {
      return false;
    } else if (other.getGbaRecord().getIndex() != getGbaRecord().getIndex()) {
      return false;
    } else {
      return other.getGbaSet().getExtIndex() == getGbaSet().getExtIndex();
    }
  }

  public BasePLElem getGbaElement() {
    return gbaElement;
  }

  public void setGbaElement(BasePLElem gbaElement) {
    this.gbaElement = gbaElement;
  }

  public BasePLRec getGbaRecord() {
    return gbaRecord;
  }

  public void setGbaRecord(BasePLRec gbaRecord) {
    this.gbaRecord = gbaRecord;
  }

  public BasePLSet getGbaSet() {
    return gbaSet;
  }

  public void setGbaSet(BasePLSet gbaSet) {
    this.gbaSet = gbaSet;
  }

  public GBAGroupElements.GBAGroupElem getPleElement() {
    return pleElement;
  }

  public void setPleElement(GBAGroupElements.GBAGroupElem pleElement) {
    this.pleElement = pleElement;
  }

  public String getValueDescr() {
    String val = getGbaElement().getValue().getVal();
    String descr = getGbaElement().getValue().getDescr();
    GBATable tableType = getPleElement().getElem().getTable();
    if (GBATable.ONBEKEND != tableType && StringUtils.isNotBlank(descr)) {
      return val + " (" + descr + ")";
    }
    return descr;
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = 1;
    result = prime * result + gbaElement.getCatCode();
    result = prime * result + gbaElement.getElemCode();
    result = prime * result + gbaRecord.getIndex();
    result = prime * result + gbaSet.getExtIndex();

    return result;
  }
}
