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

package nl.procura.gba.web.services.bs.erkenning;

import static nl.procura.standard.Globalfunctions.aval;

import java.math.BigDecimal;
import java.util.Arrays;

public enum NaamsPersoonType {

  MOEDER(1, "De moeder"),
  ERKENNER(2, "De erkenner"),
  VADER_DUO_MOEDER(3, "De vader/duo-moeder"),
  GEEN_VAN_BEIDE(4, "Geen van beide"),
  PARTNER(5, "De partner"),
  MOEDER_VADER_DUO_MOEDER(6, "Moeder + de vader/duo-moeder", MOEDER, VADER_DUO_MOEDER),
  VADER_DUO_MOEDER_MOEDER(7, "De vader/duo-moeder + moeder", VADER_DUO_MOEDER, MOEDER),
  MOEDER_ERKENNER(8, "Moeder + de erkenner", MOEDER, ERKENNER),
  ERKENNER_MOEDER(9, "De erkenner + moeder", ERKENNER, MOEDER),
  MOEDER_PARTNER(10, "Moeder + de partner", MOEDER, PARTNER),
  PARTNER_MOEDER(11, "De partner + moeder", PARTNER, MOEDER),
  ONBEKEND(-1, "Onbekend");

  private int                code;
  private String             type;
  private NaamsPersoonType[] combiTypes;

  NaamsPersoonType(int code, String type) {
    this.code = code;
    this.type = type;
  }

  NaamsPersoonType(int code, String type, NaamsPersoonType... naamsPersoonType) {
    this.code = code;
    this.type = type;
    this.combiTypes = naamsPersoonType;
  }

  public static NaamsPersoonType get(BigDecimal code) {
    return Arrays.stream(values())
        .filter(type -> type.getCode() == aval(code))
        .findFirst().orElse(ONBEKEND);

  }

  /**
   * Returns the combination of mother and erkenner, partner or vader/duo-moeder
   */
  public static NaamsPersoonType getNaamsPersoonType(String naam, String moeder, String partner,
      NaamsPersoonType partnerType) {
    if (naam.startsWith(moeder) && naam.startsWith(partner)) { // Beide
      return ONBEKEND;
    } else if (naam.startsWith(moeder)) {
      if (naam.endsWith(partner)) {
        return getType(MOEDER, partnerType);
      } else if (naam.equals(moeder)) {
        return MOEDER;
      }
    } else if (naam.startsWith(partner)) {
      if (naam.endsWith(moeder)) {
        return getType(partnerType, MOEDER);
      } else if (naam.equals(partner)) {
        return partnerType;
      }
    }
    return ONBEKEND;
  }

  public static NaamsPersoonType getType(NaamsPersoonType type1, NaamsPersoonType type2) {
    for (NaamsPersoonType type : NaamsPersoonType.values()) {
      if (type.getCombiTypes() != null) {
        boolean type1Equals = type.getCombiTypes()[0].equals(type1);
        boolean type2Equals = type.getCombiTypes()[1].equals(type2);
        if (type1Equals && type2Equals) {
          return type;
        }
      }
    }
    return ONBEKEND;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public NaamsPersoonType[] getCombiTypes() {
    return combiTypes;
  }

  public boolean is(NaamsPersoonType... types) {
    return Arrays.stream(types).anyMatch(
        type -> type.getCode() == getCode()
            || getCombiTypes() != null
            && Arrays.asList(getCombiTypes())
            .contains(type));
  }

  @Override
  public String toString() {
    return getType();
  }
}
