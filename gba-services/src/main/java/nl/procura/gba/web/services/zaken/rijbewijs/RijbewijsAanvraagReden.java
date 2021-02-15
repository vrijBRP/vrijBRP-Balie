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

public enum RijbewijsAanvraagReden {

  NVT(0, "N.v.t."),
  WEGENS_VERLIES_OF_DIEFSTAL(1, "Wegens verlies of diefstal"),
  WEGENS_BESCHADIGING_OF_ONLEESBAAR(2, "Wegens beschadiging of onleesbaar"),
  WEGENS_VERLOOP_GELDIGHEIDSDATUM(3, "Wegens verloop geldigheidsdatum"),
  WEGENS_GEDEELTELIJKE_ONGELDIGHEIDSVERKLARING(4, "Wegens gedeeltelijke ongeldigheidsverklaring"),
  WEGENS_OVERIGE_REDENEN(5, "Wegens overige redenen");

  private long   code = 0;
  private String oms  = "";

  RijbewijsAanvraagReden(long code, String oms) {

    setCode(code);
    setOms(oms);
  }

  public static RijbewijsAanvraagReden get(long code) {
    for (RijbewijsAanvraagReden a : values()) {
      if (a.getCode() == code) {
        return a;
      }
    }

    return NVT;
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
    return getCode() + ": " + getOms();
  }
}
