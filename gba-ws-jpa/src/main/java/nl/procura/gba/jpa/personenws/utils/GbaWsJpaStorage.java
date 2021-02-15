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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GbaWsJpaStorage {

  private static final Logger LOGGER = LoggerFactory.getLogger(GbaWsJpaStorage.class);

  private EntityManager manager;

  private final Properties properties;

  public GbaWsJpaStorage(Properties properties) {
    // defensive copy
    this.properties = new Properties();
    this.properties.putAll(properties);
  }

  public void remove() {

    if (manager != null) {

      LOGGER.debug("Closing EntityManager");

      if (manager.getTransaction().isActive()) {

        manager.getTransaction().rollback();

        LOGGER.error("Er is nog een actieve transactie! Eventuele database wijzigingen zijn NIET doorgevoerd.");
      }

      try {
        manager.close(); // Close entityManager
      } catch (RuntimeException e) {

        LOGGER.error("Fout bij afsluiten database: " + e.getMessage());
        throw e;
      } finally {
        manager = null;
      }

      LOGGER.debug("Remove EntityManager");
    }
  }

  public EntityManager getManager() {

    if (manager == null || !manager.isOpen()) {

      LOGGER.debug("Return new EntityManager");

      manager = GbaWsJpa.createManager(properties);
    }

    return manager;
  }

}
