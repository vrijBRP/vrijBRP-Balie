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

package nl.procura.gbaws.web.vaadin.module.email.config.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.ProTextArea;
import nl.procura.vaadin.component.field.ProTextField;

@FormFieldFactoryBean(accessType = ElementType.FIELD,
    defaultRequiredError = "Veld \"{}\" is verplicht.")
public class Page1EmailConfigBean implements Serializable {

  public static final String CHECK_WRONG_PW   = "checkWrongPassword";
  public static final String CHECK_NEW_PW     = "checkNewPassword";
  public static final String EMAIL_FROM       = "emailFrom";
  public static final String EMAIL_REPLYTO    = "emailReplyTo";
  public static final String EMAIL_RECIPIENTS = "emailRecipients";
  public static final String PROPERTIES       = "properties";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Bij foutief wachtwoord foutmelding",
      required = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  private boolean checkWrongPassword = false;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Bij aanmaken nieuw wachtwoord",
      required = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  private boolean checkNewPassword = false;

  @Field(customTypeClass = ProTextField.class,
      width = "400px",
      caption = "Afzender e-mail",
      required = true)
  @TextField(nullRepresentation = "",
      maxLength = 250)
  private String emailFrom = "";

  @Field(customTypeClass = ProTextField.class,
      width = "400px",
      caption = "Beantwoord naar e-mail",
      required = true)
  @TextField(nullRepresentation = "",
      maxLength = 250)
  private String emailReplyTo = "";

  @Field(customTypeClass = ProTextArea.class,
      width = "400px",
      caption = "Ontvanger(s) e-mail",
      required = true)
  @TextArea(nullRepresentation = "",
      maxLength = 500,
      rows = 3)
  private String emailRecipients = "";

  @Field(customTypeClass = ProTextArea.class,
      width = "400px",
      caption = "Eigenschappen")
  @TextArea(nullRepresentation = "",
      maxLength = 500,
      rows = 6)
  private String properties = "";

  public boolean isCheckWrongPassword() {
    return checkWrongPassword;
  }

  public void setCheckWrongPassword(boolean checkWrongPassword) {
    this.checkWrongPassword = checkWrongPassword;
  }

  public boolean isCheckNewPassword() {
    return checkNewPassword;
  }

  public void setCheckNewPassword(boolean checkNewPassword) {
    this.checkNewPassword = checkNewPassword;
  }

  public String getEmailFrom() {
    return emailFrom;
  }

  public void setEmailFrom(String emailFrom) {
    this.emailFrom = emailFrom;
  }

  public String getEmailReplyTo() {
    return emailReplyTo;
  }

  public void setEmailReplyTo(String emailReplyTo) {
    this.emailReplyTo = emailReplyTo;
  }

  public String getEmailRecipients() {
    return emailRecipients;
  }

  public void setEmailRecipients(String emailRecipients) {
    this.emailRecipients = emailRecipients;
  }

  public String getProperties() {
    return properties;
  }

  public void setProperties(String properties) {
    this.properties = properties;
  }
}
