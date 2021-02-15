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

package nl.procura.gbaws.web.vaadin.layouts.forms;

import static nl.procura.standard.Globalfunctions.along;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Field;

import nl.procura.gbaws.web.vaadin.application.GbaWsApplication;
import nl.procura.vaadin.component.field.FieldUtils;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.form.tableform.TableForm;

public class DefaultForm extends TableForm {

  public DefaultForm() {
    setColumnWidths("130px", "");
    setReadonlyAsText(true); // Velden die readonly zijn als String tonen i.p.v. veld.
  }

  @Override
  public Field newField(Field field, Property property) {
    FieldUtils.addDiacritics(field);
    return super.newField(field, property);
  }

  @Override
  public GbaWsApplication getApplication() {
    return (GbaWsApplication) super.getApplication();
  }

  public boolean hasParent() {
    return getParent() != null;
  }

  @SuppressWarnings("unused")
  public <T> T getValue(Class<T> type, String field) {
    return (T) getField(field).getValue();
  }

  public Object getValue(String field) {
    return getField(field).getValue();
  }

  public void setValue(String field, Object value) {
    getField(field).setValue(value);
  }

  protected long getFieldValueCode(String field) {

    FieldValue fieldValue = (FieldValue) getField(field).getValue();
    if (fieldValue != null) {
      return along(fieldValue.getValue());
    }

    return -1;
  }

  /**
   * Alleen committen als deze is geattached
   */
  @Override
  public void commit() throws SourceException, InvalidValueException {
    if (getParent() != null) {
      super.commit();
    }
  }
}
