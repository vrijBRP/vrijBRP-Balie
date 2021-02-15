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

package nl.procura.gba.web.services.applicatie;

import static nl.procura.standard.Globalfunctions.trim;

import java.io.File;
import java.io.Serializable;

import nl.procura.gba.config.GbaConfigProperty;
import nl.procura.gba.config.GbaProperties;

public class DatabaseConfig implements Serializable {

  private File   file     = null;
  private String type     = "";
  private String server   = "";
  private String port     = "";
  private String schema   = "";
  private String sid      = "";
  private String username = "";
  private String password = "";

  public DatabaseConfig(File file) {

    this.file = file;

    if (file.exists()) {

      GbaProperties config = new GbaProperties(file);

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
        setPassword(config.getProperty(GbaConfigProperty.DB_PW.getProperty()));
      }
    }
  }

  public DatabaseConfig(String type, String server, String port, String schema, String sid, String username,
      String password) {
    this.type = type;
    this.server = server;
    this.port = port;
    this.schema = schema;
    this.sid = sid;
    this.username = username;
    this.password = password;
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getSchema() {
    return schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  public String getServer() {
    return server;
  }

  public void setServer(String server) {
    this.server = server;
  }

  public String getSid() {
    return sid;
  }

  public void setSid(String sid) {
    this.sid = sid;
  }

  public String getType() {
    return type;
  }

  public void setType(String servertype) {
    this.type = servertype;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String toString() {
    return trim(String.format("%s, %s, %s, %s, %s, %s, %s", type, server, port, schema, sid, username, password));
  }
}
