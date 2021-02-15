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

package nl.procura.gba.web.services.zaken.protocol;

public enum ProtocolleringGroep {

  DATUM(0, "Datum"),
  ANUMMER(1, "A-nummer"),
  GEBRUIKER(2, "Gebruiker");

  private int    groep = 0;
  private String id    = "";

  ProtocolleringGroep(int groep, String id) {
    this.setGroep(groep);
    this.setId(id);
  }

  public int getGroep() {
    return groep;
  }

  public void setGroep(int groep) {
    this.groep = groep;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return id;
  }
}
