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

import static nl.procura.standard.Globalfunctions.pos;

import java.sql.SQLException;
import java.text.MessageFormat;

import javax.persistence.EntityManager;

import nl.procura.gba.web.common.database.DBCheckTemplateLb;

import liquibase.database.Database;

/**
 * Tabellen met een 0 record
 */
public class DBCheckPre1 extends DBCheckTemplateLb {

  public DBCheckPre1(EntityManager entityManager, Database database, String type) {
    super(entityManager, database, type, "Controle lege waarden van tabellen");
  }

  @Override
  public void init() throws SQLException {
    checkTabel("usr", "c_usr");
    checkTabel("document", "c_document");
    checkTabel("reisdoc", "c_reisdoc");
    checkTabel("profile", "c_profile");
    checkTabel("location", "c_location");
  }

  private void checkTabel(String table, String column) throws SQLException {
    String sql = "select count(*) from {0} where {1} = 0";
    if (!pos(count(MessageFormat.format(sql, table, column)))) {
      count(update(MessageFormat.format("insert into {0} ({1}) values (0)", table, column)));
    }
  }
}
