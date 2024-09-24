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

package nl.procura.gba.web.modules.bs.common.pages.zoekpage;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.BagPopupField;
import nl.procura.gba.web.components.fields.GbaBsnField;
import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.field.PostalcodeField;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class BsZoekBean implements Serializable {

  public static final String F_TYPE = "type";
  public static final String F_BSN = "bsn";
  public static final String F_GEBOORTEDATUM = "geboortedatum";
  public static final String F_HNR = "hnr";
  public static final String F_POSTCODE = "postcode";
  public static final String F_GESLACHTSNAAM = "geslachtsnaam";
  public static final String F_VOORNAMEN = "voornamen";
  public static final String F_ADRES = "adres";

  @Field(type = FieldType.LABEL,
      caption = "Betreft",
      width = "200px")
  private String type = "";

  @Field(customTypeClass = GbaBsnField.class,
      caption = "Burgerservicenummer",
      width = "200px",
      description = "Burgerservice nummer")
  @Immediate
  private String bsn = "";

  @Field(customTypeClass = GbaDateField.class,
      caption = "Geboortedatum",
      width = "200px",
      description = "Geboortedatum")
  private GbaDateFieldValue geboortedatum = null;

  @Field(customTypeClass = PostalcodeField.class,
      caption = "Postcode / Huisnr.",
      width = "100px",
      description = "Postcode")
  private String postcode = "";

  @Field(customTypeClass = NumberField.class,
      caption = "Huisnummer",
      width = "95px",
      description = "Hnr.")
  private String hnr = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geslachtsnaam",
      width = "150px")
  private String geslachtsnaam = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Voornamen",
      width = "150px")
  private String voornamen = "";

  @Field(customTypeClass = BagPopupField.class,
      caption = "BAG-adres",
      description = "Adres",
      width = "482px")
  private Address adres = null;
}
