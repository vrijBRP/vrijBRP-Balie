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

package examples.nl.procura.gbaws;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GbaRestClientVoorbeeldenBulk extends GbaWsRestClientVoorbeelden {

  private final List<Class>     classes = new ArrayList<>();
  private final ExecutorService es;
  private final int             count;

  public GbaRestClientVoorbeeldenBulk(int count) {
    es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 3);
    this.count = count;
  }

  public static void main(String[] args) {
    GbaWsRestGuice.getInstance(GbaRestClientVoorbeeldenBulk.class);
  }

  public void add(Class cl) {
    classes.add(cl);
  }

  public void start() {
    try {
      for (int i = 0; i < count; i++) {
        es.submit(() -> {
          try {
            for (Class cl : classes) {
              cl.newInstance();
            }
          } catch (InstantiationException | IllegalAccessException e) {
            log.trace("Error", e);
          }
        });
      }

      es.shutdown();
      es.awaitTermination(30, TimeUnit.SECONDS);
      es.shutdownNow();

    } catch (InterruptedException e) {
      log.trace("Error", e);
      Thread.currentThread().interrupt();
    }
  }
}
