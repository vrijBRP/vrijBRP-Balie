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

package nl.procura.gba.web.modules.tabellen.huwelijksambtenaar.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.BsnField;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.EmailField;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2HuwelijksAmbtenaarBean implements Serializable {

  public static final String BSN         = "bsn";
  public static final String NAAM        = "naam";
  public static final String EMAIL       = "email";
  public static final String TELEFOON    = "telefoon";
  public static final String TOELICHTING = "toelichting";
  public static final String ALIAS       = "alias";
  public static final String INGANG_GELD = "ingangGeld";
  public static final String EINDE_GELD  = "eindeGeld";

  @Field(customTypeClass = BsnField.class,
      caption = "Burgerservicenummer",
      required = true,
      width = "100px")
  private BsnFieldValue bsn = new BsnFieldValue();

  @Field(customTypeClass = EmailField.class,
      caption = "E-mail",
      width = "400px")
  private String email = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Telefoon",
      width = "300px")
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String telefoon = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Naam",
      required = true,
      width = "400px")
  private String naam = "";

  @Field(type = FieldType.TEXT_AREA,
      caption = "Alias(sen)",
      width = "400px")
  @TextArea(rows = 2,
      maxLength = 1000)
  private String alias = "";

  @Field(type = FieldType.TEXT_AREA,
      caption = "Toelichting",
      required = true,
      width = "400px")
  @TextArea(rows = 5)
  private String toelichting = "";

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum ingang",
      width = "80px")
  private DateFieldValue ingangGeld = new DateFieldValue();

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum einde",
      width = "80px")
  private DateFieldValue eindeGeld = new DateFieldValue();
}
