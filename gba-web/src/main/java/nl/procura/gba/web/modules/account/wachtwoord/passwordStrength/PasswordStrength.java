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

package nl.procura.gba.web.modules.account.wachtwoord.passwordStrength;

/**
 * Definieert de verschillende wachtwoord sterktes met de bijbehorende kleur
 * waarmee deze sterkte weergegeven moet worden.
 *

 * <p>
 * 2012
 */

public enum PasswordStrength {

  ONBEKEND("Onbekend", ""),
  TEKORT("Te kort", "red"),
  GOED("Goed", "green"),
  CIJFER("Minimaal 1 cijfer nodig", "red"),
  LEESTEKEN("Minimaal 1 leesteken nodig", "red"),
  HOOFDLETTER("Minimaal 1 hoofdletter nodig", "red"),
  KLEINELETTER("Minimaal 1 kleine letter nodig", "red");

  private String oms;
  private String color;

  PasswordStrength(String oms, String color) {
    this.oms = oms;
    this.color = color;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  @Override
  public String toString() {
    return oms;
  }
}
