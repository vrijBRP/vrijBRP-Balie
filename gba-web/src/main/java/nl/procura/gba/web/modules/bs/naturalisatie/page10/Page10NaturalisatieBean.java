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

package nl.procura.gba.web.modules.bs.naturalisatie.page10;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.bs.naturalisatie.enums.BevoegdTotVerzoekType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.component.container.NLBooleanContainer;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page10NaturalisatieBean implements Serializable {

  public static final String F_VERBLIJFSTITEL       = "verblijfstitel";
  public static final String F_NATIONALITEIT        = "nationaliteit";
  public static final String F_GEBOREN              = "geboren";
  public static final String F_EERSTE_VESTIGING_NL  = "eersteVestigingNL";
  public static final String F_LAATSTE_VESTIGING_NL = "laatsteVestigingNL";
  public static final String F_CURATELE             = "curatele";
  public static final String F_PARTNER              = "partner";

  public static final String F_BEVOEGD      = "bevoegd";
  public static final String F_TOELICHTING  = "toelichting";
  public static final String F_OPTIE        = "optie";
  public static final String F_TOELICHTING2 = "toelichting2";

  @Field(type = FieldType.LABEL,
      caption = "Verblijfstitel")
  private String verblijfstitel;

  @Field(type = FieldType.LABEL,
      caption = "Nationaliteit")
  private String nationaliteit;

  @Field(type = FieldType.LABEL,
      caption = "Geboren")
  private String geboren;

  @Field(type = FieldType.LABEL,
      caption = "Eerste vestiging in NL")
  private String eersteVestigingNL;

  @Field(type = FieldType.LABEL,
      caption = "Laatste vestiging in NL")
  private String laatsteVestigingNL;

  @Field(type = FieldType.LABEL,
      caption = "Curatele")
  private String curatele;

  @Field(type = FieldType.LABEL,
      caption = "Huidige partner")
  private String partner;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Bevoegd tot indienen verzoek?",
      required = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = BevoegdTotVerzoekContainer.class)
  private BevoegdTotVerzoekType bevoegd;

  @Field(type = FieldType.TEXT_AREA,
      width = "350px",
      caption = "Toelichting")
  @TextArea(nullRepresentation = "",
      rows = 3)
  private String toelichting;

  @Field(customTypeClass = GbaNativeSelect.class,
      required = true,
      caption = "Optie mogelijk?")
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private Boolean optie;

  @Field(type = FieldType.TEXT_AREA,
      width = "350px",
      caption = "Toelichting")
  @TextArea(nullRepresentation = "",
      rows = 3)
  private String toelichting2;
}
