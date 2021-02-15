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

package nl.procura.gbaws.requests.wk;

import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;

import nl.procura.diensten.gba.ple.procura.utils.jpa.PLEJpaManager;
import nl.procura.diensten.gba.wk.baseWK.BaseWKBuilder;
import nl.procura.diensten.gba.wk.procura.WK;
import nl.procura.diensten.gba.wk.procura.argumenten.WKArgumentsLogger;
import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.gbaws.db.jpa.PleJpaUtils;

public class WkRequestHandler {

  private final nl.procura.gbaws.requests.Logger logger = new nl.procura.gbaws.requests.Logger();
  private ZoekArgumenten                         args   = null;

  /**
   * Zoek
   */
  public void find(BaseWKBuilder handler, ZoekArgumenten args) {

    this.args = args;

    final PLEJpaManager em = PleJpaUtils.createManager();

    try {

      getLogger().chapter("Zoekargumenten");

      for (final Entry<String, Object> e : new WKArgumentsLogger(getArgs()).getMap().entrySet()) {
        getLogger().item(e.getKey(), e.getValue());
      }

      final WK wk = new WK(em, getArgs());
      wk.setBuilder(handler);
      wk.find();
    } finally {
      IOUtils.closeQuietly(em);
    }

    getLogger().chapter("Resultaat");
    getLogger().item("Aantal woningkaarten", handler.getZoekResultaat().getBaseWKs().size());
  }

  public nl.procura.gbaws.requests.Logger getLogger() {
    return logger;
  }

  public ZoekArgumenten getArgs() {
    return args;
  }

  public void setArgs(ZoekArgumenten args) {
    this.args = args;
  }
}
