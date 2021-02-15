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

import static java.text.MessageFormat.format;

import java.util.Date;

import com.vaadin.data.validator.AbstractStringValidator;

import nl.procura.gba.common.DateTime;
import nl.procura.vaadin.component.field.ProDateField;

public class DatumVolgordeValidator extends AbstractStringValidator {

  private final DateValue dv1;
  private final DateValue dv2;

  public DatumVolgordeValidator(String caption1, ProDateField field1, String caption2, ProDateField field2) {
    super(format("\"{0}\" mag niet vóór \"{1}\" liggen", caption2, caption1));
    this.dv1 = () -> (Date) field1.getValue();
    this.dv2 = () -> (Date) field2.getValue();
  }

  public DatumVolgordeValidator(String caption1, DateValue dv1, String caption2, DateValue dv2) {
    super(format("\"{0}\" mag niet vóór \"{1}\" liggen", caption2, caption1));
    this.dv1 = dv1;
    this.dv2 = dv2;
  }

  @Override
  protected boolean isValidString(String value) {
    DateTime d1 = new DateTime(dv1.get());
    DateTime d2 = new DateTime(dv2.get());
    return d1.getLongDate() <= d2.getLongDate();
  }

  public interface DateValue {

    Date get();
  }
}
