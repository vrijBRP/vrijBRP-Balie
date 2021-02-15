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

import nl.procura.gba.web.common.database.DBCheckTemplateLb;
import nl.procura.standard.exceptions.ProException;

import liquibase.database.Database;

/**
 * Controle op minimale versie
 */
public class DBCheckPre4 extends DBCheckTemplateLb {

  public DBCheckPre4(EntityManager entityManager, Database database, String type) {
    super(entityManager, database, type, "Controle op minimale versie 1.5.9");
  }

  @Override
  public void init() {
    try {

      String sql = "select count(*) from stempel";
      count(sql);
    } catch (SQLException e) {
      throw new ProException(
          "Installeer eerste versie 1.5.9. Daarna kan de nieuwste versie worden geïnstalleerd.");
    }
  }
}
