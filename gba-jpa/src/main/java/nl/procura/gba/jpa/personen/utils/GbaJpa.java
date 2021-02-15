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

import java.util.Properties;

import javax.persistence.EntityManager;

import nl.procura.standard.threadlocal.ThreadLocalStorage;

public class GbaJpa {

  private static final String PERSISTENCE_NAME = "personen-jpa";

  /**
   * Non-static method of {@link #getManager}, so class can be injected.
   */
  public EntityManager entityManager() {
    return getManager();
  }

  /**
   * Create new entityManger from the factory
   */
  public static EntityManager createManager(Properties properties) {
    return GbaEclipseLinkUtil.createEntityManager(PERSISTENCE_NAME, properties);
  }

  /**
   * Get entityManager from the threadLocalStorage. No need to close this one
   */
  public static EntityManager getManager() {
    return ThreadLocalStorage.get(GbaJpaStorage.class).getManager();
  }
}
