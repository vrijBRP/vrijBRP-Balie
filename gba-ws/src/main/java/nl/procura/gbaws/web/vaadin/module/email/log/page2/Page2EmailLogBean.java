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

package nl.procura.gbaws.web.vaadin.module.email.log.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD,
    defaultRequiredError = "Veld \"{}\" is verplicht.")
public class Page2EmailLogBean implements Serializable {

  public static final String STATUS  = "status";
  public static final String SUBJECT = "subject";
  public static final String BODY    = "body";
  public static final String ERROR   = "error";
  public static final String D_IN    = "dIn";

  @Field(type = FieldType.LABEL,
      caption = "Status")
  private String status = "";

  @Field(type = FieldType.LABEL,
      caption = "Onderwerp")
  private String subject = "";

  @Field(type = FieldType.LABEL,
      caption = "Inhoud")
  private String body = "";

  @Field(type = FieldType.LABEL,
      caption = "error")
  private String error = "";

  @Field(type = FieldType.LABEL,
      caption = "Datum invoer")
  private String dIn = "";

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getdIn() {
    return dIn;
  }

  public void setdIn(String dIn) {
    this.dIn = dIn;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
