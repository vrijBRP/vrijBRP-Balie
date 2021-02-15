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

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import nl.procura.gba.jpa.personenws.db.Request;
import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;
import nl.procura.gbaws.db.wrappers.RequestWrapper;

public class RequestDao extends GenericDao {

  @SuppressWarnings("unchecked")
  public static List<RequestWrapper> getRequests() {

    final List<RequestWrapper> l = new ArrayList<>();
    final List<Request> list = GbaWsJpa.getManager().createQuery(
        "select r from Request r order by r.cRequest asc").getResultList();

    for (final Request request : list) {
      l.add(new RequestWrapper(request));
    }

    return l;
  }

  public static RequestWrapper get(Integer pk) {
    return new RequestWrapper(find(Request.class, pk));
  }

  @SuppressWarnings("unchecked")
  public static List<Integer> getRequests(long cRequest, long dFrom, long dEnd, int cUsr, String keyword) {

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("crequest", cRequest);

    StringBuilder sql = new StringBuilder();
    sql.append("select r.cRequest from Request r where r.cRequest > :crequest ");
    sql.append(" and r.dIn >= :dFrom ");
    sql.append(" and r.dIn <= :dEnd ");

    parameters.put("dFrom", dFrom);
    parameters.put("dEnd", dEnd);

    if (pos(cUsr)) {
      sql.append(" and r.cUsr.cUsr = :cUsr");
      parameters.put("cUsr", cUsr);
    }

    if (fil(keyword)) {
      sql.append(" and r.content like :keyword ");
      parameters.put("keyword", "%" + keyword + "%");
    }

    sql.append(" order by r.cRequest desc");

    final TypedQuery<Integer> query = GbaWsJpa.getManager().createQuery(sql.toString(), Integer.class);
    parameters.entrySet().stream().forEach(ks -> query.setParameter(ks.getKey(), ks.getValue()));
    return query.getResultList();
  }
}
