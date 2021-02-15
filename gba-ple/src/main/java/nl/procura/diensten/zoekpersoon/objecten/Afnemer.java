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

public class Afnemer {

  private Afnemergegevens   afnemer_actueel = new Afnemergegevens();
  private Afnemergegevens[] afnemer_historie;

  public Afnemer() {
  }

  public Afnemergegevens getAfnemer_actueel() {
    return afnemer_actueel;
  }

  public void setAfnemer_actueel(Afnemergegevens afnemer_actueel) {
    this.afnemer_actueel = afnemer_actueel;
  }

  public Afnemergegevens[] getAfnemer_historie() {
    return afnemer_historie;
  }

  public void setAfnemer_historie(Afnemergegevens[] afnemer_historie) {
    this.afnemer_historie = afnemer_historie;
  }
}
