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

package nl.procura.gba.web.modules.zaken.reisdocument.page21;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.zaken.reisdocumenten.ToestemmingGegevenType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page21ReisdocumentBean implements Serializable {

  public static final String OUDER1_LABEL  = "ouder1Label";
  public static final String OUDER2_LABEL  = "ouder2Label";
  public static final String DERDE_LABEL   = "derdeLabel";
  public static final String CURATOR_LABEL = "curatorLabel";

  public static final String DERDE_TEXT   = "derdeText";
  public static final String CURATOR_TEXT = "curatorText";

  public static final String TOESTEMMING1 = "toestemming1";
  public static final String TOESTEMMING2 = "toestemming2";
  public static final String TOESTEMMING3 = "toestemming3";
  public static final String TOESTEMMING4 = "toestemming4";

  public static final String CONSTATERING1 = "constatering1";
  public static final String CONSTATERING2 = "constatering2";
  public static final String CONSTATERING3 = "constatering3";
  public static final String CONSTATERING4 = "constatering4";

  public static final String CONCLUSIE1 = "conclusie1";
  public static final String CONCLUSIE2 = "conclusie2";
  public static final String CONCLUSIE3 = "conclusie3";
  public static final String CONCLUSIE4 = "conclusie4";

  @Field(type = FieldType.LABEL,
      caption = "Betreft",
      width = "400px")
  private String ouder1Label = "";

  @Field(type = FieldType.LABEL,
      caption = "Betreft",
      width = "400px")
  private String ouder2Label = "";

  @Field(type = FieldType.LABEL,
      caption = "Betreft",
      width = "400px")
  private String derdeLabel = "";

  @Field(type = FieldType.LABEL,
      caption = "Betreft",
      width = "400px")
  private String curatorLabel = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Betreft",
      width = "400px",
      required = true)
  @TextField(maxLength = 255)
  private String derdeText = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Betreft",
      width = "400px",
      required = true)
  @TextField(maxLength = 255)
  private String curatorText = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Toestemming")
  @Select(containerDataSource = ToestemminggevenContainer1.class,
      nullSelectionAllowed = false)
  private ToestemmingGegevenType toestemming1 = ToestemmingGegevenType.NEE;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Toestemming")
  @Select(containerDataSource = ToestemminggevenContainer1.class,
      nullSelectionAllowed = false)
  private ToestemmingGegevenType toestemming2 = ToestemmingGegevenType.NEE;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Toestemming")
  @Select(containerDataSource = ToestemminggevenContainer1.class,
      nullSelectionAllowed = false)
  @Immediate()
  private ToestemmingGegevenType toestemming3 = ToestemmingGegevenType.NEE;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Toestemming")
  @Select(containerDataSource = ToestemminggevenContainer2.class,
      nullSelectionAllowed = false)
  @Immediate()
  private ToestemmingGegevenType toestemming4 = ToestemmingGegevenType.NEE;

  @Field(type = FieldType.LABEL,
      caption = "Constateringen",
      width = "600px")
  private String constatering1 = "";

  @Field(type = FieldType.LABEL,
      caption = "Constateringen",
      width = "600px")
  private String constatering2 = "";

  @Field(type = FieldType.LABEL,
      caption = "Constateringen",
      width = "600px")
  private String constatering3 = "";

  @Field(type = FieldType.LABEL,
      caption = "Constateringen",
      width = "600px")
  private String constatering4 = "";

  @Field(type = FieldType.LABEL,
      caption = "Conclusie",
      width = "600px")
  private String conclusie1 = "";

  @Field(type = FieldType.LABEL,
      caption = "Conclusie",
      width = "600px")
  private String conclusie2 = "";

  @Field(type = FieldType.LABEL,
      caption = "Conclusie",
      width = "600px")
  private String conclusie3 = "";

  @Field(type = FieldType.LABEL,
      caption = "Conclusie",
      width = "600px")
  private String conclusie4 = "";
}
