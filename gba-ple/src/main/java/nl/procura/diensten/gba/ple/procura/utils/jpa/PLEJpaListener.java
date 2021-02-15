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

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PLEJpaListener extends Thread {

  private static final int    ONE_SECOND     = 1000;
  private static final int    THIRTY_SECONDS = (ONE_SECOND * 30);
  private final static Logger LOGGER         = LoggerFactory.getLogger(PLEJpaListener.class);

  private EntityManager manager  = null;
  private boolean       timedOut = false;

  public PLEJpaListener(EntityManager manager) {
    setManager(manager);
  }

  public void close() {
    try {
      manager.close();
    } catch (Exception e) {
      LOGGER.debug(e.toString());
    }
  }

  @Override
  public void run() {
    long st = System.currentTimeMillis();
    while (manager.isOpen()) {
      try {
        sleep(ONE_SECOND);
      } catch (InterruptedException e) {
        LOGGER.debug(e.toString());
        close();
        Thread.currentThread().interrupt();
      }

      long et = System.currentTimeMillis();
      if ((et - st) > THIRTY_SECONDS) {
        setTimedOut(true);
        close();
      }
    }
  }

  public EntityManager getManager() {
    return manager;
  }

  public void setManager(EntityManager manager) {
    this.manager = manager;
  }

  public synchronized boolean isTimedOut() {
    return timedOut;
  }

  public synchronized void setTimedOut(boolean timedOut) {
    this.timedOut = timedOut;
  }
}
