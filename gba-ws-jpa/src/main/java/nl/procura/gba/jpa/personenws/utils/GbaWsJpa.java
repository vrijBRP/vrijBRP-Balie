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

package nl.procura.gba.jpa.personenws.utils;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.sessions.Session;

import nl.procura.standard.threadlocal.ThreadLocalStorage;

public final class GbaWsJpa {

  private static final String PERSISTENCE_NAME = "personen-ws-jpa";

  private GbaWsJpa() {
  }

  /**
   * Create new entityManger from the factory
   */
  public static EntityManager createManager(Properties properties) {
    return GbaWsEclipseLinkUtil.createEntityManager(PERSISTENCE_NAME, properties);
  }

  /**
   * Create new entityManger from the factory
   */
  public static EntityManagerFactory getFactory(Properties properties) {
    return GbaWsEclipseLinkUtil.getEntityManagerFactory(PERSISTENCE_NAME, properties);
  }

  /**
   * Get entityManager from the threadLocalStorage. No need to close this one
   */
  public static EntityManager getManager() {
    return ThreadLocalStorage.get(GbaWsJpaStorage.class).getManager();
  }

  public static Session getSession() {
    return ((EntityManagerImpl) getManager().getDelegate()).getActiveSession();
  }
}
