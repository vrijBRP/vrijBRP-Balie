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

public class Woningkaart {

  private Woningkaartgegevens   woningkaart_actueel;
  private Woningkaartgegevens[] woningkaart_historie;

  public Woningkaart() {
  }

  public Woningkaartgegevens getWoningkaart_actueel() {
    return woningkaart_actueel;
  }

  public void setWoningkaart_actueel(Woningkaartgegevens woningkaart_actueel) {
    this.woningkaart_actueel = woningkaart_actueel;
  }

  public Woningkaartgegevens[] getWoningkaart_historie() {
    return woningkaart_historie;
  }

  public void setWoningkaart_historie(Woningkaartgegevens[] woningkaart_historie) {
    this.woningkaart_historie = woningkaart_historie;
  }
}
