/*
 * Copyright 2024 - 2025 Procura B.V.
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

  MIJN_OVERHEID_WEL("mijn-overheid-wel", "Wel abonnee van mijn-overheid", false),
  MIJN_OVERHEID_NIET("mijn-overheid-niet", "Geen abonnee van mijn-overheid", false),
  WACHT_OP_RISICOANALYSE("wacht-op-risicoanalyse", "Wacht op risicoanalyse", false),
  FOUT_BIJ_VERWERKING("fout-bij-verwerking", "Fout bij verwerking", false),
  FAVORIET("favoriet", "Favoriete zaak", true),
  REQUEST_INBOX("verzoek", "Gekoppeld verzoek", false),
  ANDERS("", "Anders", false);

  private final String  code;
  private final String  oms;
  private final boolean user;

  ZaakAttribuutType(String code, String oms, boolean user) {
    this.code = code;
    this.oms = oms;
    this.user = user;
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

  public boolean isUser() {
    return user;
  }

  public boolean is(ZaakAttribuutType type) {
    return (type != null && type.getCode().equals(code));
  }

  @Override
  public String toString() {
    return getOms();
  }
}
