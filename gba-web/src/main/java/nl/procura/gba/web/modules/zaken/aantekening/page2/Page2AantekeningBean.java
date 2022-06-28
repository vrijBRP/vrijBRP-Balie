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

package nl.procura.gba.web.modules.zaken.aantekening.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningIndicatie;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningStatus;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.field.ProNativeSelect;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2AantekeningBean implements Serializable {

  public static final String DATUM           = "datum";
  public static final String GEBRUIKER       = "gebruiker";
  public static final String INDICATIE       = "indicatie";
  public static final String INDICATIE_LABEL = "indicatieLabel";
  public static final String ONDERWERP       = "onderwerp";
  public static final String ONDERWERP_LABEL = "onderwerpLabel";
  public static final String INHOUD          = "inhoud";
  public static final String INHOUD_LABEL    = "inhoudLabel";
  public static final String STATUS          = "status";

  @Field(type = FieldType.LABEL,
      caption = "Datum / tijd")
  private String datum = "";

  @Field(type = FieldType.LABEL,
      caption = "Gebruiker")
  private String gebruiker = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Indicatie",
      width = "500px",
      required = true)
  @Immediate
  private PlAantekeningIndicatie indicatie = null;

  @Field(type = FieldType.LABEL,
      caption = "Indicatie",
      width = "500px")
  @TextArea(maxLength = 250)
  private String indicatieLabel = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Onderwerp",
      width = "500px")
  private String onderwerp = "";

  @Field(type = FieldType.LABEL,
      caption = "Onderwerp",
      width = "500px")
  private String onderwerpLabel = "";

  @Field(type = FieldType.TEXT_AREA,
      caption = "Inhoud",
      width = "1000px")
  @TextArea(rows = 25)
  private String inhoud = "";

  @Field(type = FieldType.TEXT_AREA,
      caption = "Inhoud",
      width = "1000px",
      readOnly = true)
  @TextArea(rows = 25)
  private String inhoudLabel = "";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Status")
  @Select(containerDataSource = StatusContainer.class,
      nullSelectionAllowed = false)
  private PlAantekeningStatus status;
}
