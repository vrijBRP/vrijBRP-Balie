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

public class Kiesrecht {

  private Kiesrechtgegevens   kiesrecht_actueel;
  private Kiesrechtgegevens[] kiesrecht_historie;

  public Kiesrecht() {
  }

  public Kiesrechtgegevens getKiesrecht_actueel() {
    return kiesrecht_actueel;
  }

  public void setKiesrecht_actueel(Kiesrechtgegevens kiesrecht_actueel) {
    this.kiesrecht_actueel = kiesrecht_actueel;
  }

  public Kiesrechtgegevens[] getKiesrecht_historie() {
    return kiesrecht_historie;
  }

  public void setKiesrecht_historie(Kiesrechtgegevens[] kiesrecht_historie) {
    this.kiesrecht_historie = kiesrecht_historie;
  }
}
