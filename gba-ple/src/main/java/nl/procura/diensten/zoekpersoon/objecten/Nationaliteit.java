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

public class Nationaliteit {

  private Nationaliteitgegevens   nationaliteit_actueel;
  private Nationaliteitgegevens[] nationaliteit_historie;

  public Nationaliteit() {
  }

  public Nationaliteitgegevens getNationaliteit_actueel() {
    return nationaliteit_actueel;
  }

  public void setNationaliteit_actueel(Nationaliteitgegevens nationaliteit_actueel) {
    this.nationaliteit_actueel = nationaliteit_actueel;
  }

  public Nationaliteitgegevens[] getNationaliteit_historie() {
    return nationaliteit_historie;
  }

  public void setNationaliteit_historie(Nationaliteitgegevens[] nationaliteit_actueel_historie) {
    this.nationaliteit_historie = nationaliteit_actueel_historie;
  }
}
