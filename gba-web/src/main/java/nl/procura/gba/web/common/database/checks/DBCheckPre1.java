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

package nl.procura.gba.web.common.database.checks;

import java.sql.SQLException;

import javax.persistence.EntityManager;

import nl.procura.commons.liquibase.objecten.LbColumn;
import nl.procura.commons.liquibase.objecten.LbTable;
import nl.procura.commons.liquibase.parameters.LbSchemaParameters;
import nl.procura.commons.liquibase.utils.LbObjectUtils;
import nl.procura.gba.web.common.database.DBCheckTemplateLb;

import liquibase.database.Database;

public class DBCheckPre1 extends DBCheckTemplateLb {

  private static final String C_USR      = "c_usr";
  private static final String C_LOCATION = "c_location";

  public DBCheckPre1(EntityManager entityManager, Database database, String type) {
    super(entityManager, database, type, "Gerelateerde waarden corrigeren");
  }

  @Override
  public void init() throws SQLException {

    LbSchemaParameters parameters = new LbSchemaParameters();
    parameters.setDatabase(getDatabase());
    parameters.setExcludeTables("PROT");
    parameters.setExcludeTables("DATABASECHANGELOG");
    parameters.setExcludeTables("DATABASECHANGELOGLOCK");

    for (LbTable table : LbObjectUtils.getSchema(parameters).getTables()) {
      updateTable(table);
    }
  }

  private void updateTable(LbTable table) throws SQLException {
    for (LbColumn column : table.getColumns()) {

      if (column.isPrimaryKey()) {
        continue;
      }

      boolean isUsr = column.getName().equalsIgnoreCase(C_USR);
      boolean isLocation = column.getName().equalsIgnoreCase(C_LOCATION);

      if (isUsr && column.isNullable()) {
        updateUser(table.getName());
      }

      if (isLocation && column.isNullable()) {
        updateLocation(table.getName());
      }
    }
  }

  private void updateLocation(String table) throws SQLException {

    String sql = String.format("update %s" +
        " set c_location = 0" +
        " where c_location is null or c_location not in (select c_location from location)",
        getDatabase().escapeTableName(null, null, table));
    count(update(sql), "NULL waarde: " + table + ".c_location");
  }

  private void updateUser(String table) throws SQLException {

    String sql = String.format("update %s" +
        " set c_usr = 0" +
        " where c_usr is null or c_usr not in (select c_usr from usr)",
        getDatabase().escapeTableName(null, null, table));
    count(update(sql), "NULL waarde: " + table + ".c_usr");
  }
}
