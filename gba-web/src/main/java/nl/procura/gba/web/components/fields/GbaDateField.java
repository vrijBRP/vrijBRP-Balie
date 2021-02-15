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

import com.vaadin.data.Validator;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;

import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.validator.DatumValidator;

public class GbaDateField extends GbaTextField implements BlurListener {

  public GbaDateField() {
    this(null);
  }

  public GbaDateField(String naam) {
    this(naam, new DatumValidator("Incorrecte datum"));
  }

  public GbaDateField(String naam, Validator validator) {

    super(naam);
    setValidationVisible(true);
    setMaxLength(10);
    addValidator(validator);
    addListener((BlurListener) this);
  }

  @Override
  public void blur(BlurEvent event) {
  }

  @Override
  public Class<GbaDateField> getType() {
    return GbaDateField.class;
  }

  @Override
  public Object getValue() {
    return getGbaDateFieldValue();
  }

  public GbaDateFieldValue getGbaDateFieldValue() {

    String stringValue = astr(super.getValue());
    ProcuraDate procuraDate = new ProcuraDate(stringValue).setAllowedFormatExceptions(true);

    GbaDateFieldValue dateFieldValue;
    if (procuraDate.isAutocompleteCorrect()) {

      procuraDate.autocomplete();
      dateFieldValue = new GbaDateFieldValue(procuraDate.getSystemDate());
    } else {

      dateFieldValue = new GbaDateFieldValue("");
      dateFieldValue.setDescription(stringValue);
    }

    return dateFieldValue;
  }

  @Override
  protected void fireEvent(Event event) {

    String stringValue = event.getSource().toString();

    if (fil(stringValue)) {

      ProcuraDate procuraDate = new ProcuraDate(stringValue).setAllowedFormatExceptions(true);

      if (procuraDate.isAutocompleteCorrect()) {
        procuraDate.autocomplete();
        setValue(stringValue, procuraDate.getFormatDate());
      }
    }

    super.fireEvent(event);
  }
}
