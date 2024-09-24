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

package nl.procura.gba.web.services.bs.naamskeuze;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

import java.util.Arrays;

public enum NaamskeuzeType {

  GEEN_NAAMSKEUZE("G", "Geen naamskeuze vooraf"),
  NAAMSKEUZE_VOOR_GEBOORTE("P", "Naamskeuze voor geboorte"),
  NAAMSKEUZE_BESTAAND_KIND("B", "Naamskeuze bestaand kind"),
  ONBEKEND("", "Onbekend");

  private final String code;
  private final String oms;

  NaamskeuzeType(String code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static NaamskeuzeType get(String code) {
    return Arrays.stream(values())
        .filter(a -> equalsIgnoreCase(a.getCode(), code))
        .findFirst().orElse(ONBEKEND);

  }

  public String getCode() {
    return code;
  }

  public String getOms() {
    return oms;
  }

  public boolean is(NaamskeuzeType... types) {
    return Arrays.stream(types)
        .anyMatch(type -> type == this);
  }

  @Override
  public String toString() {
    return getOms();
  }
}
