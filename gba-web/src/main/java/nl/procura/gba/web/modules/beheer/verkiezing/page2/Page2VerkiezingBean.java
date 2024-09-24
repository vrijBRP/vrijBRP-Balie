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

package nl.procura.gba.web.modules.beheer.verkiezing.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.components.containers.actueel.PlaatsActueelContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.ProTextField;
import nl.procura.vaadin.component.field.fieldvalues.TabelFieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2VerkiezingBean implements Serializable {

  public static final String F_GEMEENTE                 = "gemeente";
  public static final String F_AFK_VERK                 = "afkVerkiezing";
  public static final String F_VERK                     = "verkiezing";
  public static final String F_DATUM_KAND               = "datumKandidaatstelling";
  public static final String F_DATUM_VERK               = "datumVerkiezing";
  public static final String F_KIEZERSPAS               = "kiezerspas";
  public static final String F_BRIEFSTEMBEWIJS          = "briefstembewijs";
  public static final String F_GEMACHTIGDE_KIESREGISTER = "gemachtigdeKiesregister";
  public static final String F_AANTAL_VOlMACHTEN        = "aantalVolmachten";
  public static final String F_AANTAL_STEMPASSEN        = "aantalStempassen";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Gemeente",
      required = true,
      width = "250px")
  @Select(nullSelectionAllowed = false,
      containerDataSource = PlaatsActueelContainer.class)
  private TabelFieldValue gemeente;

  @Field(customTypeClass = ProTextField.class,
      caption = "Afkorting",
      width = "50px",
      required = true)
  @TextField(nullRepresentation = "")
  private String afkVerkiezing = "";

  @Field(customTypeClass = ProTextField.class,
      caption = "Naam verkiezing",
      width = "250px",
      required = true)
  @TextField(nullRepresentation = "")
  private String verkiezing = "";

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum kandidaatstelling",
      width = "97px",
      required = true)
  private Date datumKandidaatstelling = null;

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum verkiezing",
      width = "97px",
      description = "Datum",
      required = true)
  private Date datumVerkiezing = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Kiezerspas van toepassing",
      width = "70px",
      required = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private boolean kiezerspas;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Briefstembewijs van toepassing",
      width = "70px",
      required = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private boolean briefstembewijs;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Gemachtigde in eigen kiesregister",
      width = "70px",
      required = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private boolean gemachtigdeKiesregister;

  @Field(customTypeClass = NumberField.class,
      caption = "Aantal volmachten toegestaan",
      required = true,
      width = "70px")
  @TextField(nullRepresentation = "")
  private Long aantalVolmachten;

  @Field(type = Field.FieldType.LABEL,
      caption = "Aantal stempassen",
      width = "200px")
  private Long aantalStempassen = 0L;
}
