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

package nl.procura.gba.web.services.beheer.kassa;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Geeft setjes terug met alle mogelijke combinaties van een list
 */
public class KassaCombinations {

  public static <T> List<Set<T>> getCombinationsFor(List<T> group) {
    List<Set<T>> list = new ArrayList<>();
    for (int i = 1; i <= group.size(); i++) {
      list.addAll(getCombinationsFor(group, i));
    }
    return list;
  }

  private static <T> void emptySet(Set<Set<T>> resultingCombinations) {
    resultingCombinations.add(new LinkedHashSet<>());
  }

  private static <T> Set<Set<T>> getCombinationsFor(List<T> group, int subsetSize) {
    Set<Set<T>> resultingCombinations = new LinkedHashSet<>();
    int totalSize = group.size();

    if (subsetSize == 0) {
      emptySet(resultingCombinations);
    } else if (subsetSize <= totalSize) {
      List<T> remainingElements = new ArrayList<>(group);
      T x = popLast(remainingElements);

      Set<Set<T>> combinationsExclusiveX = getCombinationsFor(remainingElements, subsetSize);
      Set<Set<T>> combinationsInclusiveX = getCombinationsFor(remainingElements, subsetSize - 1);

      for (Set<T> combination : combinationsInclusiveX) {
        combination.add(x);
      }

      resultingCombinations.addAll(combinationsExclusiveX);
      resultingCombinations.addAll(combinationsInclusiveX);
    }
    return resultingCombinations;
  }

  private static <T> T popLast(List<T> elementsExclusiveX) {
    return elementsExclusiveX.remove(elementsExclusiveX.size() - 1);
  }
}
