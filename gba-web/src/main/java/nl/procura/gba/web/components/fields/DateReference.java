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

import static java.lang.String.format;
import static nl.procura.standard.Globalfunctions.along;

import java.util.Collection;
import java.util.Date;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Field;

import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

/**
 * Utility class to link a date field to a select box to validate the value of
 * the select box.
 */
public final class DateReference {

  private DateReference() {
  }

  public static void setField(AbstractSelect select, Field field) {
    Property.ValueChangeListener dateListener = e -> setDate(select, getDate(e));
    field.addListener(dateListener);
    dateListener.valueChange(new Field.ValueChangeEvent(field));
  }

  public static void setFieldWithValidator(AbstractSelect select, Field field) {
    setField(select, field);
    select.addValidator(new DateRangeValidator(field));
  }

  public static void setDate(AbstractSelect select, long date) {
    setCaptions(select, new GbaDateRange(date));
  }

  private static void setCaptions(AbstractSelect select, GbaDateRange date) {
    @SuppressWarnings("unchecked")
    Collection<FieldValue> itemIds = (Collection<FieldValue>) select.getItemIds();
    itemIds.forEach(i -> setItemCaption(select, date, i));
  }

  private static void setItemCaption(AbstractSelect select, GbaDateRange date, FieldValue i) {
    if (select.getItemCaptionMode() == AbstractSelect.ITEM_CAPTION_MODE_PROPERTY) {
      select.getItem(i).getItemProperty(select.getItemCaptionPropertyId()).setValue(getItemDescription(i, date));
    } else {
      select.setItemCaption(i, getItemDescription(i, date));
    }
  }

  private static String getItemDescription(FieldValue value, GbaDateRange date) {
    if (date.isValidFor(value.getDateIn(), value.getDateEnd())) {
      return format("%s (%s)", value.getDescription(), value.getValue());
    }
    return format("%s (niet geldig op %s)", value.getDescription(), date.toString());
  }

  private static long getDate(Property.ValueChangeEvent e) {
    return getDate(e.getProperty().getValue());
  }

  private static long getDate(Object value) {
    if (value instanceof FieldValue) {
      return ((FieldValue) value).getLongValue();
    }
    if (value instanceof Date) {
      return along(new ProcuraDate((Date) value).getSystemDate());
    }
    return -1;
  }

  private static class DateRangeValidator implements Validator {

    private final Field field;

    public DateRangeValidator(Field field) {
      this.field = field;
    }

    @Override
    public void validate(Object value) throws InvalidValueException {
      if (!isValid(value)) {
        throw new InvalidValueException(format("%s is niet geldig op %s", value, field.getValue()));
      }
    }

    @Override
    public boolean isValid(Object value) {
      FieldValue tableValue = (FieldValue) value;
      GbaDateRange fieldDate = new GbaDateRange(getDate(field.getValue()));
      return fieldDate.isValidFor(tableValue.getDateIn(), tableValue.getDateEnd());
    }
  }
}
