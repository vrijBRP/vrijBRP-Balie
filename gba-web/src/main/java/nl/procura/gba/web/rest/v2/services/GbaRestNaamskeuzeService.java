/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.rest.v2.services;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.MOEDER;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.PARTNER_ANDERE_OUDER;

import nl.procura.gba.web.rest.v2.model.zaken.base.naamskeuze.GbaRestNaamskeuze;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GbaRestNaamskeuzeService extends GbaRestAbstractService {

  public GbaRestNaamskeuze toGbaRestNaamskeuze(Dossier zaak) {
    DossierNaamskeuze naamskeuze = (DossierNaamskeuze) zaak.getZaakDossier();
    GbaRestNaamskeuze restNaamskeuze = new GbaRestNaamskeuze();
    GbaRestDossierService dossierService = getRestServices().getDossierService();
    restNaamskeuze.setMoeder(dossierService.getRestPersoon(zaak, MOEDER));
    restNaamskeuze.setPartner(dossierService.getRestPersoon(zaak, PARTNER_ANDERE_OUDER));
    restNaamskeuze.setNamenrecht(GbaRestGeboorteService.toGbaRestGeboorteNamenrecht(naamskeuze));
    return restNaamskeuze;
  }
}
