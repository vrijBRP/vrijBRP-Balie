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

package nl.procura.gba.web.services.zaken.algemeen;

public enum ZaakSortering {

  DATUM_INGANG_NIEUW_OUD("1", "1. datum ingang: nieuw > oud", true),
  DATUM_INGANG_OUD_NIEUW("2", "2. datum ingang: oud > nieuw", false),
  DATUM_INVOER_NIEUW_OUD("3", "3. tijdstip invoer: nieuw > oud", true),
  DATUM_INVOER_OUD_NIEUW("4", "4. tijdstip invoer: oud > nieuw", false);

  private final String  code;
  private final String  oms;
  private final boolean descending;

  ZaakSortering(String code, String oms, boolean descending) {
    this.code = code;
    this.oms = oms;
    this.descending = descending;
  }

  public static ZaakSortering get(String sort, ZaakSortering defaultSortering) {
    for (ZaakSortering sortering : values()) {
      if (sortering.getCode().equals(sort)) {
        return sortering;
      }
    }

    return defaultSortering;
  }

  public String getOms() {
    return oms;
  }

  @Override
  public String toString() {
    return getOms();
  }

  public boolean isDescending() {
    return descending;
  }

  public int getNumber(int max, int current) {
    return isDescending() ? ((max - current) + 1) : current;
  }

  public String getCode() {
    return code;
  }
}
