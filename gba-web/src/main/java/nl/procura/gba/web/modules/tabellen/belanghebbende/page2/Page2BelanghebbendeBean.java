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

package nl.procura.gba.web.modules.tabellen.belanghebbende.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.bs.ontbinding.page30.AanhefContainer;
import nl.procura.gba.web.services.gba.basistabellen.belanghebbende.BelanghebbendeType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.EmailField;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2BelanghebbendeBean implements Serializable {

  public static final String TYPE        = "type";
  public static final String NAAM        = "naam";
  public static final String TAV_AANHEF  = "tavAanhef";
  public static final String TAV_VOORL   = "tavVoorl";
  public static final String TAV_NAAM    = "tavNaam";
  public static final String ADRES       = "adres";
  public static final String POSTCODE    = "postcode";
  public static final String PLAATS      = "plaats";
  public static final String LAND        = "land";
  public static final String TELEFOON    = "telefoon";
  public static final String EMAIL       = "email";
  public static final String INGANG_GELD = "ingangGeld";
  public static final String EINDE_GELD  = "eindeGeld";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Type",
      required = true,
      width = "300px")
  @Select(containerDataSource = BelanghebbendeContainer.class)
  private BelanghebbendeType type = null;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Naam",
      width = "300px",
      required = true)
  @TextField(maxLength = 75,
      nullRepresentation = "")
  private String naam = "";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Ter attentie van",
      width = "80px")
  @Select(containerDataSource = AanhefContainer.class)
  @InputPrompt(text = "Aanhef")
  private FieldValue tavAanhef = null;

  @Field(type = FieldType.TEXT_FIELD,
      width = "70px")
  @InputPrompt(text = "Voorletters")
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String tavVoorl = "";

  @Field(type = FieldType.TEXT_FIELD,
      width = "140px")
  @TextField(maxLength = 250,
      nullRepresentation = "")
  @InputPrompt(text = "Naam")
  private String tavNaam = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Adres",
      required = true,
      width = "300px",
      description = "Adres")
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String adres = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Postcode",
      width = "100px")
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String postcode = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Plaats",
      required = true,
      width = "300px")
  @TextField(maxLength = 40,
      nullRepresentation = "")
  private String plaats = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Land",
      required = true,
      width = "300px")
  @Select(containerDataSource = LandContainer.class)
  private FieldValue land = null;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Telefoon",
      width = "300px")
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String telefoon = "";

  @Field(customTypeClass = EmailField.class,
      caption = "E-mail",
      width = "300px")
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String email = "";

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum ingang",
      width = "80px")
  private DateFieldValue ingangGeld = new DateFieldValue();

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum einde",
      width = "80px")
  private DateFieldValue eindeGeld = new DateFieldValue();
}
