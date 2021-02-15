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

package nl.procura.gba.web.modules.beheer.parameters.layout.database.page2;

import static nl.procura.standard.Globalfunctions.astr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import nl.procura.gba.config.GbaConfigProperty;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.modules.beheer.parameters.container.DatabaseTypeContainer;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.annotation.layout.Position;
import nl.procura.vaadin.component.field.NumberField;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD,
    defaultWidth = "200px")
public class Page2DatabaseBean implements Serializable {

  public static final String TYPE     = "type";
  public static final String SERVER   = "server";
  public static final String PORT     = "port";
  public static final String SCHEMA   = "schema";
  public static final String SID      = "sid";
  public static final String USERNAME = "username";
  public static final String PW       = "pw";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Wachtwoord",
      description = "Wachtwoord")
  @Position(order = "7")
  @TextField(secret = true)
  private String pw = "";

  @Field(customTypeClass = NumberField.class,
      caption = "Database poort",
      description = "Database poort",
      required = true,
      requiredError = "Poort is verplicht")
  @Position(order = "3")
  private String port = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Database server",
      description = "Database server",
      required = true,
      requiredError = "Server is verplicht")
  @Position(order = "2")

  private String server = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Schema",
      description = "Database schema")
  @Position(order = "4")
  private String schema = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Database SID",
      description = "Database SID",
      required = true,
      requiredError = "SID is verplicht")
  @Position(order = "5")
  private String sid = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Soort database",
      description = "Soort database",
      required = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = DatabaseTypeContainer.class,
      itemCaptionPropertyId = DatabaseTypeContainer.OMSCHRIJVING)
  @Position(order = "1")
  private String type = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Gebruikersnaam",
      description = "Gebruikersnaam",
      required = true,
      requiredError = "Gebruikersnaam is verplicht")
  @Position(order = "6")
  private String username = "";

  public Page2DatabaseBean() {
  }

  public Page2DatabaseBean(Properties config) {

    if (config != null) {
      if (config.containsKey(GbaConfigProperty.DB_NAME.getProperty())) {
        setType(config.getProperty(GbaConfigProperty.DB_NAME.getProperty()));
      }

      if (config.containsKey(GbaConfigProperty.DB_SERVER.getProperty())) {
        setServer(config.getProperty(GbaConfigProperty.DB_SERVER.getProperty()));
      }

      if (config.containsKey(GbaConfigProperty.DB_PORT.getProperty())) {
        setPort(config.getProperty(GbaConfigProperty.DB_PORT.getProperty()));
      }

      if (config.containsKey(GbaConfigProperty.DB_SCHEMA.getProperty())) {
        setSchema(config.getProperty(GbaConfigProperty.DB_SCHEMA.getProperty()));
      }

      if (config.containsKey(GbaConfigProperty.DB_SID.getProperty())) {
        setSid(config.getProperty(GbaConfigProperty.DB_SID.getProperty()));
      }

      if (config.containsKey(GbaConfigProperty.DB_USERNAME.getProperty())) {
        setUsername(config.getProperty(GbaConfigProperty.DB_USERNAME.getProperty()));
      }

      if (config.containsKey(GbaConfigProperty.DB_PW.getProperty())) {
        setPw(config.getProperty(GbaConfigProperty.DB_PW.getProperty()));
      }
    }
  }

  public void save(File propertiesFile) {

    Properties config = new Properties();
    config.setProperty(GbaConfigProperty.DB_NAME.getProperty(), getType());
    config.setProperty(GbaConfigProperty.DB_SERVER.getProperty(), getServer());
    config.setProperty(GbaConfigProperty.DB_PORT.getProperty(), astr(getPort()));
    config.setProperty(GbaConfigProperty.DB_SCHEMA.getProperty(), getSchema());
    config.setProperty(GbaConfigProperty.DB_SID.getProperty(), getSid());
    config.setProperty(GbaConfigProperty.DB_USERNAME.getProperty(), getUsername());
    config.setProperty(GbaConfigProperty.DB_PW.getProperty(), getPw());

    FileOutputStream fos = null;

    try {
      fos = new FileOutputStream(propertiesFile);
      config.store(fos, null);
      fos.flush();
    } catch (IOException e) {
      throw new ProException("Fout bij opslaan", e);
    } finally {
      IOUtils.closeQuietly(fos);
    }
  }
}
