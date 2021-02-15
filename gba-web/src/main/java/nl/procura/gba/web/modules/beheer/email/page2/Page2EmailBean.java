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

package nl.procura.gba.web.modules.beheer.email.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaInternetAddressField;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.beheer.email.EmailType;
import nl.procura.gba.web.services.beheer.email.EmailTypeContent;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.PosNumberField;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2EmailBean implements Serializable {

  public static final String TYPE          = "type";
  public static final String TYPE_CONTENT  = "typeContent";
  public static final String ACTIVEREN     = "activeren";
  public static final String ONDERWERP     = "onderwerp";
  public static final String BCC           = "bcc";
  public static final String VAN           = "van";
  public static final String ANTWOORD_NAAR = "antwoordNaar";
  public static final String VARIABELEN    = "variabelen";
  public static final String GELDIG        = "geldig";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Geactiveerd",
      required = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private Boolean activeren = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Type",
      required = true,
      width = "400px")
  @Select(containerDataSource = EmailTypeContainer.class)
  @Immediate
  private EmailType type = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Soort inhoud",
      required = true)
  @Select(containerDataSource = EmailTypeContentContainer.class,
      nullSelectionAllowed = false)
  @Immediate
  private EmailTypeContent typeContent = null;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Onderwerp",
      required = true,
      width = "400px")
  @TextField(nullRepresentation = "",
      maxLength = 255)
  private String onderwerp = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Variabelen",
      width = "600px",
      readOnly = true)
  private String variabelen = "";

  @Field(customTypeClass = GbaInternetAddressField.class,
      caption = "Van",
      required = true,
      width = "400px")
  @TextField(nullRepresentation = "",
      maxLength = 255)
  private String van = "";

  @Field(customTypeClass = GbaInternetAddressField.class,
      caption = "BCC",
      width = "400px")
  @TextField(nullRepresentation = "",
      maxLength = 255)
  private String bcc = "";

  @Field(customTypeClass = GbaInternetAddressField.class,
      caption = "Antwoord-naar",
      required = true,
      width = "400px")
  @TextField(nullRepresentation = "",
      maxLength = 255)
  private String antwoordNaar = "";

  @Field(customTypeClass = PosNumberField.class,
      caption = "Geldigheid van de link",
      width = "40px",
      required = true)
  @TextField(maxLength = 2)
  private String geldig = "";
}
