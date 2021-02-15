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

package nl.procura.gba.web.services.beheer.profiel.actie;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

public enum ProfielActieType {

  SELECT("inzage"),
  ADD("toevoegen"),
  DELETE("verwijderen"),
  UPDATE("muteren"),
  ONBEKEND("onbekend");

  private String type = "";

  ProfielActieType(String type) {

    setType(type);
  }

  public static ProfielActieType getType(String type) {
    for (ProfielActieType e : values()) {
      if (equalsIgnoreCase(e.getType(), type)) {
        return e;
      }
    }

    return ONBEKEND;
  }

  public String getOms() {
    return getType().substring(0, 1).toUpperCase() + getType().substring(1);
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return getOms();
  }

}
