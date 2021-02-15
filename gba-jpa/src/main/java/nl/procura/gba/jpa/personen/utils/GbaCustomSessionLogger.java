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

package nl.procura.gba.jpa.personen.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GbaCustomSessionLogger extends AbstractSessionLog {

  public static final String SQL_STATEMENT = "sql-statement";

  private final static Logger LOGGER = LoggerFactory.getLogger(GbaCustomSessionLogger.class.getName());

  int  nr       = 0;
  long lastTime = System.currentTimeMillis();

  @Override
  public void log(SessionLogEntry entry) {

    if (LOGGER.isDebugEnabled()) {
      String sqlWithParameters = getSqlWithParameters(entry);
      if (entry.getSession() != null) {
        List<String> lists = new ArrayList();
        Object property = entry.getSession().getProperty(SQL_STATEMENT);
        if (property != null) {
          lists = (List<String>) property;
        }
        lists.add(sqlWithParameters);
        entry.getSession().setProperty(SQL_STATEMENT, lists);
      }
    }
  }

  private String getSqlWithParameters(SessionLogEntry entry) {
    String[] split = entry.getMessage().split("bind => \\[");
    String sql = split[0];
    if (split.length > 1) {
      String[] parms = split[1].replaceAll("]$", "").replaceAll("^[,\\s]+", "").split("[,\\s]+");
      for (String parm : parms) {
        sql = sql.replaceFirst("\\?", "'" + parm + "'");
      }
    }

    return sql.trim();
  }
}
