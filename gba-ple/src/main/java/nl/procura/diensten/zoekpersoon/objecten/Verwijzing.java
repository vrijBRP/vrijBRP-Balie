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

public class Verwijzing {

  private Verwijzinggegevens   verwijzing_actueel;
  private Verwijzinggegevens[] verwijzing_historie;

  public Verwijzing() {
  }

  public Verwijzinggegevens getVerwijzing_actueel() {
    return verwijzing_actueel;
  }

  public void setVerwijzing_actueel(Verwijzinggegevens verwijzing_actueel) {
    this.verwijzing_actueel = verwijzing_actueel;
  }

  public Verwijzinggegevens[] getVerwijzing_historie() {
    return verwijzing_historie;
  }

  public void setVerwijzing_historie(Verwijzinggegevens[] verwijzing_historie) {
    this.verwijzing_historie = verwijzing_historie;
  }
}
