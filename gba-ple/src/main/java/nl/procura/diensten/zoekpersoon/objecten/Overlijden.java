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

public class Overlijden {

  private Overlijdengegevens   overlijden_actueel;
  private Overlijdengegevens[] overlijden_historie;

  public Overlijden() {
  }

  public Overlijdengegevens getOverlijden_actueel() {
    return overlijden_actueel;
  }

  public void setOverlijden_actueel(Overlijdengegevens overlijden_actueel) {
    this.overlijden_actueel = overlijden_actueel;
  }

  public Overlijdengegevens[] getOverlijden_historie() {
    return overlijden_historie;
  }

  public void setOverlijden_historie(Overlijdengegevens[] overlijden_historie) {
    this.overlijden_historie = overlijden_historie;
  }
}
