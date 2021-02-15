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

import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.component.field.BsnField;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.field.PostalcodeField;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class BsZoekBean implements Serializable {

  public static final String TYPE          = "type";
  public static final String BSN           = "bsn";
  public static final String GEBOORTEDATUM = "geboortedatum";
  public static final String HNR           = "hnr";
  public static final String POSTCODE      = "postcode";

  @Field(type = FieldType.LABEL,
      caption = "Betreft",
      width = "200px")
  private String type = "";

  @Field(customTypeClass = BsnField.class,
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
}
