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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.overlijden.gemeente;

import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.overlijden.GbaRestOverlijdenHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.overlijden.gemeente.DossierOverlijdenGemeente;

public class GbaRestOverlijdenGemeenteHandler extends GbaRestOverlijdenHandler {

  public GbaRestOverlijdenGemeenteHandler(Services services) {
    super(services);
  }

  @SuppressWarnings("unused")
  private void addGemeenteElementen(GbaRestElement parent, Dossier dossier) {
    DossierOverlijdenGemeente dossierOverlijden = (DossierOverlijdenGemeente) dossier.getZaakDossier();
    dossierOverlijden.getPlaatsOverlijden();
    dossierOverlijden.getLeeftijdOverledene();
    dossierOverlijden.getTijdOverlijden();
    dossierOverlijden.getDatumLijkbezorging();
    dossierOverlijden.getTermijnLijkbezorging();
    dossierOverlijden.getTermijnLijkbezorging();
    dossierOverlijden.getOntvangenDocumentLijkbezorging();
  }
}
