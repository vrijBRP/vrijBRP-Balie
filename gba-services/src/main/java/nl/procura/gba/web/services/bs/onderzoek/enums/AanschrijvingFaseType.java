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

package nl.procura.gba.web.services.bs.onderzoek.enums;

import java.util.Arrays;

public enum AanschrijvingFaseType {

  ONBEKEND(0, ""),
  FASE_1(1, "1e aanschrijving"),
  FASE_2(2, "2e aanschrijving - externe bron"),
  FASE_EXTRA(3, "Extra aanschrijving"),
  FASE_VOORNEMEN(4, "Voornemen ambtshalve wijziging"),
  FASE_BESLUIT(5, "Besluit"),
  FASE_OVERIG(6, "Overige");

  private int    code;
  private String oms = "";

  AanschrijvingFaseType(int code, String oms) {
    setCode(code);
    setOms(oms);
  }

  public static AanschrijvingFaseType get(int code) {
    return Arrays.stream(values())
        .filter(a -> a.getCode() == code)
        .findFirst().orElse(ONBEKEND);
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
