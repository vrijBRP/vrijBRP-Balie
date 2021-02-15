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

package nl.procura.gba.web.services.gba.functies;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

public enum Geslacht {

  MAN("Man", "Mannelijk", "M", "M (mannelijk)", "dhr.", "heer", 1),
  VROUW("Vrouw", "Vrouwelijk", "V", "F (vrouwelijk)", "mw.", "mevrouw", 2),
  ONBEKEND("Onbekend", "Onbekend", "O", "O (onbekend)", "dhr./mw.", "heer/mevrouw", 0),
  NIET_VAST_GESTELD("", "", "", "", "", "", -1);

  private String volledig;
  private String normaal;
  private String afkorting;
  private String burgerlijkeStand;
  private String aanhefKort;
  private String aanhefLang;
  private int    rdwCode;

  Geslacht(String normaal, String volledig, String afkorting, String burgerlijkeStand, String aanhefKort,
      String aanhefLang, int rdwCode) {

    this.normaal = normaal;
    this.volledig = volledig;
    this.afkorting = afkorting;
    this.burgerlijkeStand = burgerlijkeStand;
    this.aanhefKort = aanhefKort;
    this.aanhefLang = aanhefLang;
    this.rdwCode = rdwCode;
  }

  public static Geslacht get(String afkorting) {
    for (Geslacht a : values()) {
      if (equalsIgnoreCase(a.getAfkorting(), afkorting)) {
        return a;
      }
    }

    return NIET_VAST_GESTELD;
  }

  public static Geslacht getByRdwCode(int rdwCode) {
    for (Geslacht a : values()) {
      if (a.getRdwCode() == rdwCode) {
        return a;
      }
    }

    return NIET_VAST_GESTELD;
  }

  public String getAfkorting() {
    return afkorting;
  }

  public String getBurgerlijkeStand() {
    return burgerlijkeStand;
  }

  public String getNormaal() {
    return normaal;
  }

  public String getVolledig() {
    return volledig;
  }

  public int getRdwCode() {
    return rdwCode;
  }

  @Override
  public String toString() {
    return getNormaal();
  }

  public String getAanhefKort() {
    return aanhefKort;
  }

  public void setAanhefKort(String aanhefKort) {
    this.aanhefKort = aanhefKort;
  }

  public String getAanhefLang() {
    return aanhefLang;
  }

  public void setAanhefLang(String aanhefLang) {
    this.aanhefLang = aanhefLang;
  }
}
