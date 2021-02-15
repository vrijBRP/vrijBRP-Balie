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

package nl.procura.gba.web.services.zaken.documenten.stempel;

/**
 * Documentstempel type
 */
public enum DocumentStempelType {

  OSARA_QR_ZAAK_ID(1, "Osara QR Zaak-id"),
  PROCURA_QR_ZAAK_ID(2, "Procura QR Zaak-id"),
  PROCURA_TEKST_ZAAK_ID(4, "Procura Tekst Zaak-id"),
  AFBEELDING(3, "Afbeelding"),
  ONBEKEND(-1, "Onbekend");

  private long   code = -1;
  private String oms;

  DocumentStempelType(int code, String oms) {
    this.setCode(code);
    this.oms = oms;
  }

  public static DocumentStempelType get(long code) {

    for (DocumentStempelType type : DocumentStempelType.values()) {
      if (code == type.getCode()) {
        return type;
      }
    }

    return ONBEKEND;
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

  @Override
  public String toString() {
    return getOms();
  }
}
