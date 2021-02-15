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

package nl.procura.gba.web.modules.hoofdmenu.sms.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.component.field.ProTextArea;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2SmsBean implements Serializable {

  public static final String CODE                = "code";
  public static final String UID                 = "uid";
  public static final String DESTINATION         = "destination";
  public static final String BSN                 = "bsn";
  public static final String ZAAK_ID             = "zaakId";
  public static final String TIMESTAMP           = "timestamp";
  public static final String CUSTOMER            = "customer";
  public static final String APP                 = "app";
  public static final String SENDER              = "sender";
  public static final String SEND_TO_SERVICE     = "sendToService";
  public static final String SEND_TO_DESTINATION = "sendToDestination";
  public static final String AUTO_SEND           = "autoSend";
  public static final String MESSAGE             = "message";

  @Field(type = FieldType.LABEL,
      caption = "Code",
      width = "300px")
  private int code = 0;

  @Field(type = FieldType.LABEL,
      caption = "SMS Id",
      width = "300px")
  private String uid = "";

  @Field(type = FieldType.LABEL,
      caption = "Telefoonnummer",
      width = "300px")
  private String destination = "";

  @Field(type = FieldType.LABEL,
      caption = "Burgerservicenummer",
      width = "300px")
  private String bsn = "";

  @Field(type = FieldType.LABEL,
      caption = "Automatisch verzenden",
      width = "300px")
  private String autoSend = "";

  @Field(type = FieldType.LABEL,
      caption = "Zaak-id",
      width = "300px")
  private String zaakId = "";

  @Field(type = FieldType.LABEL,
      caption = "Ingevoerd op",
      width = "300px")
  private String timestamp = "";

  @Field(type = FieldType.LABEL,
      caption = "Klant",
      width = "300px")
  private String customer = "";

  @Field(type = FieldType.LABEL,
      caption = "Applicatie",
      width = "300px")
  private String app = "";

  @Field(type = FieldType.LABEL,
      caption = "SMS afzender",
      width = "300px")
  private String sender = "";

  @Field(type = FieldType.LABEL,
      caption = "Verzonden naar SMS service",
      width = "300px")
  private String sendToService = "";

  @Field(type = FieldType.LABEL,
      caption = "Ontvangen door telefoon",
      width = "300px")
  private String sendToDestination = "";

  @Field(customTypeClass = ProTextArea.class,
      caption = "SMS bericht",
      width = "300px")
  @TextArea(rows = 10)
  private String message = "";
}
