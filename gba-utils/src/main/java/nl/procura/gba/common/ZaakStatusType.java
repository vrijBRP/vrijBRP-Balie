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

package nl.procura.gba.common;

import java.util.ArrayList;
import java.util.List;

public enum ZaakStatusType {

  INCOMPLEET(-1, 1, false, "Incompleet"),
  WACHTKAMER(5, 2, false, "Wachtkamer"),
  GEPREVALIDEERD(6, 3, false, "Geprevalideerd"),
  OPGENOMEN(0, 4, false, "Opgenomen"),
  INBEHANDELING(1, 5, false, "In behandeling"),
  DOCUMENT_ONTVANGEN(7, 6, false, "Doc. ontvangen"),
  GEWEIGERD(8, 7, true, "Geweigerd"),
  VERWERKT_IN_GBA(3, 9, true, "Verwerkt op de PL"),
  VERWERKT(2, 8, true, "Verwerkt", VERWERKT_IN_GBA),
  GEANNULEERD(4, 10, true, "Geannuleerd"),
  VERWIJDERD(9, 11, true, "Verwijderd"),
  NIET_GEVONDEN(10, 12, true, "Niet gevonden"),
  ONBEKEND(99, 13, false, "Onbekend");

  private long           code;
  private long           sortCode;
  private boolean        eindStatus;
  private String         oms;
  private ZaakStatusType combiStatus;

  ZaakStatusType(long code, long sortCode, boolean eindStatus, String oms) {
    this(code, sortCode, eindStatus, oms, null);
  }

  ZaakStatusType(long code, long sortCode, boolean eindStatus, String oms, ZaakStatusType combiStatus) {
    this.code = code;
    this.sortCode = sortCode;
    this.eindStatus = eindStatus;
    this.oms = oms;
    this.combiStatus = combiStatus;
  }

  public static ZaakStatusType get(long code) {
    for (ZaakStatusType e : values()) {
      if (e.getCode() == code) {
        return e;
      }
    }

    return ONBEKEND;
  }

  public static List<ZaakStatusType> getAll() {
    List<ZaakStatusType> types = new ArrayList<>();
    for (ZaakStatusType type : values()) {
      if (type != ONBEKEND) {
        types.add(type);
      }
    }

    return types;
  }

  public static List<ZaakStatusType> getCombiStatussen(ZaakStatusType... zaakStatusTypes) {
    List<ZaakStatusType> list = new ArrayList<>();
    for (ZaakStatusType type : zaakStatusTypes) {
      if (type.getCombiStatus() != null) {
        list.add(type.getCombiStatus());
      }
    }
    return list;
  }

  public static List<ZaakStatusType> getMetEindStatus() {
    List<ZaakStatusType> types = new ArrayList<>();
    for (ZaakStatusType type : values()) {
      if (type.isEindStatus()) {
        types.add(type);
      }
    }

    return types;
  }

  public static List<ZaakStatusType> getZonderEindStatus() {
    List<ZaakStatusType> types = new ArrayList<>();
    for (ZaakStatusType type : values()) {
      if (!type.isEindStatus()) {
        types.add(type);
      }
    }

    return types;
  }

  public boolean isCombiStatus() {
    for (ZaakStatusType type : values()) {
      if (type.getCombiStatus() == this) {
        return true;
      }
    }
    return false;
  }

  public boolean isKleinerDan(ZaakStatusType zaakStatus) {
    return getSortCode() < zaakStatus.getSortCode();
  }

  public boolean isGroterDan(ZaakStatusType zaakStatus) {
    return getSortCode() > zaakStatus.getSortCode();
  }

  public boolean isMinimaal(ZaakStatusType zaakStatus) {
    return getSortCode() >= zaakStatus.getSortCode();
  }

  public boolean isMaximaal(ZaakStatusType zaakStatus) {
    return getSortCode() <= zaakStatus.getSortCode();
  }

  public boolean is(ZaakStatusType... zaakStatussen) {
    for (ZaakStatusType type : zaakStatussen) {
      if (getCode() == type.getCode()) {
        return true;
      }
    }
    return false;
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

  public ZaakStatusType getCombiStatus() {
    return combiStatus;
  }

  public void setCombiStatus(ZaakStatusType combiStatus) {
    this.combiStatus = combiStatus;
  }
}
