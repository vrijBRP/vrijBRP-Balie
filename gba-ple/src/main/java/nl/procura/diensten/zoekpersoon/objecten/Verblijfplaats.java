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

public class Verblijfplaats {

  private Verblijfplaatsgegevens   verblijfplaats_actueel;
  private Verblijfplaatsgegevens[] verblijfplaats_historie;

  public Verblijfplaats() {
  }

  public Verblijfplaatsgegevens getVerblijfplaats_actueel() {
    return verblijfplaats_actueel;
  }

  public void setVerblijfplaats_actueel(Verblijfplaatsgegevens verblijfplaats_actueel) {
    this.verblijfplaats_actueel = verblijfplaats_actueel;
  }

  public Verblijfplaatsgegevens[] getVerblijfplaats_historie() {
    return verblijfplaats_historie;
  }

  public void setVerblijfplaats_historie(Verblijfplaatsgegevens[] verblijfplaats_historie) {
    this.verblijfplaats_historie = verblijfplaats_historie;
  }
}
