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

package nl.procura.gba.web.services.zaken.algemeen.attribuut;

public enum ZaakAttribuutType {

  MIJN_OVERHEID_WEL("mijn-overheid-wel", "Wel abonnee van mijn-overheid"),
  MIJN_OVERHEID_NIET("mijn-overheid-niet", "Geen abonnee van mijn-overheid"),
  WACHT_OP_RISICOANALYSE("wacht-op-risicoanalyse", "Wacht op risicoanalyse"),
  ANDERS("", "Anders");

  private String code = "";
  private String oms  = "";

  ZaakAttribuutType(String code, String oms) {

    setCode(code);
    setOms(oms);
  }

  public static ZaakAttribuutType get(String code) {
    for (ZaakAttribuutType type : values()) {
      if (type.getCode().equals(code)) {
        return type;
      }
    }
    return null;
  }

  public static String getOms(String attribuut) {
    ZaakAttribuutType type = ZaakAttribuutType.get(attribuut);
    return type != null ? type.getOms() : attribuut;
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

  public boolean is(ZaakAttribuutType type) {
    return (type != null && type.getCode().equals(code));
  }

  @Override
  public String toString() {
    return getOms();
  }
}
