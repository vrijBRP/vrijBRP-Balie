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

package nl.procura.gba.web.modules.zaken.rijbewijs.page15;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

public enum RedenOngeldigVerklaring {

  K1(false, "A", "art. 124.1a (afgifte op grond onjuiste gegevens)"),
  // Oud
  A(true, "A", "art. 124.1a (afgifte op grond onjuiste gegevens)"),
  B(true, "B", "art. 124.1b (abusievelijke afgifte)"),
  C(true, "C", "art. 124.1c (schriftelijke verklaring van afstand)"),
  D(false, "D", "art. 124.1d (onderzoek op eigen verzoek)"),
  E(true, "E", "art. 124.1e (niet in ontvangst genomen)"),
  F(false, "F", "art. 124.7 (aantekening i.v.m. niet geschikt na onderzoek naar geschiktheid)"),
  H(true, "H", "art. 124.6 (aantekening i.v.m. niet rijvaardig na vrijwillige afstand)"),
  L(false, "L", "Wegens ten onrechte toegekende of verlengde vakbekwaamheid)"),
  P(false, "P", "Ongeldig van rechtswege"),
  Q(false, "Q", "Ongeldig van rechtswege, terwijl het rijbewijs zijn geldigheid al heeft verloren"),
  K6(false, "X", "art. 123.1b (omwisseling buitenlands rijbewijs)"),
  // Oud
  X(false, "X", "art. 123.1b (omwisseling buitenlands rijbewijs)"),
  K8(false, "Y", "art. 123.1e (overlijden van de houder)"),
  // Oud
  Y(true, "Y", "art. 123.1e (overlijden van de houder)"),
  ONBEKEND(false, "Onbekend", "Onbekend");

  private final String  oms;
  private final String  code;
  private final boolean invulbaar;

  RedenOngeldigVerklaring(boolean invulbaar, String code, String oms) {
    this.invulbaar = invulbaar;
    this.code = code;
    this.oms = oms;
  }

  public static RedenOngeldigVerklaring get(String code) {
    for (RedenOngeldigVerklaring a : values()) {
      if (equalsIgnoreCase(a.getCode(), code)) {
        return a;
      }
    }

    return RedenOngeldigVerklaring.ONBEKEND;
  }

  public String getCode() {
    return code;
  }

  public String getOms() {
    return oms;
  }

  @Override
  public String toString() {
    return getCode() + ": " + getOms();
  }

  public boolean isInvulbaar() {
    return invulbaar;
  }
}
