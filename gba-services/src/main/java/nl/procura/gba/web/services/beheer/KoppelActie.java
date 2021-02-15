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

package nl.procura.gba.web.services.beheer;

import static nl.procura.gba.common.MiscUtils.setClass;

public enum KoppelActie {

  KOPPEL("Koppelen"),
  ONTKOPPEL("Ontkoppelen");

  private String oms;

  KoppelActie(String oms) {
    this.oms = oms;
  }

  public static KoppelActie get(boolean b) {
    return b ? KOPPEL : ONTKOPPEL;
  }

  public String getStatus() {

    switch (this) {
      case KOPPEL:
        return setClass("green", "Gekoppeld");

      case ONTKOPPEL:
      default:
        return setClass("red", "Niet-gekoppeld");
    }
  }

  public boolean isPossible(boolean isGekoppeld) {
    return (ONTKOPPEL == this && isGekoppeld) || (KOPPEL == this && !isGekoppeld);
  }

  @Override
  public String toString() {
    return oms;
  }
}
