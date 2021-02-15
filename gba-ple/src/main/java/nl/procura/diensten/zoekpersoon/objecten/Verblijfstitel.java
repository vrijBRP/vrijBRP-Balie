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

public class Verblijfstitel {

  private Verblijfstitelgegevens   verblijfstitel_actueel;
  private Verblijfstitelgegevens[] verblijfstitel_historie;

  public Verblijfstitel() {
  }

  public Verblijfstitelgegevens getVerblijfstitel_actueel() {
    return verblijfstitel_actueel;
  }

  public void setVerblijfstitel_actueel(Verblijfstitelgegevens verblijfstitel_actueel) {
    this.verblijfstitel_actueel = verblijfstitel_actueel;
  }

  public Verblijfstitelgegevens[] getVerblijfstitel_historie() {
    return verblijfstitel_historie;
  }

  public void setVerblijfstitel_historie(Verblijfstitelgegevens[] verblijfstitel_historie) {
    this.verblijfstitel_historie = verblijfstitel_historie;
  }
}
