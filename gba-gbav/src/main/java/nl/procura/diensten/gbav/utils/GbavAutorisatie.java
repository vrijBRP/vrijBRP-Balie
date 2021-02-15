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

package nl.procura.diensten.gbav.utils;

import java.util.TreeMap;
import java.util.TreeSet;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gbav.exceptions.GbavArgumentException;

public class GbavAutorisatie {

  private TreeMap<Integer, TreeSet<Integer>> elements = new TreeMap<>();

  public void addElement(GBACat cat, GBAElem element) {
    addElement(cat.getCode(), element.getCode());
  }

  public void addElement(int cat, int element) {

    TreeSet<Integer> hm = getElements().get(cat);
    if (hm != null) {
      hm.add(element);
    } else {
      hm = new TreeSet<>();
      hm.add(element);
      elements.put(cat, hm);
    }
  }

  public TreeSet<Integer> getElements(int cat) {

    TreeSet<Integer> hm = getElements().get(cat);
    if (hm != null) {
      return hm;
    }

    return new TreeSet<>();
  }

  public boolean isElement(int cat, int element) {
    return getElements(cat).contains(element);
  }

  public void checkElement(int cat_nr, int element_nr) {

    if (!isElement(cat_nr, element_nr)) {
      throw new GbavArgumentException(
          GBACat.getByCode(cat_nr),
          GBAElem.getByCode(element_nr),
          cat_nr + "." + element_nr);
    }
  }

  public TreeMap<Integer, TreeSet<Integer>> getElements() {
    return elements;
  }

  public void setElements(TreeMap<Integer, TreeSet<Integer>> elementen) {
    this.elements = elementen;
  }
}
