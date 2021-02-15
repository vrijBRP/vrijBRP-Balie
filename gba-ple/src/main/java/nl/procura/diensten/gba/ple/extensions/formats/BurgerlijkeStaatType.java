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

package nl.procura.diensten.gba.ple.extensions.formats;

import static nl.procura.standard.Globalfunctions.astr;

import java.util.Arrays;

public enum BurgerlijkeStaatType {

  ONBEKEND(0, "ONB", "Onbekend"),
  ONGEHUWD(1, "ONG", "Ongehuwd en nooit gehuwd geweest"),
  HUWELIJK(2, "HUW", "Gehuwd"),
  GESCHEIDEN(3, "SCH", "Gescheiden"),
  WEDUWE(4, "WED", "Weduwe/Weduwnaar"),
  PARTNERSCHAP(5, "PRT", "Partnerschap"),
  ONTBONDEN(6, "BGP", "Partnerschap beëindigd"),
  ACHTERGEBLEVEN(7, "AGP", "Achtergebleven partner");

  private final long   code;
  private final String bs;
  private final String oms;

  BurgerlijkeStaatType(long code, String bs, String oms) {
    this.code = code;
    this.bs = bs;
    this.oms = oms;
  }

  public static BurgerlijkeStaatType get(long code) {
    return Arrays.stream(values()).filter(a -> a.getCode() == code)
        .findFirst().orElse(ONBEKEND);
  }

  public static BurgerlijkeStaatType get(String bs) {
    return Arrays.stream(values())
        .filter(a -> a.getBs().equalsIgnoreCase(bs) || astr(a.getCode()).equalsIgnoreCase(bs))
        .findFirst()
        .orElse(ONBEKEND);

  }

  public static BurgerlijkeStaatType get(BurgerlijkeStaatType burgerlijkeStaat) {
    return Arrays.stream(values()).filter(a -> a == burgerlijkeStaat)
        .findFirst().orElse(ONBEKEND);
  }

  public boolean is(BurgerlijkeStaatType... types) {
    return Arrays.stream(types).anyMatch(type -> type == this);
  }

  @Override
  public String toString() {
    return getOms();
  }

  public String getOms() {
    return oms;
  }

  public String getBs() {
    return bs;
  }

  public long getCode() {
    return code;
  }
}
