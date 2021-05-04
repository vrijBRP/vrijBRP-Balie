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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab8.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.List;
import java.util.stream.Collectors;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.components.fields.SimpleMultiField;
import nl.procura.gba.web.components.fields.values.MultiFieldValue;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Tab8DocumentenPage2Bean implements Serializable {

  public static final String OMSCHRIJVING = "omschrijving";
  public static final String ZAAK_TYPES   = "zaakTypes";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Omschrijving",
      required = true,
      width = "250px")
  private String omschrijving = "";

  @Field(customTypeClass = SimpleMultiField.class,
      caption = "Zaaktypes",
      width = "250px")
  private MultiFieldValue<FieldValue> zaakTypes = new MultiFieldValue<>();

  public List<ZaakType> getZaakTypesAsList() {
    return zaakTypes.getValues()
        .stream()
        .map(fv -> (ZaakType) fv.getValue())
        .collect(Collectors.toList());
  }
}
