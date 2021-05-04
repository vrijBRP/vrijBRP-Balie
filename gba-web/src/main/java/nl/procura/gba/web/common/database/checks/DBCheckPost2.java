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
import java.text.MessageFormat;

import javax.persistence.EntityManager;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.common.database.DBCheckTemplateLb;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsStatusType;

import liquibase.database.Database;

/**
 * Update geannuleerde rijbewijsaanvragen
 */
public class DBCheckPost2 extends DBCheckTemplateLb {

  public DBCheckPost2(EntityManager entityManager, Database database, String type) {
    super(entityManager, database, type, "Status van rijbewijsaanvragen corrigeren");
  }

  @Override
  public void init() throws SQLException {
    updateOnjuistVerwerkt();
    updateOnjuistGeannuleerd();
  }

  private void updateOnjuistGeannuleerd() throws SQLException {
    String sql = "update nrd set ind_verwerkt = {0} where ind_verwerkt != {0} and c_aanvr in (select c_aanvr from nrd_status where c_stat >= {1})";
    long zCode = ZaakStatusType.VERWERKT.getCode();
    long rCode = RijbewijsStatusType.RIJBEWIJS_NIET_UITGEREIKT.getCode();
    count(update(MessageFormat.format(sql, zCode, rCode)));
  }

  private void updateOnjuistVerwerkt() throws SQLException {
    String sql = "update nrd set ind_verwerkt = {0} where ind_verwerkt != {0} and c_aanvr in (select c_aanvr from nrd_status where c_stat = {1})";
    long zCode = ZaakStatusType.GEANNULEERD.getCode();
    long rCode = RijbewijsStatusType.GEANNULEERD.getCode();
    count(update(MessageFormat.format(sql, zCode, rCode)));
  }
}
