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

package nl.procura.gba.web.services.bs.naturalisatie.enums;

import java.util.Arrays;

import nl.procura.gba.common.EnumUtils;
import nl.procura.gba.common.EnumWithCode;

public enum BetrokkeneType implements EnumWithCode<Integer> {

  ONBEKEND(0, ""),
  ZELFDE(1, "Nog steeds woonachtig op het geregisteerde adres"),
  IMMIGRATIE(2, "Vanuit het buitenland gevestigd in de gemeente"),
  BINNEN(3, "Verhuisd binnen de gemeente"),
  NAAR_ANDERE(4, "Vertrokken naar een adres in een andere gemeente"),
  EMIGRATIE(5, "Vertrokken naar het buitenland"),
  NAAR_ONBEKEND(6, "Vertrokken met onbekende bestemming");

  private int    code = 0;
  private String oms  = "";

  BetrokkeneType(int code, String oms) {
    setCode(code);
    setOms(oms);
  }

  public boolean is(BetrokkeneType... types) {
    return Arrays.stream(types).anyMatch(type -> type.getCode() == getCode());
  }

  public static BetrokkeneType get(Number code) {
    return EnumUtils.get(values(), code, ONBEKEND);
  }

  @Override
  public Integer getCode() {
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