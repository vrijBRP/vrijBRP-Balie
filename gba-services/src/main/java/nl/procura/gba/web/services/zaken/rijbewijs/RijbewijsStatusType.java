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

public enum RijbewijsStatusType {

  GEREGISTREERD(10, "Aanvraag geregistreerd"),
  GEANNULEERD(15, "Aanvraag geannuleerd"),
  GEACCORDEERD(20, "Aanvraag geaccordeerd"),
  IN_BACKOFFICE_GOEDGEKEURD(30, "Aanvraag in backoffice goedgekeurd"),
  GEREGISTREERD_DOOR_RDW(32, "Aanvraag geregistreerd door RDW"),
  IN_BEHANDELING_GENOMEN_DOOR_RDW(33, "Aanvraag in behandeling genomen door RDW"),
  AFGEWEZEN_DOOR_RDW(36, "Aanvraag afgewezen door RDW"),
  RIJBEWIJSNUMMER_OPGEVOERD(40, "Rijbewijsnummer opgevoerd"),
  NIEUW_RIJBEWIJSNUMMER_OPGEVOERD(43, "Nieuw rijbewijsnummer opgevoerd"),
  PERSONALISATIEOPDRACHT_VERZONDEN_NAAR_PERSONALISATOR(50, "Personalisatieopdracht verzonden naar Personalisator"),
  PERSONALISATIEOPDRACHT_ONTVANGEN_NAAR_PERSONALISATOR(53, "Personalisatieopdracht ontvangen naar Personalisator"),
  RIJBEWIJS_AANGEMAAKT_EN_VERZONDEN(55, "Rijbewijs aangemaakt en verzonden"),
  RIJBEWIJS_ONTVANGEN_DOOR_GEMEENTE_OK(60, "Rijbewijs ontvangen door gemeente, OK"),
  RIJBEWIJS_ONTVANGEN_DOOR_GEMEENTE_NIET_OK(65, "Rijbewijs ontvangen door gemeente, niet OK"),
  RIJBEWIJS_NIET_ONTVANGEN_DOOR_GEMEENTE(70, "Rijbewijs niet ontvangen door gemeente"),
  RIJBEWIJS_NIET_UITGEREIKT(80, "Rijbewijs niet uitgereikt"),
  RIJBEWIJS_UITGEREIKT(90, "Rijbewijs uitgereikt"),
  RIJBEWIJS_GEMUTEERD_DOOR_RDW(95, "Rijbewijs gemuteerd door RDW"),
  ONBEKEND(0, "Onbekend");

  private long    code      = 0;
  private String  oms       = "";
  private boolean crbStatus = true;

  RijbewijsStatusType(long code, String oms) {
    this(code, oms, true);
  }

  RijbewijsStatusType(long code, String oms, boolean crbStatus) {

    setCode(code);
    setOms(oms);
    setCrbStatus(crbStatus);
  }

  public static RijbewijsStatusType get(long code) {

    for (RijbewijsStatusType a : values()) {
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

  public boolean is(RijbewijsStatusType... types) {
    if (types != null) {
      for (RijbewijsStatusType type : types) {
        if (type != null) {
          if (type.getCode() == getCode()) {
            return true;
          }
        }
      }
    }

    return false;
  }

  public boolean isCrbStatus() {
    return crbStatus;
  }

  public void setCrbStatus(boolean crbStatus) {
    this.crbStatus = crbStatus;
  }

  @Override
  public String toString() {
    return getCode() + ": " + getOms();
  }
}
