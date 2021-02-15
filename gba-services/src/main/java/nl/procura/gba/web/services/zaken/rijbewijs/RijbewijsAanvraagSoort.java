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

public enum RijbewijsAanvraagSoort {

  EERSTE_AFGIFTE(1, "Eerste afgifte"),
  CATEGORIE_UITBREIDING(2, "Categorie-uitbreiding"),
  VERNIEUWEN(3, "Vernieuwen"),
  VERVANGEN_MET_HUIDIGE_GELDIGHEIDSDATA(4, "Vervangen met huidige geldigheidsdata"),
  HERAFGIFTE_NADAT_ONGELDIGHEIDSVERKLARING_IS_OPGEHEVEN(5, "Herafgifte nadat Ongeldigheidsverklaring is opgeheven"),
  OMWISSELING_BUITENLANDS_RIJBEWIJS(10, "Omwisseling buitenlands rijbewijs"),
  AANVRAAG_OBV_OUD_LINNEN_RIJBEWIJS(11, "Aanvraag o.b.v. oud linnen rijbewijs"),
  AANVRAAG_OBV_MILITAIR_RIJBEWIJS(12, "Aanvraag o.b.v. militair rijbewijs"),
  AANVRAAG_OPGEVOERD_DOOR_RDW(20, "Aanvraag opgevoerd door RDW"),
  AANVRAAG_OPGEVOERD_DOOR_OBV_AANVRAGER_NIET_IN_GBA(30, "Aanvraag opgevoerd door obv 'aanvrager niet in BRP'"),
  ONBEKEND(0, "Onbekend");

  private long   code = 0;
  private String oms  = "";

  RijbewijsAanvraagSoort(long code, String oms) {

    setCode(code);
    setOms(oms);
  }

  public static RijbewijsAanvraagSoort get(long code) {
    for (RijbewijsAanvraagSoort a : values()) {
      if (a.getCode() == code) {
        return a;
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
    return getCode() + ": " + getOms();
  }
}
