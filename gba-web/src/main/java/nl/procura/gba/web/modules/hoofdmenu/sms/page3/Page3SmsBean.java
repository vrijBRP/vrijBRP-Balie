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

package nl.procura.gba.web.modules.hoofdmenu.sms.page3;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaBsnField;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.modules.hoofdmenu.sms.page3.Page3SmsForm.ApplicationContainer;
import nl.procura.gba.web.modules.hoofdmenu.sms.page3.Page3SmsForm.SenderContainer;
import nl.procura.gba.web.modules.hoofdmenu.sms.page3.Page3SmsForm.TemplateContainer;
import nl.procura.gba.web.services.beheer.sms.SmsTemplate;
import nl.procura.sms.rest.domain.Application;
import nl.procura.sms.rest.domain.Sender;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.ProTextArea;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page3SmsBean implements Serializable {

  public static final String DESTINATION = "destination";
  public static final String BSN         = "bsn";
  public static final String CUSTOMER    = "customer";
  public static final String APP         = "app";
  public static final String TEMPLATE    = "template";
  public static final String SENDER      = "sender";
  public static final String ZAAK_ID     = "zaakId";
  public static final String MESSAGE     = "message";
  public static final String AUTO_SEND   = "autoSend";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Telefoonnummer",
      required = true)
  private String destination = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Zaak-ID")
  private String zaakId = "";

  @Field(customTypeClass = GbaBsnField.class,
      caption = "Burgerservicenummer")
  private String bsn = "";

  @Field(type = FieldType.LABEL,
      caption = "Klant",
      required = true)
  @Immediate
  private String customer = "";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Applicatie",
      width = "400px",
      required = true)
  @Select(itemCaptionPropertyId = ApplicationContainer.DESCRIPTION,
      nullSelectionAllowed = false)
  @Immediate
  private Application app;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "SMS sjabloon",
      width = "400px",
      required = true)
  @Select(itemCaptionPropertyId = TemplateContainer.DESCRIPTION)
  @Immediate
  private SmsTemplate template;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "SMS afzender",
      width = "400px",
      required = true)
  @Select(itemCaptionPropertyId = SenderContainer.DESCRIPTION)
  @Immediate
  private Sender sender;

  @Field(customTypeClass = ProTextArea.class,
      caption = "SMS bericht",
      width = "400px",
      required = true)
  @TextArea(rows = 10)
  private String message = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Automatisch verzenden",
      required = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private boolean autoSend = true;
}
