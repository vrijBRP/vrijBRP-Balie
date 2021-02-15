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

package nl.procura.gba.web.components.validators;

import com.vaadin.data.validator.AbstractValidator;

import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.GbaDateRange;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;

public class GbaDateIsBeforeValidator extends AbstractValidator {

  private final GbaDateField reference;

  public GbaDateIsBeforeValidator(String errorMessage, GbaDateField reference) {
    super(errorMessage);
    this.reference = reference;
  }

  @Override
  public boolean isValid(Object value) {
    if (!(value instanceof GbaDateFieldValue)) {
      throw new IllegalArgumentException("Value is not GbaDateFieldValue but " + value.getClass().getSimpleName());
    }
    int date = ((GbaDateFieldValue) value).toInt();
    GbaDateRange range = new GbaDateRange(reference.getGbaDateFieldValue().toInt());
    return range.isBefore(date);
  }
}
