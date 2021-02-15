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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nl.procura.gba.jpa.personen.db.*;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

public class DossLooseDao extends DossDao {

  /*
  delete from doss
  where (c_doss not in (select c_doss_geb from doss_geb)
  and c_doss not in (select c_doss_huw from doss_huw)
  and c_doss not in (select c_doss_erk from doss_erk)
  and c_doss not in (select c_doss_overl from doss_overl)
  or c_doss not in (select c_doss from doss_pers where c_doss is not null));
  delete from doss_pers where c_doss is null and c_doss_parent_pers is null;
  delete from doss_natio where c_doss is null and c_doss_pers is null;
  */

  public static final List<Doss> getLooseDoss() {

    EntityManager em = GbaJpa.getManager();

    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Doss> query = builder.createQuery(Doss.class);
    Root<Doss> table = query.from(Doss.class);

    Predicate isGeboorte = getSubZaak(builder, query, table, DossGeb.class, 0L);
    Predicate isErkenning = getSubZaak(builder, query, table, DossErk.class, 0L);
    Predicate isHuwelijk = getSubZaak(builder, query, table, DossHuw.class, 0L);
    Predicate isOverlijden = getSubZaak(builder, query, table, DossOverl.class, 0L);

    List<Predicate> where = new ArrayList<>();
    where.add(builder.not(builder.or(isGeboorte, isErkenning, isHuwelijk, isOverlijden)));
    query.where(where.toArray(new Predicate[where.size()]));
    return em.createQuery(query).getResultList();
  }

  public static final List<DossPer> getLooseDossPer() {

    EntityManager em = GbaJpa.getManager();

    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<DossPer> query = builder.createQuery(DossPer.class);
    Root<DossPer> table = query.from(DossPer.class);

    List<Predicate> where = new ArrayList<>();
    where.add(table.get(DOSS).isNull());
    where.add(table.get(PARENT_DOSS_PER).isNull());

    query.where(where.toArray(new Predicate[where.size()]));
    return em.createQuery(query).getResultList();
  }

  public static final List<DossNatio> getLooseDossNatio() {

    EntityManager em = GbaJpa.getManager();

    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<DossNatio> query = builder.createQuery(DossNatio.class);
    Root<DossNatio> table = query.from(DossNatio.class);

    List<Predicate> where = new ArrayList<>();
    where.add(table.get(DOSS).isNull());
    where.add(table.get(DOSS_PER).isNull());

    query.where(where.toArray(new Predicate[where.size()]));
    return em.createQuery(query).getResultList();
  }
}
