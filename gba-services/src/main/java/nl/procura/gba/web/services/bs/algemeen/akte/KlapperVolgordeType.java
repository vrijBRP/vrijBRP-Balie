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

package nl.procura.gba.web.services.bs.algemeen.akte;

public enum KlapperVolgordeType {

  DATUM_OPLOPEND("Datum (oplopend)", true, true),
  VNR_OPLOPEND("Volgnummer (oplopend)", false, true),
  VNR_AFLOPEND("Volgnummer (aflopend)", false, true),
  DATUM_AFLOPEND("Datum (aflopend)", false, true),
  NAAM_OPLOPEND("Geslachtsnaam (oplopend)", true, false),
  NAAM_AFLOPEND("Geslachtsnaam (aflopend)", false, false);

  private final String  oms;
  private final boolean chronologisch;
  private final boolean afdrukbaar;

  KlapperVolgordeType(String oms, boolean afdrukbaar, boolean chronologisch) {
    this.oms = oms;
    this.afdrukbaar = afdrukbaar;
    this.chronologisch = chronologisch;
  }

  public String getOms() {
    return oms;
  }

  public String getType() {
    return chronologisch ? "Chronologische" : "Alfabetische";
  }

  public boolean isAfdrukbaar() {
    return afdrukbaar;
  }

  public boolean isChronologisch() {
    return chronologisch;
  }

  @Override
  public String toString() {
    return getOms();
  }

}
