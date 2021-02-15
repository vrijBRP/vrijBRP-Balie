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

package com.vaadin.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ComponentContainers {

  private ComponentContainers() {
  }

  public static List<Component> allComponents(ComponentContainer container) {
    ArrayList<Component> components = new ArrayList<>();
    addComponents(components, container);
    return components;
  }

  private static void addComponents(ArrayList<Component> list, ComponentContainer container) {
    Iterator<Component> iterator = container.getComponentIterator();
    iterator.forEachRemaining(c -> {
      list.add(c);
      if (c instanceof ComponentContainer) {
        addComponents(list, (ComponentContainer) c);
      }
    });
  }
}
