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

package nl.procura.gba.web.components.layouts.form;

import static nl.procura.standard.Globalfunctions.along;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Field;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.validators.TeletexValidator;
import nl.procura.vaadin.component.field.FieldUtils;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.form.tableform.TableForm;

public abstract class GbaForm<T> extends TableForm {

  public static String WIDTH_130 = "130px";

  public GbaForm() {
    setColumnWidths(WIDTH_130, "");
    setReadonlyAsText(true); // Velden die readonly zijn als String tonen i.p.v. veld.

    // true == updates field from datasource.
    // Readthrough heeft bug waardoor changelisteners ook worden aangeroepen bij commit.
    setReadThrough(false);

    // true == update datasource from field
    setWriteThrough(false);
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

  @Override
  public GbaApplication getApplication() {
    return (GbaApplication) super.getApplication();
  }

  @Override
  public T getBean() {
    return (T) super.getBean();
  }

  @Override
  public T getNewBean() {
    return (T) super.getNewBean();
  }

  public Object getValue(String field) {
    return getField(field).getValue();
  }

  public <F> F getValue(String field, Class<F> type) {
    return type.cast(getField(field).getValue());
  }

  public boolean hasParent() {
    return getParent() != null;
  }

  public boolean isAdded() {
    return getParent() != null;
  }

  @Override
  public Field newField(Field field, Property property) {
    FieldUtils.addDiacritics(field);
    TeletexValidator.add(field);
    return super.newField(field, property);
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
}
