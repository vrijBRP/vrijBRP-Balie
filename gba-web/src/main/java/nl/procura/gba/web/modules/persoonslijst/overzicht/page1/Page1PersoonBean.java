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

package nl.procura.gba.web.modules.persoonslijst.overzicht.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1PersoonBean implements Serializable {

  public static final String ANR           = "anr";
  public static final String BSN           = "bsn";
  public static final String GESLACHTSNAAM = "geslachtsnaam";
  public static final String NATIONALITEIT = "nationaliteit";
  public static final String VOORVOEGSEL   = "voorvoegsel";
  public static final String GEBOREN       = "geboren";
  public static final String TITEL         = "titel";
  public static final String OVERLIJDEN    = "overlijden";
  public static final String VOORNAAM      = "voornaam";
  public static final String NAAMGEBRUIK   = "naamgebruik";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "A-nummer")
  private String anr = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "BSN")
  private String bsn = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geslachtsnaam")
  private String geslachtsnaam = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Nationaliteit")
  private String nationaliteit = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Voorvoegsel")
  private String voorvoegsel = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geboren")
  private String geboren = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Titel")
  private String titel = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Overlijden")
  private String overlijden = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Voornaam")
  private String voornaam = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Naamgebruik")
  private String naamgebruik = "";
}
