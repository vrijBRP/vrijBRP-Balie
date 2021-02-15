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

package nl.procura.gbaws.db.wrappers;

import java.io.Serializable;

public class ParmWrapper implements Serializable {

  private String naam   = "";
  private String waarde = "";

  public ParmWrapper() {
  }

  public ParmWrapper(String naam, String waarde) {
    setNaam(naam);
    setWaarde(waarde);
  }

  public String getNaam() {
    return naam;
  }

  public void setNaam(String naam) {
    this.naam = naam;
  }

  public String getWaarde() {
    return waarde;
  }

  public void setWaarde(String waarde) {
    this.waarde = waarde;
  }

  @Override
  public String toString() {
    return getNaam() + " = " + getWaarde();
  }
}
