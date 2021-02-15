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
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.ProTextArea;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page20OnderzoekBean implements Serializable {

  public static final String DATUM_ONTVANGST             = "datumOntvangst";
  public static final String DATUM_EINDE                 = "datumEinde";
  public static final String AFHANDELINGSTERMIJN         = "afhandelingstermijn";
  public static final String REDEN                       = "reden";
  public static final String DATUM_AANVANG_ONDERZOEK     = "datumAanvangOnderzoek";
  public static final String AANDUIDING_GEGEVENS         = "aanduidingGegevens";
  public static final String ONDERZOEK_DOOR_ANDER_GEDAAN = "onderzoekDoorAnderGedaan";
  public static final String VOLDOENDE_REDEN             = "voldoendeReden";
  public static final String TOELICHTING                 = "toelichting";

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
      width = "250px")
  @TextArea(maxLength = 250,
      rows = 3,
      nullRepresentation = "")
  private String reden = "";

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum aanvang onderzoek",
      width = "97px",
      required = true)
  private Date datumAanvangOnderzoek = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Aanduiding gegevens in onderzoek",
      required = true,
      width = "250px")
  @Select(containerDataSource = AanduidingOnderzoekContainer.class,
      itemCaptionPropertyId = AanduidingOnderzoekContainer.OMSCHRIJVING)
  private String aanduidingGegevens;

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
      width = "250px")
  @TextArea(maxLength = 250,
      rows = 3,
      nullRepresentation = "")
  private String toelichting = "";
}
