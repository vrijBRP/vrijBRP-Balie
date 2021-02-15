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

package nl.procura.gba.web.rest.v2.model.zaken.base;

import nl.procura.gba.web.rest.v2.model.base.GbaRestEnum;

public enum GbaRestZaakStatusType implements GbaRestEnum<Integer> {

  INCOMPLEET(-1, "Incompleet", 1, false),
  WACHTKAMER(5, "Wachtkamer", 2, false),
  GEPREVALIDEERD(6, "Geprevalideerd", 3, false),
  OPGENOMEN(0, "Opgenomen", 4, false),
  INBEHANDELING(1, "In behandeling", 5, false),
  DOCUMENT_ONTVANGEN(7, "Doc. ontvangen", 6, false),
  GEWEIGERD(8, "Geweigerd", 7, true),
  VERWERKT(2, "Verwerkt", 8, true),
  VERWERKT_IN_GBA(3, "Verwerkt in PROBEV", 9, true),
  GEANNULEERD(4, "Geannuleerd", 10, true),
  ONBEKEND(99, "Onbekend", 11, true);

  private Integer code;
  private Integer sortIndex;
  private boolean finalStatus;
  private String  descr;

  GbaRestZaakStatusType(int code, String descr, int sortIndex, boolean finalStatus) {
    this.code = code;
    this.sortIndex = sortIndex;
    this.finalStatus = finalStatus;
    this.descr = descr;
  }

  @Override
  public Integer getCode() {
    return code;
  }

  @Override
  public String getDescr() {
    return descr;
  }

  public long getSortIndex() {
    return sortIndex;
  }

  public boolean isFinalStatus() {
    return finalStatus;
  }
}
