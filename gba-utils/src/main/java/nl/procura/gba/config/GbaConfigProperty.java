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

package nl.procura.gba.config;

public enum GbaConfigProperty {

  APP(true, "s_app", "app.name"),
  DB_NAME(true, "app_db_name", "app.db.name"),
  DB_PW(true, "app_db_password", "app.db.password"),
  DB_SERVER(true, "app_db_server", "app.db.server"),
  DB_PORT(true, "app_db_port", "app.db.port"),
  DB_SCHEMA(false, "app_db_schema", "app.db.schema"),
  DB_SID(true, "app_db_sid", "app.db.sid"),
  DB_USERNAME(true, "app_db_username", "app.db.username"),
  CODE_GEMEENTE(true, "c_gem", "gemeente.code"),
  GEMEENTE(true, "s_gem", "gemeente.naam"),
  PROWEB_AUTHENTICATION(false, "proweb.authentication"),
  PROCURA_ENDPOINT(false, "procura.endpoint"),
  LICENSE_KEY(false, "license.key"),
  ENCRYPTION_KEY0(false, "crypt.version0.key"),
  ENCRYPTION_KEY1(false, "crypt.version1.key"),
  ENCRYPTION_KEY1_UPDATE(false, "crypt.version1.key.update");

  private boolean  required;
  private String[] properties;

  GbaConfigProperty(boolean required, String... properties) {
    this.required = required;
    this.properties = properties;
  }

  public String[] getProperties() {
    return properties;
  }

  public boolean isRequired() {
    return required;
  }

  public String getProperty() {
    return getProperties()[0];
  }

  @Override
  public String toString() {
    return getProperties()[0];
  }
}
