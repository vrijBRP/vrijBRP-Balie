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

import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import nl.procura.gba.jpa.personen.db.PlHist;
import nl.procura.gba.jpa.personen.db.PlHistPK;
import nl.procura.gba.jpa.personen.db.Usr;

public class PlHistDao extends GenericDao {

  private static final int MAX_HISTORIE = 20;

  public static final List<PlHist> find(long type, long cUsr) {
    TypedQuery<PlHist> q = createQuery(
        "select u from PlHist u where u.id.type = :type and u.usr.cUsr = :cUsr order by u.timestamp desc",
        PlHist.class);
    q.setParameter("type", type);
    q.setParameter("cUsr", cUsr);

    return q.getResultList();
  }

  public static final List<PlHist> find(long type, long cUsr, long nr) {

    StringBuilder query = new StringBuilder();
    query.append("select u from PlHist u ");
    query.append("where u.id.type = :type ");
    query.append("and u.usr.cUsr = :cUsr ");
    query.append("and u.id.nr = :nr ");
    query.append("order by u.timestamp desc");

    TypedQuery<PlHist> q = createQuery(query.toString(), PlHist.class);
    q.setParameter("type", type);
    q.setParameter("cUsr", cUsr);
    q.setParameter("nr", nr);

    return q.getResultList();
  }

  public static void add(long type, long cUsr, long nr, String oms, long bron) {

    PlHist hist = new PlHist();
    hist.setOms(oms);
    hist.setBron(toBigDecimal(bron));
    hist.setUsr(find(Usr.class, cUsr));
    hist.setTimestamp(toBigDecimal(new Date().getTime()));

    PlHistPK pk = new PlHistPK();
    pk.setCUsr(cUsr);
    pk.setType(type);
    pk.setNr(nr);
    hist.setId(pk);

    saveEntity(hist);
  }

  public static void remove(long type, long cUsr, long nr) {
    Query q = createQuery("delete from PlHist u where u.id.type = :type and u.usr.cUsr = :cUsr and u.id.nr = :nr");
    q.setParameter("cUsr", cUsr);
    q.setParameter("nr", nr);
    q.setParameter("type", type);
    q.executeUpdate();
  }

  public static void cleanUp(long cUsr) {

    TypedQuery<PlHist> q = createQuery(
        "select u from PlHist u where u.id.type = 2 and u.usr.cUsr = :cUsr order by u.timestamp desc",
        PlHist.class);

    q.setParameter("cUsr", cUsr);
    q.setMaxResults(MAX_HISTORIE);
    List<PlHist> result = q.getResultList();

    if (!result.isEmpty()) {
      BigDecimal lastTimeStamp = result.get(result.size() - 1).getTimestamp();
      Query q2 = createQuery(
          "delete from PlHist u where u.id.type = 2 and u.usr.cUsr = :cUsr and u.timestamp < :timestamp");
      q2.setParameter("cUsr", cUsr);
      q2.setParameter("timestamp", lastTimeStamp);
      q2.executeUpdate();
    }
  }
}
