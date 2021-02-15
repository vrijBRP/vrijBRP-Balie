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

import static nl.procura.standard.Globalfunctions.emp;

import java.util.List;

import javax.persistence.TypedQuery;

import nl.procura.gba.jpa.personen.db.Parm;
import nl.procura.gba.jpa.personen.db.Profile;
import nl.procura.gba.jpa.personen.db.Usr;

public class ParmDao extends GenericDao {

  public static final List<Parm> findParameters(String parameter) {

    StringBuilder sql = new StringBuilder();
    sql.append("select p from Parm p ");
    sql.append("where p.parm = :parm ");

    TypedQuery<Parm> q = createQuery(sql.toString(), Parm.class);
    q.setParameter("parm", parameter);
    return q.getResultList();
  }

  public static final List<Parm> findParameters(List<Long> cUsrs, List<Long> cProfiles, String... parameters) {

    StringBuilder andParm = new StringBuilder();

    if ((parameters != null) && (parameters.length > 0)) {
      andParm.append("and (");

      int i = 0;
      for (String parm : parameters) {

        if (i > 0) {
          andParm.append(" or ");
        }

        andParm.append("p.parm = '");
        andParm.append(parm);
        andParm.append("'");
        i++;
      }

      andParm.append(") ");
    }

    StringBuilder sql = new StringBuilder();
    sql.append("select p from Parm p ");
    sql.append("where ((p.usr.cUsr = 0 and p.profile.cProfile = 0) ");

    if (!cProfiles.isEmpty()) {
      sql.append("or (p.usr.cUsr = 0 and p.profile.cProfile > 0 and p.profile.cProfile in :c_profile) ");
    }

    if (!cUsrs.isEmpty()) {
      sql.append("or (p.profile.cProfile = 0 and p.usr.cUsr > 0 and p.usr.cUsr in :c_usr) ");
    }

    sql.append(") ");
    sql.append(andParm.toString());
    sql.append("order by p.usr.cUsr asc, p.profile.cProfile asc");

    TypedQuery<Parm> q = createQuery(sql.toString(), Parm.class);

    if (!cProfiles.isEmpty()) {
      q.setParameter("c_profile", cProfiles);
    }

    if (!cUsrs.isEmpty()) {
      q.setParameter("c_usr", cUsrs);
    }

    return q.getResultList();
  }

  public static final Parm findParameter(String parm, long cUsr, long cProfile) {

    StringBuilder sb = new StringBuilder();
    sb.append("select p from Parm p where p.parm = :p");
    sb.append(" and p.usr.cUsr = :c");
    sb.append(" and p.profile.cProfile = :x");

    TypedQuery<Parm> q = createQuery(sb.toString(), Parm.class);
    q.setParameter("p", parm);
    q.setParameter("c", cUsr);
    q.setParameter("x", cProfile);

    for (Parm p : q.getResultList()) {
      return p;
    }

    return null;
  }

  public static final void saveParameter(String parm, String value, long cUsr, long cProfile) {

    Parm p = findParameter(parm, cUsr, cProfile);

    if (emp(value) && (p == null)) {
      return;
    } else if (emp(value) && (p != null)) {
      removeEntity(p);
    } else if (p != null) {
      p.setValue(value);
      saveEntity(p);
    } else {
      p = new Parm();
      p.setParm(parm);
      p.setValue(value);
      p.setUsr(find(Usr.class, cUsr));
      p.setProfile(find(Profile.class, cProfile));
      saveEntity(p);
    }
  }
}
