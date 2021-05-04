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

import nl.procura.commons.liquibase.objecten.LbColumn;
import nl.procura.commons.liquibase.objecten.LbTable;
import nl.procura.commons.liquibase.parameters.LbSchemaParameters;
import nl.procura.commons.liquibase.utils.LbObjectUtils;
import nl.procura.commons.liquibase.utils.LbUpdateUtils;
import nl.procura.gba.web.common.database.DBCheckTemplateLb;

import liquibase.database.Database;

public class DBCheckPost1 extends DBCheckTemplateLb {

  public DBCheckPost1(EntityManager entityManager, Database database, String type) {
    super(entityManager, database, type, "Null waarden corrigeren met standaardwaarde");
  }

  @Override
  public void init() {
    LbSchemaParameters parameters = new LbSchemaParameters();
    parameters.setDatabase(getDatabase());
    parameters.setExcludeTables("PROT", DATABASECHANGELOGLOCK, DATABASECHANGELOG);

    for (LbTable table : LbObjectUtils.getSchema(parameters).getTables()) {
      for (LbColumn column : table.getColumns()) {
        String msg = "ongedefinieerde waarde " + table + "." + column;
        count(LbUpdateUtils.updateNullValues(getDatabase(), table, column), msg);
      }
    }
  }
}
