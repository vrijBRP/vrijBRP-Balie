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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.procura.gba.web.services.zaken.documenten;

import static nl.procura.standard.Globalfunctions.astr;

public enum DocumentVertrouwelijkheid {

  ONBEKEND(-1, "ONBEKEND", "Onbekend"),
  ZEER_GEHEIM(1, "ZEER GEHEIM", "Zeer geheim"),
  GEHEIM(2, "GEHEIM", "Geheim"),
  CONFIDENTIEEL(3, "CONFIDENTIEEL", "Confidentieel"),
  VERTROUWELIJK(4, "VERTROUWELIJK", "Vertrouwelijk"),
  ZAAKVERTROUWELIJK(5, "ZAAKVERTROUWELIJK", "Zaakvertrouwelijk"),
  INTERN(6, "INTERN", "Intern"),
  BEPERKT_OPENBAAR(7, "BEPERKT OPENBAAR", "Beperkt openbaar"),
  OPENBAAR(8, "OPENBAAR", "Openbaar");

  private int    code;
  private String naam;
  private String omschrijving;

  DocumentVertrouwelijkheid(int code, String naam, String omschrijving) {
    this.code = code;
    this.naam = naam;
    this.omschrijving = omschrijving;
  }

  /**
   *
   * @param value
   * @return
   */
  public static DocumentVertrouwelijkheid get(String value) {
    for (DocumentVertrouwelijkheid type : DocumentVertrouwelijkheid.values()) {
      if (type.getNaam().equalsIgnoreCase(value) || type.getOmschrijving().equalsIgnoreCase(value) ||
          type.name().equalsIgnoreCase(value) || astr(type.getCode()).equalsIgnoreCase(value)) {
        return type;
      }
    }
    return ONBEKEND;
  }

  /**
   *
   * @return
   */
  public int getCode() {
    return code;
  }

  /**
   *
   * @param code
   */
  public void setCode(int code) {
    this.code = code;
  }

  /**
   *
   * @return
   */
  public String getNaam() {
    return naam;
  }

  /**
   *
   * @param naam
   */
  public void setNaam(String naam) {
    this.naam = naam;
  }

  @Override
  public String toString() {
    return getOmschrijving();
  }

  public String getOmschrijving() {
    return omschrijving;
  }

  public void setOmschrijving(String omschrijving) {
    this.omschrijving = omschrijving;
  }

}
