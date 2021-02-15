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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.overlijden.levenloos;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.LEVENLOOS;

import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.overlijden.GbaRestOverlijdenHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;

public class GbaRestLevenloosHandler extends GbaRestOverlijdenHandler {

  public GbaRestLevenloosHandler(Services services) {
    super(services);
  }

  @Override
  protected void addAlgemeneElementen(GbaRestElement parent, Dossier dossier) {
    addNationaliteiten(parent, dossier, null); // Geen datum_verkrijging, want kinderen kunnen verschillende geboortedatums krijgen.
    addPersonen(parent, dossier);
  }

  @Override
  protected void addDossierElementen(GbaRestElement dossierElement, Dossier zaak) {
    GbaRestElement levenloos = dossierElement.add(LEVENLOOS);
    addAlgemeneElementen(levenloos, zaak);
  }
}
