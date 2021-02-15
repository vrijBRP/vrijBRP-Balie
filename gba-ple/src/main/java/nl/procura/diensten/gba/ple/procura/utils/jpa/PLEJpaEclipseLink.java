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

package nl.procura.diensten.gba.ple.procura.utils.jpa;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.aval;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.config.CacheType;
import org.eclipse.persistence.config.PersistenceUnitProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PLEJpaEclipseLink implements PLEJpaInterface {

  private static final String DB_LOG_INFO = "INFO";
  private static final String DB_ORACLE   = "oracle";
  private static final String DB_POSTGRES = "postgres";

  private final HashMap<String, String> propertyMap = new HashMap<>();
  private String                        tnsAdminDir, customUrl, customDriver, database = "";
  private String                        sid, username, password, server = "";
  private String                        loglevel    = DB_LOG_INFO;
  private int                           port, maxConnections, minConnections = 0;

  public PLEJpaEclipseLink() {
  }

  public void addProperty(String key, String value) {
    propertyMap.put(key, value);
  }

  @Override
  public String getDatabase() {
    return database;
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  public String getLoglevel() {
    return loglevel;
  }

  public void setLoglevel(String loglevel) {
    this.loglevel = loglevel;
  }

  @Override
  public int getMaxConnections() {
    return maxConnections;
  }

  public void setMaxConnections(int maxConnections) {
    this.maxConnections = maxConnections;
  }

  @Override
  public int getMinConnections() {
    return minConnections;
  }

  public void setMinConnections(int minConnections) {
    this.minConnections = minConnections;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getProperty(Properties properties, String key) {
    return (properties.containsKey(key) ? properties.getProperty(key) : "");
  }

  @SuppressWarnings("deprecation")
  @Override
  public Map<String, String> getPropertyMap() {

    addProperty(PersistenceUnitProperties.LOGGING_LEVEL, getLoglevel());
    addProperty(PersistenceUnitProperties.ID_VALIDATION, "NONE");
    addProperty(PersistenceUnitProperties.PESSIMISTIC_LOCK_TIMEOUT, "30000");
    addProperty(PersistenceUnitProperties.QUERY_TIMEOUT, "30000");
    addProperty(PersistenceUnitProperties.CONNECTION_POOL_WAIT, "30000");
    addProperty(PersistenceUnitProperties.CONNECTION_POOL_SHARED, "true");
    addProperty(PersistenceUnitProperties.JDBC_READ_CONNECTIONS_MIN, astr(getMinConnections()));
    addProperty(PersistenceUnitProperties.JDBC_READ_CONNECTIONS_MAX, astr(getMaxConnections()));
    addProperty(PersistenceUnitProperties.JDBC_WRITE_CONNECTIONS_MIN, astr(getMinConnections()));
    addProperty(PersistenceUnitProperties.JDBC_WRITE_CONNECTIONS_MAX, astr(getMaxConnections()));
    addProperty(PersistenceUnitProperties.JDBC_READ_CONNECTIONS_SHARED, "true");
    addProperty(PersistenceUnitProperties.JDBC_USER, getUsername());
    addProperty(PersistenceUnitProperties.JDBC_PASSWORD, getPassword());
    addProperty(PersistenceUnitProperties.CACHE_TYPE_DEFAULT, CacheType.HardWeak);
    addProperty(PersistenceUnitProperties.CACHE_STATEMENTS, "true");
    addProperty(PersistenceUnitProperties.CACHE_SIZE_DEFAULT, "3000");
    addProperty(PersistenceUnitProperties.CACHE_SHARED_DEFAULT, "false");

    if (StringUtils.isNotBlank(tnsAdminDir)) {
      System.setProperty("oracle.net.tns_admin", tnsAdminDir);
    }

    String finalUrl = customUrl;
    if (StringUtils.isBlank(finalUrl)) {
      if (getDatabase().equals(DB_POSTGRES)) {
        finalUrl = String.format("jdbc:postgresql://%s:%s/%s", getServer(), getPort(), getSid());
      } else if (getDatabase().equals(DB_ORACLE)) {
        finalUrl = String.format("jdbc:oracle:thin:@%s:%s:%s", getServer(), getPort(), getSid());
      }
    }

    String finalDriver = customDriver;
    if (StringUtils.isBlank(finalDriver)) {
      if (getDatabase().equals(DB_POSTGRES)) {
        finalDriver = "org.postgresql.Driver";
      } else if (getDatabase().equals(DB_ORACLE)) {
        finalDriver = "oracle.jdbc.OracleDriver";
      }
    }

    StringBuilder sessionName = new StringBuilder(finalUrl);
    if (StringUtils.isNotBlank(tnsAdminDir)) {
      sessionName.append(" (Oracle TNS dir: ");
      sessionName.append(tnsAdminDir);
      sessionName.append(")");
    }

    log.info("JPA connection: " + sessionName);
    addProperty(PersistenceUnitProperties.SESSION_NAME, sessionName.toString());
    addProperty(PersistenceUnitProperties.JDBC_URL, finalUrl);
    addProperty(PersistenceUnitProperties.JDBC_DRIVER, finalDriver);

    return propertyMap;
  }

  @Override
  public String getServer() {
    return server;
  }

  public void setServer(String server) {
    this.server = server;
  }

  @Override
  public String getSid() {
    return sid;
  }

  public void setSid(String sid) {
    this.sid = sid;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String getCustomUrl() {
    return customUrl;
  }

  public void setCustomUrl(String customUrl) {
    this.customUrl = customUrl;
  }

  public String getTnsAdminDir() {
    return tnsAdminDir;
  }

  public void setTnsAdminDir(String tnsAdminDir) {
    this.tnsAdminDir = tnsAdminDir;
  }

  public String getCustomDriver() {
    return customDriver;
  }

  public void setCustomDriver(String customDriver) {
    this.customDriver = customDriver;
  }

  public void setProperyFile(File file) {

    FileInputStream fis = null;

    try {

      fis = new FileInputStream(file);
      Properties properties = new Properties();
      properties.load(fis);

      setTnsAdminDir(getProperty(properties, "tns-admin-dir"));
      setCustomUrl(getProperty(properties, "custom-url"));
      setCustomDriver(getProperty(properties, "custom-driver"));
      setDatabase(getProperty(properties, "db"));
      setSid(getProperty(properties, "sid"));
      setUsername(getProperty(properties, "username"));
      setPassword(getProperty(properties, "password"));
      setPort(aval(getProperty(properties, "port")));
      setServer(getProperty(properties, "server"));
      setLoglevel(getProperty(properties, "loglevel"));
      setMinConnections(aval(getProperty(properties, "connections.min")));
      setMaxConnections(aval(getProperty(properties, "connections.max")));

    } catch (IOException e) {
      log.debug(e.toString());
    } finally {
      IOUtils.closeQuietly(fis);
    }
  }
}
