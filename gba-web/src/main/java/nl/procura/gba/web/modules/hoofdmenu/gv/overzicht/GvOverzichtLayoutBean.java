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

package nl.procura.gba.web.modules.hoofdmenu.gv.overzicht;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class GvOverzichtLayoutBean implements Serializable {

  public static final String NAAM          = "naam";
  public static final String GESLACHT      = "geslacht";
  public static final String GEBOORTE      = "geboorte";
  public static final String ADRES_PERSOON = "adresPersoon";
  public static final String STATUS        = "status";

  public static final String DATUM_ONTVANGST = "datumOntvangst";
  public static final String GRONDSLAG       = "grondslag";
  public static final String AFNEMER         = "afnemer";
  public static final String BA_AFWEGING     = "belangenAfweging";
  public static final String TOEKENNING      = "toekenning";

  public static final String INFORMATIEVRAGER = "informatievrager";
  public static final String TAV              = "tav";
  public static final String ADRES_AANVRAGER  = "adresAanvrager";
  public static final String PC               = "pc";
  public static final String EMAIL            = "email";
  public static final String KENMERK          = "kenmerk";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Naam")
  private String naam = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geslacht")
  private String geslacht = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geboorte")
  private String geboorte = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Adres")
  private String adresPersoon = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Status")
  private String status = "";

  @Field(type = FieldType.LABEL,
      caption = "Datum ontvangst",
      width = "97px")
  private String datumOntvangst = null;

  @Field(type = FieldType.LABEL,
      caption = "Grondslag",
      required = true)
  @Select()
  @Immediate
  private String grondslag = null;

  @Field(type = FieldType.LABEL,
      caption = "Door",
      required = true)
  private String afnemer = null;

  @Field(type = FieldType.LABEL,
      caption = "Toegekend")
  @Immediate
  private String toekenning = null;

  @Field(type = FieldType.LABEL,
      caption = "Informatievrager",
      width = "300px")
  private String informatievrager = null;

  @Field(type = FieldType.LABEL,
      caption = "Ter attentie van",
      width = "300px")
  private String tav = null;

  @Field(type = FieldType.LABEL,
      caption = "Adres",
      width = "300px")
  private String adresAanvrager = null;

  @Field(type = FieldType.LABEL,
      caption = "Postcode / plaats",
      width = "300px")
  private String pc = null;

  @Field(type = FieldType.LABEL,
      caption = "E-mail",
      width = "300px")
  private String email = null;

  @Field(type = FieldType.LABEL,
      caption = "Kenmerk",
      width = "300px")
  private String kenmerk = null;
}
