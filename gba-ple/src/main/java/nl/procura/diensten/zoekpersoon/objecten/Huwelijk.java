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

package nl.procura.diensten.zoekpersoon.objecten;

public class Huwelijk {

  private Huwelijkgegevens   huwelijk_actueel;
  private Huwelijkgegevens[] huwelijk_historie;

  public Huwelijk() {
  }

  public Huwelijkgegevens getHuwelijk_actueel() {
    return huwelijk_actueel;
  }

  public void setHuwelijk_actueel(Huwelijkgegevens huwelijk_actueel) {
    this.huwelijk_actueel = huwelijk_actueel;
  }

  public Huwelijkgegevens[] getHuwelijk_historie() {
    return huwelijk_historie;
  }

  public void setHuwelijk_historie(Huwelijkgegevens[] huwelijk_historie) {
    this.huwelijk_historie = huwelijk_historie;
  }
}
