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

package nl.procura.gba.web.modules.bs.onderzoek.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.modules.bs.ontbinding.page30.AanhefContainer;
import nl.procura.gba.web.services.bs.onderzoek.enums.OnderzoekAardType;
import nl.procura.gba.web.services.bs.onderzoek.enums.OnderzoekBronType;
import nl.procura.gba.web.services.bs.onderzoek.enums.VermoedAdresType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.InputPrompt;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.PostalcodeField;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.ProTextArea;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1OnderzoekBean implements Serializable {

  public static final String BRON            = "bron";
  public static final String RELATIE         = "relatie";
  public static final String SOORT_AANGEVER  = "soortAangever";
  public static final String NR              = "nr";
  public static final String INSTANTIE       = "instantie";
  public static final String TAV_AANHEF      = "tavAanhef";
  public static final String TAV_VOORL       = "tavVoorl";
  public static final String TAV_NAAM        = "tavNaam";
  public static final String ADRES           = "adres";
  public static final String PC              = "pc";
  public static final String PLAATS          = "plaats";
  public static final String KENMERK         = "kenmerk";
  public static final String TOELICHTING     = "toelichting";
  public static final String AFDELING        = "afdeling";
  public static final String DATUM_ONTVANGST = "datumOntvangst";
  public static final String AARD            = "aard";
  public static final String AARD_ANDERS     = "aardAnders";
  public static final String VERMOED_ADRES   = "vermoedAdres";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Bron",
      required = true,
      width = "250px")
  @Select(containerDataSource = OnderzoekBronContainer.class)
  private OnderzoekBronType bron;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Dossiernummer TMV",
      width = "250px",
      required = true,
      visible = false)
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String nr = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Relatie tot te onderzoeken personen",
      width = "250px",
      required = true)
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String relatie = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Naam instantie",
      width = "250px",
      required = true)
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String instantie = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Naam afdeling",
      width = "250px",
      required = true)
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String afdeling = "";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Ter attentie van",
      width = "80px")
  @Select(containerDataSource = AanhefContainer.class)
  private FieldValue tavAanhef = null;

  @Field(customTypeClass = GbaTextField.class,
      width = "70px")
  @InputPrompt(text = "Voorletters")
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String tavVoorl = "";

  @Field(customTypeClass = GbaTextField.class,
      width = "140px")
  @TextField(maxLength = 250,
      nullRepresentation = "")
  @InputPrompt(text = "Naam")
  private String tavNaam = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Adres",
      width = "250px",
      required = true)
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String adres = "";

  @Field(customTypeClass = PostalcodeField.class,
      caption = "Postcode",
      width = "80px",
      description = "Postcode")
  private FieldValue pc = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Plaats",
      width = "250px",
      required = true)
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String plaats = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Kenmerk",
      width = "250px")
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String kenmerk = "";

  @Field(customTypeClass = ProTextArea.class,
      caption = "Toelichting",
      width = "250px")
  @TextArea(rows = 5,
      nullRepresentation = "")
  private String toelichting = "";

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum ontvangst",
      width = "97px",
      required = true)
  private Date datumOntvangst = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Reden",
      required = true,
      width = "250px")
  @Select(containerDataSource = OnderzoekAardContainer.class)
  @Immediate
  private OnderzoekAardType aard;

  @Field(customTypeClass = GbaTextField.class,
      visible = false,
      required = true,
      width = "250px")
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String aardAnders = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Vermoedelijk adres",
      required = true,
      width = "250px")
  @Select(containerDataSource = VermoedAdresContainer.class)
  private VermoedAdresType vermoedAdres;
}
