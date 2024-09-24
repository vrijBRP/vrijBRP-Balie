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

package nl.procura.diensten.gba.ple.procura.utils.jpa;

import java.io.Closeable;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.sessions.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.commons.core.exceptions.ProException;

public class PLEJpaManager implements Closeable {

  private final static Logger LOGGER = LoggerFactory.getLogger(PLEJpaManager.class);

  private final EntityManager  manager;
  private final PLEJpaListener listener;

  public PLEJpaManager(EntityManager manager) {
    this.manager = manager;
    this.listener = new PLEJpaListener(manager);
    this.listener.start();
  }

  public void begin() {
    getTransaction().begin();
  }

  @Override
  public void close() {
    try {
      getManager().close();
    } catch (Exception e) {
      LOGGER.debug(e.toString());
    }
  }

  public void commit() {
    try {
      getTransaction().commit();
    } catch (Exception e) {
      LOGGER.debug(e.toString());
    }
  }

  public Query createNativeQuery(String sql) {
    return getManager().createNativeQuery(sql);
  }

  public Query createQuery(String sql) {
    return getManager().createQuery(sql);
  }

  public <T> TypedQuery<T> createQuery(String sql, Class<T> c) {
    return getManager().createQuery(sql, c);
  }

  public <T> T find(Class<T> c, Object o) {
    return getManager().find(c, o);
  }

  public <T> T merge(T o) {
    return getManager().merge(o);
  }

  public void refresh(Object o) {
    getManager().refresh(o);
  }

  public void remove(Object o) {
    getManager().remove(o);
  }

  public EntityManager getManager() {
    if (listener.isTimedOut()) {
      throw new ProException(
          "De verbinding met de database is gesloten na de maximale zoektijd van 30 seconden.");
    }

    return manager;
  }

  public Session getSession() {
    return ((EntityManagerImpl) getManager().getDelegate()).getActiveSession();
  }

  public EntityTransaction getTransaction() {
    return getManager().getTransaction();
  }
}
