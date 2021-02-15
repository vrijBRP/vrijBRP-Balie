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

import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DateOfEntryContainer extends ArrayListContainer {

  public static final String CUSTOM        = "N";
  public static final String DATE_OF_BIRTH = "G";
  public static final String UNKNOWN_DATE  = "O";

  public DateOfEntryContainer() {
    addItem(new FieldValue(DATE_OF_BIRTH, "Geboortedatum"));
    addItem(new FieldValue("O", "Onbekend"));
    addItem(new FieldValue(CUSTOM, "Zelf invullen"));
  }
}
