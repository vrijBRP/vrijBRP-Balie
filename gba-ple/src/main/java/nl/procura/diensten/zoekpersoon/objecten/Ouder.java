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

public class Ouder {

  private Oudergegevens   ouder_actueel;
  private Oudergegevens[] ouder_historie;

  public Ouder() {
  }

  public Oudergegevens[] getOuder_historie() {
    return ouder_historie;
  }

  public void setOuder_historie(Oudergegevens[] ouder_historie) {
    this.ouder_historie = ouder_historie;
  }

  public Oudergegevens getOuder_actueel() {
    return ouder_actueel;
  }

  public void setOuder_actueel(Oudergegevens ouder_actueel) {
    this.ouder_actueel = ouder_actueel;
  }
}
