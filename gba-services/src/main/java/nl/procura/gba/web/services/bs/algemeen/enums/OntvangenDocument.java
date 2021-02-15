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

package nl.procura.gba.web.services.bs.algemeen.enums;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

import java.util.Arrays;

import nl.procura.gba.common.EnumWithCode;

public enum OntvangenDocument implements EnumWithCode<String> {

  NATUURLIJK("N", "Verklaring van overlijden (natuurlijke dood)"),
  ONNATUURLIJK("O", "Verklaring van geen bezwaar Officier van Justitie (niet-natuurlijke dood)"),
  VERZOEKUITSTEL("V", "Verzoek uitstel en verklaring van geen bezwaar arts"),
  TOESTEMMING("T", "Toestemming Officier van Justitie"),
  ONBEKEND("", "Onbekend");

  private String code;
  private String oms;

  OntvangenDocument(String code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static OntvangenDocument get(String code) {
    return Arrays.stream(values())
        .filter(a -> equalsIgnoreCase(a.getCode(), code))
        .findFirst().orElse(ONBEKEND);

  }

  @Override
  public String getCode() {
    return code;
  }

  public String getOms() {
    return oms;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
