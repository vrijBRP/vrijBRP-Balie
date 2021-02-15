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

package nl.procura.gba.web.modules.bs.onderzoek.page30.gesprekwindow;

import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.modules.bs.onderzoek.page1.VermoedAdresContainer;
import nl.procura.gba.web.modules.bs.ontbinding.page30.AanhefContainer;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.onderzoek.enums.AanschrijvingBronType;
import nl.procura.gba.web.services.bs.onderzoek.enums.VermoedAdresType;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.zaken.documenten.afnemers.DocumentAfnemer;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.field.EmailField;
import nl.procura.vaadin.component.field.PostalcodeField;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.ProTextArea;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class BronBean {

  public static final String F_TYPE          = "type";
  public static final String F_BRON          = "bron";
  public static final String F_INHOUD        = "inhoud";
  public static final String F_ADRES_TYPE    = "adresType";
  public static final String F_STAKEHOLDER   = "stakeholder";
  public static final String F_INSTANTIE     = "instantie";
  public static final String F_AFDELING      = "afdeling";
  public static final String F_TAV_AANHEF    = "tavAanhef";
  public static final String F_TAV_VOORL     = "tavVoorl";
  public static final String F_TAV_NAAM      = "tavNaam";
  public static final String F_EMAIL         = "email";
  public static final String F_ADRES         = "adres";
  public static final String F_PC            = "pc";
  public static final String F_PLAATS        = "plaats";
  public static final String F_AANSCHRIJVING = "aanschrijving";
  public static final String F_BETROKKENE    = "betrokkene";
  public static final String F_GERELATEERDE  = "gerelateerde";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Betrokkene",
      visible = false,
      width = "400px")
  private DossierPersoon betrokkene;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Gerelateerde",
      visible = false,
      width = "400px")
  private Relatie gerelateerde;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Type aanschrijving",
      required = true,
      width = "400px")
  @Select(containerDataSource = AanschrijvingBronTypeContainer.class)
  private AanschrijvingBronType type;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Bron",
      width = "400px",
      required = true)
  private String bron = "";

  @Field(customTypeClass = ProTextArea.class,
      caption = "Inhoud gesprek",
      width = "400")
  @TextArea(rows = 6)
  private String inhoud = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Aanschrijfadres",
      required = true,
      width = "400px")
  @Select(containerDataSource = VermoedAdresContainer.class)
  private VermoedAdresType adresType;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Naam (instantie)",
      width = "400px")
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String instantie = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Belanghebbende",
      width = "400px")
  @Select()
  @Immediate
  private DocumentAfnemer stakeholder;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Afdeling",
      width = "400px")
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String afdeling = "";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Ter attentie van",
      width = "100px")
  @Select(containerDataSource = AanhefContainer.class)
  private FieldValue tavAanhef;

  @Field(customTypeClass = GbaTextField.class,
      width = "70px")
  @InputPrompt(text = "Voorletters")
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String tavVoorl = "";

  @Field(customTypeClass = GbaTextField.class,
      width = "220px")
  @TextField(maxLength = 250,
      nullRepresentation = "")
  @InputPrompt(text = "Naam")
  private String tavNaam = "";

  @Field(type = Field.FieldType.TEXT_FIELD,
      caption = "Adres",
      width = "400px",
      required = true)
  @TextField(maxLength = 250, nullRepresentation = "")
  private String adres = "";

  @Field(customTypeClass = PostalcodeField.class,
      caption = "Postcode / Plaats",
      width = "100px",
      description = "Postcode")
  private FieldValue pc;

  @Field(type = Field.FieldType.TEXT_FIELD,
      caption = "Plaats",
      width = "295px",
      required = true)
  @TextField(maxLength = 250, nullRepresentation = "")
  private String plaats = "";

  @Field(customTypeClass = EmailField.class,
      caption = "E-mail",
      width = "400px")
  @TextField(maxLength = 250, nullRepresentation = "")
  private String email;

  @Field(type = Field.FieldType.TEXT_FIELD,
      caption = "Betreft",
      width = "400px",
      readOnly = true)
  @TextField(maxLength = 250, nullRepresentation = "")
  private String aanschrijving = "Niet van toepassing";
}
