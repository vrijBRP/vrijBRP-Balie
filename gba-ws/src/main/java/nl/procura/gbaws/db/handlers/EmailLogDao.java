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

package nl.procura.gbaws.db.handlers;

import java.util.List;

import javax.persistence.TypedQuery;

import nl.procura.gba.jpa.personenws.db.EmailLog;
import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;
import nl.procura.gbaws.db.wrappers.EmailLogWrapper;

public class EmailLogDao extends GenericDao {

  public static List<Integer> getLogs() {
    String sql = "select g.cEmailLog from EmailLog g order by g.dIn desc";
    final TypedQuery<Integer> q = GbaWsJpa.getManager().createQuery(sql, Integer.class);
    return q.getResultList();
  }

  public static EmailLogWrapper get(Integer pk) {
    return new EmailLogWrapper(find(EmailLog.class, pk));
  }
}
