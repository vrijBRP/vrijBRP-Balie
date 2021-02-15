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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab3;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.modules.bs.ontbinding.page30.AanhefContainer;
import nl.procura.gba.web.modules.hoofdmenu.gv.containers.GrondslagContainer;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.EmailField;
import nl.procura.vaadin.component.field.PostalcodeField;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Tab3DocumentenPage2Bean implements Serializable {

  public static final String AFNEMER    = "afnemer";
  public static final String TAV_AANHEF = "tavAanhef";
  public static final String TAV_VOORL  = "tavVoorl";
  public static final String TAV_NAAM   = "tavNaam";
  public static final String ADRES      = "adres";
  public static final String PC         = "pc";
  public static final String PLAATS     = "plaats";
  public static final String GRONDSLAG  = "grondslag";
  public static final String EMAIL      = "email";

  public static final String VERSTREKBEP = "verstrekbep";
  public static final String TOEKENNEN   = "toekennen";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Afnemer",
      required = true,
      width = "300px")
  private String afnemer = "";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Ter attentie van",
      width = "80px")
  @Select(containerDataSource = AanhefContainer.class)
  @InputPrompt(text = "Aanhef")
  private FieldValue tavAanhef = null;

  @Field(type = FieldType.TEXT_FIELD,
      width = "70px")
  @InputPrompt(text = "Voorletters")
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String tavVoorl = "";

  @Field(type = FieldType.TEXT_FIELD,
      width = "140px")
  @TextField(maxLength = 250,
      nullRepresentation = "")
  @InputPrompt(text = "Naam")
  private String tavNaam = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Adres",
      width = "300px",
      required = true)
  @TextField(maxLength = 250)
  private String adres = "";

  @Field(customTypeClass = PostalcodeField.class,
      caption = "Postcode",
      width = "100px",
      description = "Postcode")
  private FieldValue pc = null;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Plaats",
      width = "300px",
      required = true)
  @TextField(maxLength = 250)
  private String plaats = "";

  @Field(customTypeClass = EmailField.class,
      caption = "E-mail",
      width = "300px")
  @TextField(maxLength = 250)
  private String email;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Grondslag",
      width = "300px")
  @Select(containerDataSource = GrondslagContainer.class)
  @Immediate
  private KoppelEnumeratieType grondslag = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Van toepassing",
      required = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private Boolean verstrekbep = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Toekennen",
      width = "300px",
      required = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = ToekenningContainer.class)
  @Immediate
  private KoppelEnumeratieType toekennen = KoppelEnumeratieType.ONBEKEND;
}
