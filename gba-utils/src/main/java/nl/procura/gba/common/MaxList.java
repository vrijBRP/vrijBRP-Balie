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

package nl.procura.gba.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Een lijst met een maximum
 */
public class MaxList<T> extends ArrayList<T> {

  private final int max;

  public MaxList(int max) {
    this.max = max;
  }

  public MaxList(List<T> collection, int max) {
    this(max);
    addAll(collection);
  }

  @Override
  public void add(int index, T element) {

    if (!isMaxGezet() || size() < max) {
      super.add(index, element);
    }
  }

  @Override
  public boolean add(T e) {

    if (!isMaxGezet() || size() < max) {
      return super.add(e);
    }

    return false;
  }

  public boolean addAll(List<T> c) {
    return super.addAll(!isMaxGezet() ? c : MiscUtils.partition(c, max - size()));
  }

  public boolean isMaxGezet() {
    return max > 0;
  }

  public boolean isMaxBereikt() {
    return isMaxGezet() && size() >= max;
  }
}
