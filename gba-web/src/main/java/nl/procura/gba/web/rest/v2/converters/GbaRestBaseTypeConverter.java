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

package nl.procura.gba.web.rest.v2.converters;

import static java.util.Optional.ofNullable;

import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.common.EnumWithCode;
import nl.procura.gba.web.rest.v2.model.base.GbaRestEnum;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestTabelWaarde;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class GbaRestBaseTypeConverter {

  public static GbaRestTabelWaarde toTableRecord(Object code, String descr) {
    GbaRestTabelWaarde record = new GbaRestTabelWaarde();
    record.setWaarde(String.valueOf(code));
    record.setOmschrijving(descr);
    return record;
  }

  public static GbaRestTabelWaarde toTableRecord(FieldValue fieldValue) {
    return toTableRecord(fieldValue.getStringValue(), fieldValue.getDescription());
  }

  public static Long toBsn(BsnFieldValue fieldValue) {
    if (fieldValue.isCorrect()) {
      return fieldValue.getLongValue();
    }
    return null;
  }

  public static void setIfPresent(Integer value, Consumer<Integer> consumer) {
    if (value != null && value >= 0) {
      consumer.accept(value);
    }
  }

  public static void setIfPresent(String value, Consumer<String> consumer) {
    if (StringUtils.isNotBlank(value)) {
      consumer.accept(value);
    }
  }

  public static <T> void setIfPresent(T value, Consumer<T> consumer) {
    if (value != null && StringUtils.isNotBlank(value.toString())) {
      consumer.accept(value);
    }
  }

  public static <T extends GbaRestEnum<?>> void setIfPresent(EnumWithCode<String> value, T[] values,
      Consumer<T> consumer) {
    if (StringUtils.isNotBlank(value.getCode())) {
      consumer.accept(GbaRestEnum.toEnum(values, value.getCode()));
    }
  }

  public static void setIfPresent(FieldValue value, Consumer<GbaRestTabelWaarde> consumer) {
    ofNullable(value).ifPresent(fieldValue -> consumer.accept(toTableRecord(fieldValue)));
  }
}
