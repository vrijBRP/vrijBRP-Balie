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

package nl.procura.gba.jpa.personen.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import nl.procura.gba.jpa.personen.db.Log;

public class LogDao extends GenericDao {

  public static final List<Log> findLogs(long dIn, long dEnd) {

    StringBuilder sql = new StringBuilder();
    sql.append("select x from Log x ");
    sql.append("where x.dIn >= :dIn and x.dIn <= :dEnd ");
    sql.append("order by x.dIn desc, x.tIn desc");

    TypedQuery<Log> query = createQuery(sql.toString(), Log.class);

    query.setParameter("dIn", dIn);
    query.setParameter("dEnd", dEnd);

    return query.getResultList();
  }

  public static int removeLogs(long min, long max) {

    if (min > 0 && max > 0) {
      Query query = createQuery("delete from Log x where x.cLog >= :min and x.cLog <= :max");
      query.setParameter("min", min);
      query.setParameter("max", max);
      return query.executeUpdate();
    }

    return 0;
  }
}
