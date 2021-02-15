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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import nl.procura.gba.jpa.personenws.db.GbavProfile;
import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;
import nl.procura.gbaws.db.misc.ParmValues;
import nl.procura.gbaws.db.wrappers.GbavProfileWrapper;

public class GbavProfileDao extends GenericDao {

  public static List<GbavProfileWrapper> getProfiles() {

    final EntityManager em = GbaWsJpa.getManager();

    final List<GbavProfileWrapper> l = new ArrayList<>();
    final TypedQuery<GbavProfile> q = em.createQuery("select p from GbavProfile p order by p.gbavProfile",
        GbavProfile.class);
    final List<GbavProfile> list = q.getResultList();

    for (final GbavProfile profile : list) {
      l.add(new GbavProfileWrapper(profile));
    }

    return l;
  }

  public static GbavProfileWrapper getProfile(int code) {
    final EntityManager m = GbaWsJpa.getManager();
    return new GbavProfileWrapper(m.find(GbavProfile.class, code));
  }

  public static List<GbavProfileWrapper> getProfileByGbavUser(String gbavGebruiker) {

    final EntityManager em = GbaWsJpa.getManager();
    final List<GbavProfileWrapper> l = new ArrayList<>();

    StringBuilder sql = new StringBuilder();
    sql.append("select p from GbavProfile p ");
    sql.append("where p.gbavProfileParmCollection in (");
    sql.append("select pp from GbavProfileParm pp ");
    sql.append("where pp.gbavProfileParmPK.parm = :parm ");
    sql.append("and pp.value = :value)");

    final TypedQuery<GbavProfile> q = em.createQuery(sql.toString(), GbavProfile.class);
    q.setParameter("parm", ParmValues.GBAV.GBAV_USERNAME);
    q.setParameter("value", gbavGebruiker);
    final List<GbavProfile> list = q.getResultList();

    for (final GbavProfile profile : list) {
      l.add(new GbavProfileWrapper(profile));
    }

    return l;
  }
}
