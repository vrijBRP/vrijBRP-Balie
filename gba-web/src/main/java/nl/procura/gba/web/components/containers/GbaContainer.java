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

package nl.procura.gba.web.components.containers;

import java.util.Arrays;

import com.vaadin.data.util.IndexedContainer;

import nl.procura.vaadin.component.container.ProcuraContainer;

public abstract class GbaContainer extends IndexedContainer implements ProcuraContainer {

  public GbaContainer() {
    addContainerProperty(OMSCHRIJVING, String.class, "");
  }

  public GbaContainer(Object[] types) {
    addItems(types);
  }

  protected void addItems(Object... types) {
    Arrays.stream(types).forEach(this::addItem);
  }

  public void addItem(Object item, String description) {
    super.addItem(item).getItemProperty(OMSCHRIJVING).setValue(description);
  }
}
