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

package nl.procura.gbaws.export.elements;

public class ElementsJsonRecord {

  private int elemCode = -1;
  private int catCode  = -1;

  public ElementsJsonRecord() {
  }

  public ElementsJsonRecord(int catCode, int elemCode) {
    this.catCode = catCode;
    this.elemCode = elemCode;
  }

  public int getElemCode() {
    return elemCode;
  }

  public void setElemCode(int elemCode) {
    this.elemCode = elemCode;
  }

  public int getCatCode() {
    return catCode;
  }

  public void setCatCode(int catCode) {
    this.catCode = catCode;
  }
}
