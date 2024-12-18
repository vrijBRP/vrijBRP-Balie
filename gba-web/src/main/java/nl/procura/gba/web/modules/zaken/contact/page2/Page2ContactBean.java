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

package nl.procura.gba.web.modules.zaken.contact.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.actueel.LandActueelContainer;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.ProComboBox;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2ContactBean implements Serializable {

  public static final String VELD = "veld";
  public static final String LAND = "land";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "veld",
      width = "300px")
  @TextField(maxLength = 240)
  private String veld = "";

  @Field(customTypeClass = ProComboBox.class,
      caption = "Land",
      width = "300px")
  @Select(containerDataSource = LandActueelContainer.class)
  private FieldValue land;

  public Page2ContactBean(String veld) {
    setVeld(veld);
  }
}
