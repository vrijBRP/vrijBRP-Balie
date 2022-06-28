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

package nl.procura.gba.web.services.zaken.algemeen.attribuut;

import java.util.Arrays;

public enum ZaakAttribuutType {

  MIJN_OVERHEID_WEL("mijn-overheid-wel", "Wel abonnee van mijn-overheid"),
  MIJN_OVERHEID_NIET("mijn-overheid-niet", "Geen abonnee van mijn-overheid"),
  WACHT_OP_RISICOANALYSE("wacht-op-risicoanalyse", "Wacht op risicoanalyse"),
  FOUT_BIJ_VERWERKING("fout-bij-verwerking", "Fout bij verwerking"),
  ANDERS("", "Anders");

  private final String code;
  private final String oms;

  ZaakAttribuutType(String code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static ZaakAttribuutType get(String code) {
    return Arrays.stream(values())
        .filter(type -> type.getCode().equals(code))
        .findFirst()
        .orElse(null);
  }

  public static String getOms(String attribuut) {
    ZaakAttribuutType type = ZaakAttribuutType.get(attribuut);
    return type != null ? type.getOms() : attribuut;
  }

  public String getCode() {
    return code;
  }

  public String getOms() {
    return oms;
  }

  public boolean is(ZaakAttribuutType type) {
    return (type != null && type.getCode().equals(code));
  }

  @Override
  public String toString() {
    return getOms();
  }
}
