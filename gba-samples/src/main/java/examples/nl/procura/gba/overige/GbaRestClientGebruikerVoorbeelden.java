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

package examples.nl.procura.gba.overige;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.proweb.rest.utils.JsonUtils;
import nl.procura.proweb.rest.v1_0.gebruiker.ProRestGebruikerAntwoord;

import examples.nl.procura.gba.GbaRestClientVoorbeelden;
import examples.nl.procura.gba.GbaRestGuice;
import examples.nl.procura.gba.GbaRestGuice.Timer;

public class GbaRestClientGebruikerVoorbeelden extends GbaRestClientVoorbeelden {

  private static final Logger LOGGER = LoggerFactory.getLogger(GbaRestClientGebruikerVoorbeelden.class);

  public GbaRestClientGebruikerVoorbeelden() {
    zoeken();
  }

  public static void main(String[] args) {
    GbaRestGuice.getInstance(GbaRestClientGebruikerVoorbeelden.class);
  }

  @Timer
  protected void zoeken() {
    LOGGER.info(JsonUtils.getPrettyObject(client.getGebruiker("sync")
        .getEntity(ProRestGebruikerAntwoord.class).getGebruiker().getNaam()));
  }
}
