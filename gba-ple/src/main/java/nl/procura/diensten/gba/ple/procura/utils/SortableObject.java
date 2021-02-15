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

package nl.procura.diensten.gba.ple.procura.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortableObject<T> implements Comparable<SortableObject> {

  private String hist  = "";
  private int    vCat  = 1;
  private long   anr   = 0;
  private int    dGeld = 0;
  private int    dGba  = 0;
  private int    vGeld = 0;
  private T      object;

  public SortableObject() {
  }

  // Sortmethod
  @Override
  public int compareTo(SortableObject o) {
    int cVCat = compare(getVCat(), o.getVCat());
    int cType = compare(getHist(), o.getHist());
    int cDGeld = compare(getDGeld(), o.getDGeld());
    int cDGba = compare(getDGba(), o.getDGba());
    int cVGeld = compare(getVGeld(), o.getVGeld());

    if (cVCat == 0) {
      if (cType == 0) {
        if (cDGeld == 0) {
          if (cDGba == 0) {
            // Maakt niet uit, alles is hetzelfde
            return cVGeld;
          }
          return cDGba;
        }
        return cDGeld;
      }
      return cType;
    }
    return cVCat;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return (obj instanceof SortableObject) && super.equals(obj);
  }

  private int compare(int i, int j) {
    return Integer.compare(j, i);
  }

  private int compare(String i, String j) {
    return i.compareTo(j);
  }
}
