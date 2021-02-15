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

package nl.procura.gba.web.modules.beheer.profielen.page10.tab2.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.TextField;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2IndicatiesBean implements Serializable {

  public static final String BUTTON    = "button";
  public static final String PROBEV    = "probev";
  public static final String INDICATIE = "indicatie";
  public static final String OMS       = "oms";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Indicatie",
      required = true)
  private String indicatie = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "PROBEV code",
      required = true)
  private String probev = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Label button",
      required = true)
  @TextField(maxLength = 20)
  private String button = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Omschrijving")
  private String oms = "";
}
