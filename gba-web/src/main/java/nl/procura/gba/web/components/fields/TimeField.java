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

package nl.procura.gba.web.components.fields;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;

import nl.procura.vaadin.component.field.fieldvalues.TimeFieldValue;

public class TimeField extends GbaTextField implements BlurListener {

  private String lastValue = "";

  public TimeField() {
    init();
  }

  @Override
  public void blur(BlurEvent event) {
  }

  @Override
  public Class<TimeField> getType() {
    return TimeField.class;
  }

  @Override
  public Object getValue() {
    return new TimeFieldValue(astr(super.getValue()));
  }

  @Override
  protected void fireEvent(Event event) {

    String value = event.getSource().toString();

    if (!astr(value).equals(astr(lastValue))) {

      if (fil(value)) {

        TimeFieldValue fieldValue = new TimeFieldValue(value);

        if (fieldValue.isCorrect()) {

          setValue(value, fieldValue.getDescription());
        }

        lastValue = value;
      }

      super.fireEvent(event);
    }
  }

  private void init() {

    addValidator(new TimeValidator());
    setValidationVisible(true);
    addListener((BlurListener) this);
  }

  public class TimeValidator extends AbstractStringValidator {

    public TimeValidator() {
      this("De gegeven tijd is ongeldig.");
    }

    private TimeValidator(String errorMessage) {
      super(errorMessage);
    }

    @Override
    protected boolean isValidString(String time) {
      return new TimeFieldValue(time).isCorrect();
    }
  }
}
