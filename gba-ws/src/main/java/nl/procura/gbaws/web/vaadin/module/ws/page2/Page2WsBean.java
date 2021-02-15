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

package nl.procura.gbaws.web.vaadin.module.ws.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD,
    defaultRequiredError = "Veld \"{}\" is verplicht.")
public class Page2WsBean implements Serializable {

  public static final String NAME        = "name";
  public static final String DESCRIPTION = "description";
  public static final String ENDPOINT    = "endpoint";
  public static final String WSDL        = "wsdl";

  @Field(type = FieldType.LABEL,
      caption = "Naam")
  private String name = "";

  @Field(type = FieldType.LABEL,
      caption = "Omschrijving")
  private String description = "";

  @Field(type = FieldType.LABEL,
      caption = "Endpoint")
  private String endpoint = "";

  @Field(type = FieldType.LABEL,
      caption = "WSDL")
  private String wsdl = "";

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public String getWsdl() {
    return wsdl;
  }

  public void setWsdl(String wsdl) {
    this.wsdl = wsdl;
  }
}
