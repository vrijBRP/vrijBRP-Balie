/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.rijbewijs.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2RijbewijsBean1 implements Serializable {

  public static final String AANVRAAGNR        = "aanvraagnr";
  public static final String RASCODE           = "rasCode";
  public static final String SOORTAANVRAAG     = "soortAanvraag";
  public static final String REDENAANVRAAG     = "redenAanvraag";
  public static final String VERVANGTRIJBEWIJS = "vervangtRijbewijs";
  public static final String VERMELDING_TITEL  = "vermeldingTitel";
  public static final String SPOED             = "spoed";
  public static final String PV                = "pv";
  public static final String NAAMGEBRUIK       = "naamgebruik";
  public static final String GBABESTENDIG      = "gbaBestendig";
  public static final String DAGEN185NL        = "dagen185nl";
  public static final String RIJBEWIJSNR       = "rijbewijsNr";
  public static final String STATUSGBA         = "statusGba";
  public static final String THUISBEZORGEN     = "bezorgen";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Aanvraagnummer")
  private String aanvraagnr = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Ras code")
  private String rasCode = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Soort aanvraag")
  private String soortAanvraag = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Reden aanvraag")
  private String redenAanvraag = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Vervangt rijbewijs")
  private String vervangtRijbewijs = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Spoed")
  private String spoed = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Vermelding titel / predikaat")
  private String vermeldingTitel = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Proces-verbaal")
  private String pv = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Naamgebruik")
  private String naamgebruik = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "BRP bestendig")
  private String gbaBestendig = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "185 dagen in NL")
  private String dagen185nl = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Rijbewijsnummer")
  private String rijbewijsNr = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Thuisbezorgen")
  private String bezorgen = "";
}
