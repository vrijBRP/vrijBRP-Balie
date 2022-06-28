/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.beheer.verkiezing.page3.exporting;

public enum ROSScheidingstekenType {

  KOMMA(',', "Komma"),
  PUNT_KOMMA(';', "Puntkomma");

  private final char   karakter;
  private final String oms;

  ROSScheidingstekenType(char karakter, String oms) {
    this.karakter = karakter;
    this.oms = oms;
  }

  public char getKarakter() {
    return karakter;
  }

  public String getOms() {
    return oms;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
