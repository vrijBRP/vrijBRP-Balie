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

package nl.procura.gba.web.modules.bs.overlijden.correspondentie;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.fields.HuisnummerVeld;
import nl.procura.gba.web.services.bs.algemeen.enums.CommunicatieType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.field.EmailField;
import nl.procura.vaadin.component.field.PostalcodeField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class CorrespondentieBean implements Serializable {

  public static final String COMMUNICATIE_TYPE = "communicatieType";
  public static final String ORGANISATIE       = "organisatie";
  public static final String AFDELING          = "afdeling";
  public static final String NAAM              = "naam";
  public static final String EMAIL             = "email";
  public static final String TELEFOON          = "telefoon";
  public static final String STRAAT            = "straat";
  public static final String HNR               = "hnr";
  public static final String HNRL              = "hnrL";
  public static final String HNRT              = "hnrT";
  public static final String POSTCODE          = "postcode";
  public static final String PLAATS            = "plaats";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Wijze van communiceren",
      required = true,
      width = "250px")
  @Immediate
  @Select(containerDataSource = CommunicatieTypeContainer.class)
  private CommunicatieType communicatieType = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Organisatie",
      width = "250px")
  @TextField(nullRepresentation = "")
  private String organisatie = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Afdeling",
      width = "250px")
  @TextField(nullRepresentation = "")
  private String afdeling = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geaddresseerde",
      width = "250px")
  @TextField(nullRepresentation = "")
  private String naam = null;

  @Field(customTypeClass = EmailField.class,
      caption = "E-mail",
      width = "250px")
  @TextField(nullRepresentation = "")
  private String email = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Telefoon",
      width = "250px")
  @TextField(nullRepresentation = "")
  private String telefoon = null;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Adres",
      required = true,
      width = "200px")
  @TextField(maxLength = 35)
  @InputPrompt(text = "Straat")
  private String straat = "";

  @Field(customTypeClass = HuisnummerVeld.class,
      caption = "Huisnummer",
      width = "30px",
      description = "Huisnummer")
  @InputPrompt(text = "Nr.")
  private String hnr = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Huisletter",
      width = "30px",
      description = "Huisletter")
  @InputPrompt(text = "L")
  private String hnrL = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Toevoeging",
      width = "50px",
      description = "Toevoeging")
  @InputPrompt(text = "T")
  private String hnrT = "";

  @Field(customTypeClass = PostalcodeField.class,
      caption = "Postcode / Plaats",
      width = "60px")
  private FieldValue postcode = null;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Plaats",
      required = true,
      width = "134px")
  @TextField(maxLength = 40)
  private String plaats = "";
}
