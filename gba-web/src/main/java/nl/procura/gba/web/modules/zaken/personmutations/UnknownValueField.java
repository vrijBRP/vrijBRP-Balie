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

package nl.procura.gba.web.modules.zaken.personmutations;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.standard.Globalfunctions;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class UnknownValueField extends GbaTextField {

  public UnknownValueField() {
    addListener((ValueChangeListener) event -> {
      FieldValue newValue = new FieldValue(Globalfunctions.astr(event.getProperty().getValue()));
      String newValueValue = newValue.getValue().toString().toLowerCase();
      if (newValueValue.equals(".") || StringUtils.containsAny(newValueValue, "onbekend", "standaardwaarde")) {
        setValue(new FieldValue(".", "Onbekend (standaardwaarde)"));
      }
    });
  }
}
