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

import javax.persistence.Persistence;

import org.eclipse.persistence.internal.jpa.EntityManagerFactoryImpl;
import org.eclipse.persistence.sessions.server.ReadConnectionPool;
import org.eclipse.persistence.sessions.server.ServerSession;

public class PLEJpa {

  private EntityManagerFactoryImpl emf              = null;
  private String                   persistance_unit = "";
  private PLEJpaInterface          JpaImplementation;

  public PLEJpa() {
  }

  public synchronized void connect(String persistance_unit) {

    if ((emf == null) || !emf.isOpen()) {
      setPersistance_unit(persistance_unit);
      if (getJpaImplementation() != null) {
        emf = (EntityManagerFactoryImpl) Persistence.createEntityManagerFactory(getPersistance_unit(),
            getJpaImplementation().getPropertyMap());
      }
    }
  }

  public boolean isConnected() {
    return emf != null;
  }

  public PLEJpaManager createManager() {
    checkConnection();
    emf.getCache().evictAll();
    return new PLEJpaManager(emf.createEntityManager());
  }

  private void checkConnection() {

    try {
      ServerSession session = emf.getServerSession();
      ReadConnectionPool pool = (ReadConnectionPool) session.getReadConnectionPool();

      if (pool.isConnected()) {
        if (pool.getConnectionsAvailable().isEmpty()) {
          session.logout();
          session.login();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(
          "Er kan geen verbinding worden gemaakt met de database. Wellicht draait de database niet.", e);
    }
  }

  public PLEJpaInterface getJpaImplementation() {
    return JpaImplementation;
  }

  public void setJpaImplementation(PLEJpaInterface jpaImplementation) {
    JpaImplementation = jpaImplementation;
  }

  public String getPersistance_unit() {
    return persistance_unit;
  }

  public void setPersistance_unit(String persistance_unit) {
    this.persistance_unit = persistance_unit;
  }
}
