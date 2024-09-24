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

package nl.procura.gba.web.modules.bs.onderzoek.page20;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.ProTextArea;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page20OnderzoekBean implements Serializable {

  public static final String DATUM_ONTVANGST              = "datumOntvangst";
  public static final String DATUM_EINDE                  = "datumEinde";
  public static final String AFHANDELINGSTERMIJN          = "afhandelingstermijn";
  public static final String REDEN                        = "reden";
  public static final String DATUM_AANVANG_ONDERZOEK      = "datumAanvangOnderzoek";
  public static final String DATUM_AANVANG_ONDERZOEK_NVT  = "datumAanvangOnderzoekNvt";
  public static final String AANDUIDING_GEG_ONDERZOEK     = "aanduidingGegevensOnderzoek";
  public static final String AANDUIDING_GEG_ONDERZOEK_NVT = "aanduidingGegevensOnderzoekNvt";
  public static final String DEELRESULTAAT                = "deelresultaat";
  public static final String DATUM_AANVANG_DEELRESULTAAT  = "datumAanvangDeelresultaat";
  public static final String AAND_GEG_DEELRESULTAAT       = "aanduidingGegevensDeelresultaat";
  public static final String ONDERZOEK_DOOR_ANDER_GEDAAN  = "onderzoekDoorAnderGedaan";
  public static final String VOLDOENDE_REDEN              = "voldoendeReden";
  public static final String TOELICHTING                  = "toelichting";

  @Field(type = Field.FieldType.LABEL,
      caption = "Datum ontvangst",
      width = "97px")
  private String datumOntvangst = null;

  @Field(type = Field.FieldType.LABEL,
      caption = "Datum einde termijn",
      width = "97px")
  private String datumEinde = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Binnen 5 dagen af te handelen?",
      required = true)
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private Boolean afhandelingstermijn;

  @Field(customTypeClass = ProTextArea.class,
      caption = "Reden",
      width = "350px")
  @TextArea(maxLength = 250,
      rows = 3,
      nullRepresentation = "")
  private String reden = "";

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum aanvang onderzoek",
      width = "97px",
      required = true)
  private Date datumAanvangOnderzoek = null;

  @Field(type = FieldType.LABEL,
      caption = "Datum aanvang onderzoek")
  private String datumAanvangOnderzoekNvt = "N.v.t.";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Aanduiding gegevens in onderzoek",
      required = true,
      width = "350px")
  @Select(containerDataSource = AanduidingOnderzoekContainer.class,
      itemCaptionPropertyId = AanduidingOnderzoekContainer.OMSCHRIJVING)
  private String aanduidingGegevensOnderzoek;

  @Field(type = FieldType.LABEL,
      caption = "Aanduiding gegevens in onderzoek")
  private String aanduidingGegevensOnderzoekNvt = "N.v.t.";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Deelresultaat vastleggen?",
      required = true)
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE,
      nullSelectionAllowed = false)
  @Immediate
  private Boolean deelresultaat = false;

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum aanvang deelresultaat",
      width = "97px",
      required = true)
  private Date datumAanvangDeelresultaat = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Aanduiding deelresultaat",
      required = true,
      width = "350px")
  @Select(containerDataSource = AanduidingDeelresultaatContainer.class,
      itemCaptionPropertyId = AanduidingDeelresultaatContainer.OMSCHRIJVING)
  private String aanduidingGegevensDeelresultaat;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Gedegen onderzoek door ander overheidsorgaan gedaan en beschikbaar?",
      required = true)
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  private Boolean onderzoekDoorAnderGedaan;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Voldoende reden om stap(pen) over te slaan?",
      required = true)
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  private Boolean voldoendeReden;

  @Field(customTypeClass = ProTextArea.class,
      caption = "Toelichting",
      width = "350px")
  @TextArea(maxLength = 250,
      rows = 3,
      nullRepresentation = "")
  private String toelichting = "";
}
