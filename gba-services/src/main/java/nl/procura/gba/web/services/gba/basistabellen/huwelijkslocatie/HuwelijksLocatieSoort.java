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

package nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

public enum HuwelijksLocatieSoort {

  GEMEENTELIJK("", "Gemeentelijk", 2, 4),
  COMMERCIEEL("C", "Commercieel", 2, 4),
  BIJZONDER("B", "Bijzonder", 6, 6);

  private String code;
  private String oms;
  private int    aantalGetuigenMin;
  private int    aantalGetuigenMax;

  HuwelijksLocatieSoort(String code, String oms, int aantalGetuigenMin, int aantalGetuigenMax) {

    this.code = code;
    this.oms = oms;
    this.aantalGetuigenMin = aantalGetuigenMin;
    this.aantalGetuigenMax = aantalGetuigenMax;
  }

  public static HuwelijksLocatieSoort get(String code) {

    for (HuwelijksLocatieSoort optie : values()) {
      if (equalsIgnoreCase(optie.getCode(), code)) {
        return optie;
      }
    }

    return null;
  }

  public int getAantalGetuigenMax() {
    return aantalGetuigenMax;
  }

  public void setAantalGetuigenMax(int aantalGetuigenMax) {
    this.aantalGetuigenMax = aantalGetuigenMax;
  }

  public int getAantalGetuigenMin() {
    return aantalGetuigenMin;
  }

  public void setAantalGetuigenMin(int aantalGetuigenMin) {
    this.aantalGetuigenMin = aantalGetuigenMin;
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

  @Override
  public String toString() {
    return getOms();
  }
}
