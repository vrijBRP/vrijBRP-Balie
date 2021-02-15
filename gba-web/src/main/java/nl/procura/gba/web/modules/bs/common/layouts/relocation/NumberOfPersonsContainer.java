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

package nl.procura.gba.web.modules.bs.common.layouts.relocation;

import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class NumberOfPersonsContainer extends ArrayListContainer {

  public NumberOfPersonsContainer() {
    this(true);
  }

  public NumberOfPersonsContainer(boolean caseOfEntry) {

    addItem(new FieldValue("", ""));

    for (int i = 1; i <= 10; i++) {
      addItem(new FieldValue(i, astr(i)));
    }

    if (caseOfEntry) {
      addItem(new FieldValue(20, "> 10"));
    } else {
      addItem(new FieldValue(-1, "Niet aangegeven"));
      addItem(new FieldValue(-2, "> 10")); // Backwards compatibility
      addItem(new FieldValue(20, "> 10"));
    }
  }
}
