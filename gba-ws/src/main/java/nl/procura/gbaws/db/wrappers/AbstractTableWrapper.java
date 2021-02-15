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

package nl.procura.gbaws.db.wrappers;

import java.io.Serializable;

import javax.persistence.EntityManager;

import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;
import nl.procura.gbaws.db.interfaces.TableWrapper;

public abstract class AbstractTableWrapper<T, K> implements TableWrapper<T, K>, Serializable {

  private T table;

  public AbstractTableWrapper(T table) {
    this.table = table;
  }

  public boolean isTable() {
    return getTable() != null;
  }

  @Override
  public T getTable() {
    return table;
  }

  public void setTable(T table) {
    this.table = table;
  }

  protected Object find(EntityManager m, TableWrapper tableWrapper) {
    return m.find(tableWrapper.getTable().getClass(), tableWrapper.getPk());
  }

  protected <E> E merge(EntityManager m, E object) {
    E returnObject = m.merge(object);
    m.flush();
    return returnObject;
  }

  protected <E> E mergeAndCommit(E object) {

    EntityManager m = GbaWsJpa.getManager();
    m.getTransaction().begin();
    E returnObject = merge(m, object);
    m.getTransaction().commit();
    return returnObject;
  }

  public void mergeAndCommit() {
    setTable(mergeAndCommit(getTable()));
  }

  public void persist(EntityManager m, TableWrapper tableWrapper) {
    m.persist(tableWrapper.getTable());
  }

  public void remove(EntityManager m, TableWrapper tableWrapper) {
    m.remove(m.find(tableWrapper.getTable().getClass(), tableWrapper.getPk()));
  }

  public void remove(EntityManager m) {
    m.remove(find(m, this));
  }

  public void persist(EntityManager m) {
    persist(m, this);
  }

  @Override
  public int hashCode() {

    final Object key = getPk();

    if (key == null) {
      return super.hashCode();
    }

    if (key instanceof Number) {

      final Number number = (Number) key;
      final int intValue = number.intValue();
      return intValue <= 0 ? super.hashCode() : intValue;
    }

    return 31 + key.hashCode();
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    final AbstractTableWrapper other = (AbstractTableWrapper) obj;
    final int key = hashCode();
    final int otherKey = other.hashCode();

    return key == otherKey;
  }

}
