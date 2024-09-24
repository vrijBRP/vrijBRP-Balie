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

package nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page35GeboorteBeanBinnenErk implements Serializable {

  public static final String GEMEENTE         = "gemeente";
  public static final String DATUM            = "datum";
  public static final String AKTENR           = "aktenr";
  public static final String TOESTEMMINGGEVER = "toestemminggever";
  public static final String VERKLARING_GEZAG = "verklaringGezag";
  public static final String NAAMSKEUZE       = "naamskeuze";
  public static final String TITEL            = "titel";
  public static final String VOORVOEGSEL      = "voorv";
  public static final String GESLACHTSNAAM    = "geslachtsnaam";
  public static final String DUBBELE_NAAM     = "dubbeleNaam";
  public static final String RECHT            = "recht";

  @Field(type = FieldType.LABEL,
      caption = "Gemeente")
  private String gemeente = "";

  @Field(type = FieldType.LABEL,
      caption = "Datum")
  private String datum = "";

  @Field(type = FieldType.LABEL,
      caption = "Aktenummer")
  private String aktenr = "";

  @Field(type = FieldType.LABEL,
      caption = "Toestemminggever")
  private String toestemminggever = "";

  @Field(type = FieldType.LABEL,
      caption = "Verklaring moeder+erkenner m.b.t gezag bij moeder?")
  private String verklaringGezag = "";

  @Field(type = FieldType.LABEL,
      caption = "Naamskeuze")
  private String naamskeuze = "";

  @Field(type = FieldType.LABEL,
      caption = "Geslachtsnaam")
  private String geslachtsnaam = "";

  @Field(type = FieldType.LABEL,
      caption = "Voorvoegsel")
  private FieldValue voorv;

  @Field(type = FieldType.LABEL,
      caption = "Titel/predikaat")
  private FieldValue titel;

  @Field(customTypeClass = GbaTextField.class,
      readOnly = true,
      width = "200px",
      caption = "2e gekozen naam")
  private String dubbeleNaam = "";

  @Field(type = FieldType.LABEL,
      caption = "Toegepast recht van")
  private String recht = "";
}
