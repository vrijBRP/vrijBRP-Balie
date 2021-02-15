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

package nl.procura.gba.web.services.bs.registration;

import java.math.BigDecimal;
import java.util.Arrays;

public enum RelationType {

  NOT_SET(-1, "", true),
  PARENT_OF(1, "Is ouder van", true),
  CHILD_OF(2, "Is kind van", true),
  PARTNER_OF(3, "Is (ex-)partner van", true),
  ONLY_1_LEGAL_PARENT(4, "Heeft juridisch maar 1 ouder", false),
  NO_LEGAL_PARENTS(5, "Heeft juridisch geen ouders", false),
  UNKNOWN(0, "Onbekend", true);

  private final BigDecimal code;
  private final String     oms;
  private boolean          related;

  RelationType(int code, String oms, boolean related) {
    this.code = BigDecimal.valueOf(code);
    this.oms = oms;
    this.related = related;
  }

  public static RelationType valueOfCode(BigDecimal code) {
    for (final RelationType var : values()) {
      if (var.getCode().equals(code)) {
        return var;
      }
    }

    return NOT_SET;
  }

  public boolean is(RelationType... types) {
    return Arrays.stream(types).anyMatch(type -> type == this);
  }

  public BigDecimal getCode() {
    return code;
  }

  public String getOms() {
    return oms;
  }

  @Override
  public String toString() {
    return getOms();
  }

  public boolean isRelated() {
    return related;
  }
}
