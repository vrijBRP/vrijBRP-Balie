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

public class Persoon {

  private Persoonsgegevens   persoon_actueel  = new Persoonsgegevens();
  private Persoonsgegevens[] persoon_historie = new Persoonsgegevens[0];

  public Persoon() {
  }

  public Persoonsgegevens getPersoon_actueel() {
    return persoon_actueel;
  }

  public void setPersoon_actueel(Persoonsgegevens persoon_actueel) {
    this.persoon_actueel = persoon_actueel;
  }

  public Persoonsgegevens[] getPersoon_historie() {
    return persoon_historie;
  }

  public void setPersoon_historie(Persoonsgegevens[] persoon_historie) {
    this.persoon_historie = persoon_historie;
  }
}
