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

package nl.procura.gba.web.modules.beheer.verkiezing.page4;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page4VerkiezingBean implements Serializable {

  public static final String F_VERK          = "verkiezing";
  public static final String F_CODE          = "code";
  public static final String F_ANR           = "anr";
  public static final String F_PASNR         = "pasNr";
  public static final String F_GEBOORTEDATUM = "geboortedatum";
  public static final String F_GESLACHT      = "geslacht";
  public static final String F_NAAM          = "naam";
  public static final String F_ADRES         = "adres";
  public static final String F_TOEGEVOEGD    = "toegevoegd";
  public static final String F_AAND          = "aanduiding";

  @Field(type = Field.FieldType.LABEL, caption = "Verkiezing")
  private String verkiezing = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Code")
  private String code = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Pasnummer")
  private String pasNr = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "A-nummer")
  private String anr;

  @Field(type = Field.FieldType.LABEL,
      caption = "Geboortedatum")
  private String geboortedatum = null;

  @Field(type = Field.FieldType.LABEL,
      caption = "Geslacht")
  private String geslacht = null;

  @Field(type = Field.FieldType.LABEL,
      caption = "Naam")
  private String naam = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Adres")
  private String adres = "";

  @Field(type = Field.FieldType.LABEL,
      caption = "Stempas handmatig toegevoegd",
      width = "600px",
      readOnly = true)
  private String toegevoegd;

  @Field(type = Field.FieldType.LABEL,
      caption = "Aanduiding",
      width = "600px",
      readOnly = true)
  private String aanduiding;

}
