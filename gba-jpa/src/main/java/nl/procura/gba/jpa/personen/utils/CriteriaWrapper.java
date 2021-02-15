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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.jpa.personen.dao.QueryListener;

public class CriteriaWrapper<T, S> {

  private final CriteriaBuilder  builder;
  private final CriteriaQuery<S> cq;
  private final Root<T>          table;
  private final EntityManager    em;
  private final ConditionalMap   map;
  private QueryListener          queryListener;

  public CriteriaWrapper(Class<T> tableClass, Class<S> selectClass, ConditionalMap map) {
    this(tableClass, selectClass, map, null);
  }

  public CriteriaWrapper(Class<T> tableClass, Class<S> selectClass, ConditionalMap map, QueryListener queryListener) {

    this.map = map;
    this.em = GbaJpa.getManager();
    this.builder = getEm().getCriteriaBuilder();
    this.cq = builder.createQuery(selectClass);
    this.table = cq.from(tableClass);
    this.queryListener = queryListener;
  }

  public List<S> getResultList() {
    return getEm().createQuery(getCq()).getResultList();
  }

  public CriteriaBuilder getBuilder() {
    return builder;
  }

  public CriteriaQuery<S> getCq() {
    return cq;
  }

  public Root<T> getTable() {
    return table;
  }

  public EntityManager getEm() {
    return em;
  }

  public ConditionalMap getMap() {
    return map;
  }

  public QueryListener getQueryListener() {
    return queryListener;
  }
}
