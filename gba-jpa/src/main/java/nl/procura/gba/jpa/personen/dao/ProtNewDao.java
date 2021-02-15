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

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import nl.procura.gba.jpa.personen.db.*;
import nl.procura.gba.jpa.personen.utils.GbaJpa;
import nl.procura.standard.ProcuraDate;

public class ProtNewDao extends GenericDao {

  private static final int ANUMMER   = 1;
  private static final int GEBRUIKER = 2;

  @SuppressWarnings("unchecked")
  public static List<Object[]> findGroup(long cUsr, long anr, long dFrom, long dTo, int groep) {

    EntityManager em = GbaJpa.getManager();

    StringBuilder sql = new StringBuilder();

    String gr = "p.protNew.dIn";

    if (groep == ANUMMER) {
      gr = "p.protNew.anr";

    } else if (groep == GEBRUIKER) {
      gr = "p.protNew.usr.cUsr, p.protNew.usr.usrfullname";
    }

    sql.append("select count(p) as c, " + gr + " from ProtNewSearch p ");

    sql.append("where p.protNew.dIn >= 0 ");

    if (dFrom > 0) {
      sql.append("and p.protNew.dIn >= :dFrom and p.protNew.dIn <= :dTo ");
    }

    if (cUsr > 0) {
      sql.append("and p.protNew.usr.cUsr = :cUsr ");
    }

    if (anr > 0) {
      sql.append("and p.protNew.anr = :anr ");
    }

    sql.append("group by " + gr + " order by c desc");

    Query q = em.createQuery(sql.toString());

    if (dFrom > 0) {
      q.setParameter("dFrom", dFrom);
      q.setParameter("dTo", dTo);
    }

    if (cUsr > 0) {
      q.setParameter("cUsr", cUsr);
    }

    if (anr > 0) {
      q.setParameter("anr", anr);
    }

    return q.getResultList();
  }

  public static int deleteOld() {

    long dIn = along(new ProcuraDate().addYears(-1).getSystemDate());

    EntityManager em = GbaJpa.getManager();

    Query queryNew = em.createQuery("delete from ProtNew p where p.dIn < :dIn");

    queryNew.setParameter("dIn", dIn);

    int count = queryNew.executeUpdate();

    Query queryOld = em.createQuery("delete from Prot p where p.id.dIn < :dIn");

    queryOld.setParameter("dIn", dIn);

    count += queryOld.executeUpdate();

    return count;
  }

  public static List<ProtNewSearch> find(long cUsr, long anr, long dFrom, long dTo) {

    EntityManager em = GbaJpa.getManager();

    StringBuilder sql = new StringBuilder();

    sql.append("select p from ProtNewSearch p ");

    sql.append("where p.protNew.dIn >= 0 ");

    if (dFrom > 0) {
      sql.append("and p.protNew.dIn >= :dFrom and p.protNew.dIn <= :dTo ");
    }

    if (cUsr > 0) {
      sql.append("and p.protNew.usr.cUsr = :cUsr ");
    }

    if (anr > 0) {
      sql.append("and p.protNew.anr = :anr ");
    }

    sql.append("order by p.protNew.dIn desc, p.tIn desc");

    TypedQuery<ProtNewSearch> q = em.createQuery(sql.toString(), ProtNewSearch.class);

    if (dFrom > 0) {
      q.setParameter("dFrom", dFrom);
      q.setParameter("dTo", dTo);
    }

    if (cUsr > 0) {
      q.setParameter("cUsr", cUsr);
    }

    if (anr > 0) {
      q.setParameter("anr", anr);
    }

    return q.getResultList();
  }

  public static ProtNew getProtNew(long anr, long cUsr, long dIn) {

    EntityManager em = GbaJpa.getManager();

    TypedQuery<ProtNew> q = em.createNamedQuery("Prot.find", ProtNew.class);
    q.setParameter("anr", anr);
    q.setParameter("dIn", dIn);
    q.setParameter("cUsr", cUsr);
    q.setMaxResults(1);

    for (ProtNew pn : q.getResultList()) {
      return pn;
    }

    ProtNew protNew = new ProtNew();
    protNew.setAnr(toBigDecimal(anr));
    protNew.setdIn(toBigDecimal(dIn));
    protNew.setUsr(em.find(Usr.class, cUsr));

    saveEntity(protNew);

    return protNew;
  }

  public static void addProtNewSearchAttr(ProtNewSearch protNewSearch, String categorie, String element) {

    ProtNewSearchAttrPK pk = new ProtNewSearchAttrPK();
    pk.setCProtNewSearch(protNewSearch.getCProtNewSearch());
    pk.setFieldType(categorie);
    pk.setField(element);

    ProtNewSearchAttr attr = new ProtNewSearchAttr();

    attr.setId(pk);
    attr.setProtNewSearch(protNewSearch);

    saveEntity(attr);
  }

  public static ProtNewSearch getProtNewSearch(ProtNew protNew, long searchType, long tIn) {

    ProtNewSearch protNewSearch = new ProtNewSearch();
    protNewSearch.setProtNew(protNew);
    protNewSearch.setSearchType(toBigDecimal(searchType));
    protNewSearch.setTIn(toBigDecimal(tIn));

    saveEntity(protNewSearch);

    return protNewSearch;
  }
}
