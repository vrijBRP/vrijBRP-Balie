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

package nl.procura.gba.web.modules.bs.onderzoek.adreslayout;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class AdresBean3 implements Serializable {

  public static final String ADRES1 = "adres1";
  public static final String ADRES2 = "adres2";
  public static final String ADRES3 = "adres3";
  public static final String LAND   = "land";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Adres",
      width = "300px")
  @TextField(maxLength = 35,
      nullRepresentation = "")
  private String adres1 = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Woonplaats",
      width = "300px")
  @TextField(maxLength = 35,
      nullRepresentation = "")
  private String adres2 = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Gemeente, district, provincie, eiland",
      width = "300px")
  @TextField(maxLength = 35,
      nullRepresentation = "")
  private String adres3 = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Land",
      required = true,
      width = "300px")
  @Select(containerDataSource = LandContainer.class)
  private FieldValue land = null;
}
