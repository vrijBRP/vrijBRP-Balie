/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.bs.naturalisatie.page40;

import static nl.procura.standard.Globalfunctions.astr;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.vaadin.ui.Component;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.TimeField;
import nl.procura.gba.web.modules.bs.naturalisatie.page40.CeremonieComponent.CeremonieComponentValue;
import nl.procura.gba.web.modules.bs.naturalisatie.valuechoice.ValueChoiceComponent;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.fieldvalues.TimeFieldValue;

import lombok.Data;
import lombok.RequiredArgsConstructor;

public class CeremonieComponent implements ValueChoiceComponent<CeremonieComponentValue> {

  private final ProDateField           dateField;
  private final TimeField              timeField;
  private final GbaNativeSelect        bijgewoondField;
  private final Map<String, Component> fields = new LinkedHashMap<>();

  public CeremonieComponent(int ceremonieNummer) {
    dateField = new ProDateField();
    dateField.setImmediate(true);
    dateField.focus();

    timeField = new TimeField();
    timeField.setMaxLength(5);
    timeField.setWidth("50px");

    bijgewoondField = new GbaNativeSelect();
    bijgewoondField.setContainerDataSource(new NLBooleanContainer());
    bijgewoondField.setItemCaptionPropertyId(NLBooleanContainer.JA_NEE);
    bijgewoondField.setWidth("70px");

    fields.put("Datum ceremonie " + ceremonieNummer, dateField);
    fields.put("Tijd ceremonie " + ceremonieNummer, timeField);
    fields.put("Bijgewoond?", bijgewoondField);
  }

  @Override
  public Map<String, Component> getFields() {
    return fields;
  }

  @Override
  public void setValue(CeremonieComponentValue value) {
    dateField.setValue(value.getDatum());
    timeField.setValue(value.getTijdstip());
    bijgewoondField.setValue(value.getBijgewoond());
  }

  @Override
  public CeremonieComponentValue getValue() {
    return new CeremonieComponentValue((Date) dateField.getValue(),
        (TimeFieldValue) timeField.getValue(),
        (Boolean) bijgewoondField.getValue());
  }

  public static CeremonieComponentValue value(Date datum, BigDecimal tijdstip, Boolean bijgewoond) {
    return new CeremonieComponentValue(datum, toTime(tijdstip), bijgewoond);
  }

  private static TimeFieldValue toTime(BigDecimal tijdstip) {
    if (tijdstip != null && tijdstip.intValue() > 0) {
      return new TimeFieldValue(tijdstip.toString());
    }
    return null;
  }

  @RequiredArgsConstructor
  @Data
  public static class CeremonieComponentValue {

    private final Date           datum;
    private final TimeFieldValue tijdstip;
    private final Boolean        bijgewoond;

    public String toString() {
      return astr(datum) + astr(tijdstip) + astr(bijgewoond);
    }
  }

}
