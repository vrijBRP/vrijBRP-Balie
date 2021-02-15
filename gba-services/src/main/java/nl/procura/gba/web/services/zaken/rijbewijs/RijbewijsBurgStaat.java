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

package nl.procura.gba.web.services.zaken.rijbewijs;

public enum RijbewijsBurgStaat {

  ONBEKEND(0, "Onbekend"),
  ONGEHUWD(1, "Ongehuwd en nooit gehuwd of geregistreerde partner geweest"),
  GEHUWD(2, "Gehuwd"),
  GESCHEIDEN(3, "Gescheiden"),
  WEDUWE(4, "Weduwe/weduwnaar"),
  PARTNER(5, "Geregistreerde partner"),
  GESCHEIDEN_PARTNER(6, "Gescheiden geregistreerde partner"),
  ACHTERGEBLEVEN_PARTNER(7, "Achtergebleven geregistreerde partner"),
  OVERIG(8, "Ongehuwd of geen geregistreerde partner, eventueel wel gehuwd of geregistreerde partner geweest");

  private long   code = 0;
  private String oms  = "";

  RijbewijsBurgStaat(long code, String oms) {

    setCode(code);
    setOms(oms);
  }

  public static RijbewijsBurgStaat get(long code) {
    for (RijbewijsBurgStaat e : values()) {
      if (e.getCode() == code) {
        return e;
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
}
