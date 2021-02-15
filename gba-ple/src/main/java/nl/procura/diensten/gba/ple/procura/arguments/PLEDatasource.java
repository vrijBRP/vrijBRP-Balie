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

package nl.procura.diensten.gba.ple.procura.arguments;

import java.util.Arrays;

public enum PLEDatasource {

  STANDAARD(0, "Standaard"),
  PROCURA(1, "Gemeentelijk"),
  GBAV(2, "GBA-V");

  private int    code;
  private String oms;

  PLEDatasource(int code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static PLEDatasource get(int code) {
    return Arrays.stream(values()).filter(e -> e.getCode() == code).findFirst().orElse(STANDAARD);

  }

  public boolean is(PLEDatasource... bronnen) {
    return Arrays.stream(bronnen).anyMatch(bron -> bron == this);
  }

  @Override
  public String toString() {
    return getOms();
  }

  public int getCode() {
    return code;
  }

  public String getOms() {
    return oms;
  }
}
