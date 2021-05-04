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

package nl.procura.gbaws.web.vaadin.module.sources.procura.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.ProTextField;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1DbProcuraBean implements Serializable {

  public static final String TNS_ADMIN_DIR = "tnsAdminDir";
  public static final String CUSTOM_URL    = "customUrl";
  public static final String CUSTOM_DRIVER = "customDriver";
  public static final String DATABASE      = "database";
  public static final String SID           = "sid";
  public static final String SERVER        = "server";
  public static final String PORT          = "port";
  public static final String USERNAME      = "username";
  public static final String PW            = "pw";
  public static final String MIN_CONN      = "minConn";
  public static final String MAX_CONN      = "maxConn";

  @Field(customTypeClass = ProTextField.class,
      width = "400px",
      caption = "Oracle TNS admin map")
  private String tnsAdminDir = "";

  @Field(customTypeClass = ProTextField.class,
      width = "400px",
      caption = "Eigen URL")
  @TextField(nullRepresentation = "",
      maxLength = 250)
  private String customUrl = "";

  @Field(customTypeClass = ProTextField.class,
      width = "400px",
      caption = "Eigen driver")
  @TextField(nullRepresentation = "",
      maxLength = 250)
  private String customDriver = "";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Database",
      width = "200px")
  @Select(containerDataSource = DatabaseTypeContainer.class)
  private String database = "";

  @Field(customTypeClass = ProTextField.class,
      width = "200px",
      caption = "Naam (SID)")
  @TextField(nullRepresentation = "",
      maxLength = 250)
  private String sid = "";

  @Field(customTypeClass = ProTextField.class,
      width = "200px",
      caption = "Server")
  @TextField(nullRepresentation = "",
      maxLength = 250)
  private String server = "";

  @Field(customTypeClass = NumberField.class,
      width = "50px",
      caption = "Poort")
  @TextField(nullRepresentation = "",
      maxLength = 10)
  private String port = "";

  @Field(customTypeClass = ProTextField.class,
      width = "200px",
      caption = "Gebruikersnaam")
  @TextField(nullRepresentation = "",
      maxLength = 250)
  private String username = "";

  @Field(customTypeClass = ProTextField.class,
      width = "200px",
      caption = "Wachtwoord")
  @TextField(nullRepresentation = "",
      maxLength = 250,
      secret = true)
  private String pw = "";

  @Field(customTypeClass = NumberField.class,
      width = "50px",
      caption = "Min. aantal verbindingen",
      required = true)
  @TextField(nullRepresentation = "",
      maxLength = 10)
  private String minConn = "";

  @Field(customTypeClass = NumberField.class,
      width = "50px",
      caption = "Max. aantal verbindingen",
      required = true)
  @TextField(nullRepresentation = "",
      maxLength = 10)
  private String maxConn = "";

  public String getDatabase() {
    return database;
  }

  public void setDatabase(String database) {
    this.database = database;
  }
}
