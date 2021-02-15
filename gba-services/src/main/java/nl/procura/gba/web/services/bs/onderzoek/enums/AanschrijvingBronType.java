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

public enum AanschrijvingBronType {

  PERSOON(1, "Persoon"),
  BELANGHEBBENDE(2, "Belanghebbende"),
  OVERIGE(3, "Overige"),
  GERELATEERDE(4, "Gerelateerde van een betrokkene");

  private int    code;
  private String oms = "";

  AanschrijvingBronType(int code, String oms) {
    setCode(code);
    setOms(oms);
  }

  public static AanschrijvingBronType get(int code) {
    return Arrays.stream(values())
        .filter(a -> a.getCode() == code)
        .findFirst().orElse(OVERIGE);
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
