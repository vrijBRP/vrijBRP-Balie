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

package nl.procura.gba.web.services.zaken.identiteit;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

import java.util.Arrays;

public enum IdentificatieType {

  PASPOORT("P", "Paspoort"),
  IDENTITEITSKAART("I", "Identiteitskaart"),
  RIJBEWIJS("R", "Rijbewijs"),
  VERBLIJFSDOCUMENT("V", "Verblijfsdocument"),
  REISDOCUMENTEN_ADMINISTRATIE("D", "Reisdocumentenadministratie"),
  RIJBEWIJZEN_ADMINISTRATIE("W", "Rijbewijzenadministratie"),
  PERSOON_GEZIEN_VNM("G", "Persoon gezien, vragen niet mogelijk"),
  VRAGEN("X", "Vragen"),
  EXTERNE_APPLICATIE("E", "Door externe applicatie"),
  RPS("RPS", "Door BR"),
  ONBEKEND("", "Onbekend");

  private String code = "";
  private String oms  = "";

  IdentificatieType(String code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static IdentificatieType get(String code) {
    return Arrays.stream(values())
        .filter(a -> equalsIgnoreCase(a.getCode(), code))
        .findFirst()
        .orElse(ONBEKEND);
  }

  public String getCode() {
    return code;
  }

  public String getOms() {
    return oms;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
