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

package nl.procura.diensten.gba.ple.base;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class BasePLFilter {

  public final Map<Integer, LinkedHashSet<Integer>> elements = new HashMap<>();

  public void addElem(int catCode, int elementCode) {
    if (elements.containsKey(catCode)) {
      elements.get(catCode).add(elementCode);

    } else {
      LinkedHashSet<Integer> set = new LinkedHashSet<>();
      set.add(elementCode);
      elements.put(catCode, set);
    }
  }

  /**
   * If elements map is empty than allow all elements
   */
  public boolean isAllowed(int catCode) {
    return elements.isEmpty() || elements.containsKey(catCode);
  }

  public boolean isAllowed(int catCode, int elementCode) {
    return elements.isEmpty() || (elements.containsKey(catCode)
        && elements.get(catCode).contains(elementCode));
  }
}
