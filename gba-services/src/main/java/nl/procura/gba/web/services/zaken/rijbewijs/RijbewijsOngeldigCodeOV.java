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

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

public enum RijbewijsOngeldigCodeOV {

  A("A", "art 124.1a (afgifte op grond onjuiste gegevens)"),
  B("B", "art 124.1b (abusievelijke afgifte)"),
  C("C", "art 124.1c (schriftelijke verklaring van afstand)"),
  D("D", "art 124.1d (onderzoek op eigen verzoek)"),
  E("E", "art 124.1e (niet van afgevende instantie in ontvangst genomen)"),
  F("F", "art 124.7 (aantekening i.v.m. niet geschikt na onderzoek naar geschiktheid)"),
  H("H", "art 124.6 (aantekening i.v.m. niet rijvaardig na vrijwillige afstand)"),
  L("L", "wegens ten onrechte toegekende of verlengde vakbekwaamheid)"),
  P("P", "Ongeldig van rechtswege"),
  Q("Q", "Ongeldig van rechtswege, terwijl het rijbewijs zijn geldigheid al heeft verloren"),
  X("X", "art 123.1b (omwisseling voor een buitenlands rijbewijs)"),
  Y("Y", "art 123.1e (overlijden van de houder)"),
  OVERIG("O", "Overig");

  private String code = "";
  private String oms  = "";

  RijbewijsOngeldigCodeOV(String code, String oms) {

    setCode(code);
    setOms(oms);
  }

  public static RijbewijsOngeldigCodeOV get(String code) {

    for (RijbewijsOngeldigCodeOV var : values()) {
      if (equalsIgnoreCase(var.getCode(), code)) {
        return var;
      }
    }

    return OVERIG;
  }

  public static String getCodeOms(String code) {
    return code.toUpperCase() + " (" + get(code).getOms() + ")";
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }
}
