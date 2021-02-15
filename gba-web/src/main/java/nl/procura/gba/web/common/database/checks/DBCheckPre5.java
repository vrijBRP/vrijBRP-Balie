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

import javax.persistence.EntityManager;

import nl.procura.commons.liquibase.objecten.LbTable;
import nl.procura.commons.liquibase.parameters.LbSchemaParameters;
import nl.procura.commons.liquibase.utils.LbObjectUtils;
import nl.procura.commons.liquibase.utils.LbOracleUtils;
import nl.procura.gba.web.common.database.DBCheckTemplateLb;

import liquibase.database.Database;

public class DBCheckPre5 extends DBCheckTemplateLb {

  public DBCheckPre5(EntityManager entityManager, Database database, String type) {
    super(entityManager, database, type, "Drop alle Oracle triggers");
  }

  @Override
  public void init() {

    if (isOracle()) {

      LbSchemaParameters parameters = new LbSchemaParameters();
      parameters.setDatabase(getDatabase());
      parameters.setExcludeTables("PROT");
      parameters.setExcludeTables("DATABASECHANGELOG");
      parameters.setExcludeTables("DATABASECHANGELOGLOCK");

      int count = 0;
      for (LbTable table : LbObjectUtils.getSchema(parameters).getTables()) {
        if (LbOracleUtils.removeNoNullTriggers(getDatabase(), table)) {
          count++;
        }
      }
      count(count);
    }
  }
}
