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

package nl.procura.gba.web.modules.bs.ontbinding.page30;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.field.PostalcodeField;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
@Data
public class Page30OntbindingBean5 implements Serializable {

  public static final String KANTOREN   = "kantoren";
  public static final String NAAM       = "naam";
  public static final String TAV_AANHEF = "tavAanhef";
  public static final String TAV_VOORL  = "tavVoorl";
  public static final String TAV_NAAM   = "tavNaam";
  public static final String ADRES      = "adres";
  public static final String PC         = "pc";
  public static final String PLAATS     = "plaats";
  public static final String LAND       = "land";
  public static final String KENMERK    = "kenmerk";
  public static final String KENMERK2   = "kenmerk2";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Selectie advocatenkantoren",
      width = "250px")
  private FieldValue kantoren = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Naam kantoor",
      width = "250px",
      required = true)
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String naam = "";

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

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Land",
      required = true,
      width = "250px")
  @Select(containerDataSource = LandContainer.class)
  private FieldValue land = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Ons kenmerk",
      width = "250px")
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String kenmerk = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Uw kenmerk",
      width = "250px")
  @TextField(maxLength = 300,
      nullRepresentation = "")
  private String kenmerk2 = "";
}
