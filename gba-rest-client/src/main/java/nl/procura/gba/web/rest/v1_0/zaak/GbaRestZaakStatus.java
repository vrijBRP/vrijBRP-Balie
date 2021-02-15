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

package nl.procura.gba.web.rest.v1_0.zaak;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

import java.util.ArrayList;
import java.util.List;

public enum GbaRestZaakStatus {

  INCOMPLEET(-1, 1, false, "Incompleet"),
  WACHTKAMER(5, 2, false, "Wachtkamer"),
  GEPREVALIDEERD(6, 3, false, "Geprevalideerd"),
  OPGENOMEN(0, 4, false, "Opgenomen"),
  INBEHANDELING(1, 5, false, "In behandeling"),
  DOCUMENT_ONTVANGEN(7, 6, false, "Doc. ontvangen"),
  GEWEIGERD(8, 7, true, "Geweigerd"),
  VERWERKT(2, 8, true, "Verwerkt"),
  VERWERKT_IN_GBA(3, 9, true, "Verwerkt in PROBEV"),
  GEANNULEERD(4, 10, true, "Geannuleerd"),
  ONBEKEND(99, 11, true, "Onbekend");

  private long    code;
  private long    sortCode;
  private boolean eindStatus;
  private String  oms;

  GbaRestZaakStatus(long code, long sortCode, boolean eindStatus, String oms) {

    this.code = code;
    this.sortCode = sortCode;
    this.eindStatus = eindStatus;
    this.oms = oms;
  }

  public static GbaRestZaakStatus get(long code) {
    for (GbaRestZaakStatus e : values()) {
      if (e.getCode() == code) {
        return e;
      }
    }

    return ONBEKEND;
  }

  public static GbaRestZaakStatus get(String oms) {
    for (GbaRestZaakStatus e : values()) {
      if (equalsIgnoreCase(e.getOms(), oms)) {
        return e;
      }
    }

    return ONBEKEND;
  }

  public static List<GbaRestZaakStatus> getAll() {
    List<GbaRestZaakStatus> types = new ArrayList<>();
    for (GbaRestZaakStatus type : values()) {
      if (type != ONBEKEND) {
        types.add(type);
      }
    }

    return types;
  }

  public boolean isMinimaal(GbaRestZaakStatus zaakStatus) {
    return getSortCode() >= zaakStatus.getSortCode();
  }

  public boolean isMaximaal(GbaRestZaakStatus zaakStatus) {
    return getSortCode() <= zaakStatus.getSortCode();
  }

  @Override
  public String toString() {
    return getOms();
  }

  public long getCode() {
    return code;
  }

  public void setCode(long code) {
    this.code = code;
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public long getSortCode() {
    return sortCode;
  }

  public void setSortCode(long sortCode) {
    this.sortCode = sortCode;
  }

  public boolean isEindStatus() {
    return eindStatus;
  }

  public void setEindStatus(boolean eindStatus) {
    this.eindStatus = eindStatus;
  }
}
