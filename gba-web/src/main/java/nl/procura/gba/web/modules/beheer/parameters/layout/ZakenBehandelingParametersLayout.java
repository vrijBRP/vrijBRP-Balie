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

package nl.procura.gba.web.modules.beheer.parameters.layout;

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_NIEUW_ZAAKTYPES;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.vaadin.ui.Field;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.containers.ZaakTypeContainer;
import nl.procura.gba.web.components.fields.SimpleMultiField;
import nl.procura.gba.web.components.fields.values.MultiFieldValue;
import nl.procura.gba.web.modules.beheer.parameters.form.DatabaseParameterForm;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class ZakenBehandelingParametersLayout extends DatabaseParameterLayout {

  public ZakenBehandelingParametersLayout(GbaApplication application, String naam, String category) {
    super(application, naam, category);
  }

  @Override
  public void setForm(String category) {

    DatabaseParameterForm form = new DatabaseParameterForm(category) {

      @Override
      public Field newField(Field field, Property property) {
        if (isParameter(property, ZAKEN_NIEUW_ZAAKTYPES)) {
          SimpleMultiField select = (SimpleMultiField) field;
          select.setContainer(new ZaakTypeContainer());
          select.setValues(getBean().getNieuweZakenTypes().getValues());
        }

        return super.newField(field, property);
      }
    };

    super.setForm(form);
  }

  public static class toFieldConverter implements Function<String, MultiFieldValue<FieldValue>> {

    @Override
    public MultiFieldValue<FieldValue> apply(String value) {
      return new MultiFieldValue<>(getZaakTypesAsList(value));
    }

    private List<FieldValue> getZaakTypesAsList(String value) {
      return ZaakType.getByCodes(value).stream()
          .map(zt -> new FieldValue(zt.getCode(), zt.getOms()))
          .collect(Collectors.toList());
    }
  }

  public static class toDatabaseConverter implements Function<MultiFieldValue<FieldValue>, String> {

    @Override
    public String apply(MultiFieldValue<FieldValue> value) {
      return value.getValues().stream().map(FieldValue::getStringValue).collect(Collectors.joining(","));
    }
  }
}
