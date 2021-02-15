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

package nl.procura.gba.web.rest.v1_0;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.services.Services;

public class GbaRestHandler {

  private final Services services;

  public GbaRestHandler(Services services) {
    this.services = services;
  }

  public BasePLExt getPersoonslijst(String bsn) {

    BasePLExt pl = getServices().getPersonenWsService().getPersoonslijst(bsn);

    if (pl != null) {
      return pl;
    }

    throw new IllegalArgumentException("Persoon niet gevonden");
  }

  public Services getServices() {
    return services;
  }
}
