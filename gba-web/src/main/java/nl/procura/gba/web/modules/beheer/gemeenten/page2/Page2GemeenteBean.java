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

package nl.procura.gba.web.modules.beheer.gemeenten.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.PosNumberField;
import nl.procura.vaadin.component.field.PostalcodeField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2GemeenteBean implements Serializable {

  public static final String CBSCODE  = "cbscode";
  public static final String GEMEENTE = "gemeente";
  public static final String ADRES    = "adres";
  public static final String PC       = "pc";
  public static final String PLAATS   = "plaats";

  @Field(customTypeClass = PosNumberField.class,
      caption = "Code",
      width = "50px",
      required = true)
  @TextField(nullRepresentation = "",
      maxLength = 4)
  private String cbscode = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Gemeente",
      width = "250px",
      required = true)
  @TextField(nullRepresentation = "")
  private String gemeente = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Adres",
      width = "250px",
      required = true)
  @TextField(nullRepresentation = "")
  private String adres = "";

  @Field(customTypeClass = PostalcodeField.class,
      caption = "Postcode",
      width = "70px",
      required = true)
  @TextField(nullRepresentation = "")
  private FieldValue pc = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Plaats",
      width = "250px",
      required = true)
  @TextField(nullRepresentation = "")
  private String plaats = "";
}
