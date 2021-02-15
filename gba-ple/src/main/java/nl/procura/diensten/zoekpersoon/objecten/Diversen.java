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

public class Diversen {

  private Diversengegevens   diversen_actueel;
  private Diversengegevens[] diversen_historie;

  public Diversen() {
  }

  public Diversengegevens getDiversen_actueel() {
    return diversen_actueel;
  }

  public void setDiversen_actueel(Diversengegevens diversen_actueel) {
    this.diversen_actueel = diversen_actueel;
  }

  public Diversengegevens[] getDiversen_historie() {
    return diversen_historie;
  }

  public void setDiversen_historie(Diversengegevens[] diversen_historie) {
    this.diversen_historie = diversen_historie;
  }
}
