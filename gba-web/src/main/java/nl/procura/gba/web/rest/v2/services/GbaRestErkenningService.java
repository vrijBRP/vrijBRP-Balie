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

package nl.procura.gba.web.rest.v2.services;

import static nl.procura.gba.web.rest.v2.model.base.GbaRestEnum.toEnum;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.ERKENNER;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.MOEDER;

import nl.procura.gba.web.rest.v2.model.zaken.erkenning.GbaRestErkenning;
import nl.procura.gba.web.rest.v2.model.zaken.erkenning.GbaRestErkenningsType;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;

public class GbaRestErkenningService extends GbaRestAbstractService {

  public GbaRestErkenning toGbaRestErkenning(Dossier zaak) {
    DossierErkenning erkenning = (DossierErkenning) zaak.getZaakDossier();

    // To implement: kinderen, aktes, nationaliteiten, afstamming, toestemming?
    GbaRestErkenning restErkenning = new GbaRestErkenning();
    GbaRestDossierService dossierService = getRestServices().getDossierService();
    restErkenning.setMoeder(dossierService.getRestPersoon(zaak, MOEDER));
    restErkenning.setErkenner(dossierService.getRestPersoon(zaak, ERKENNER));
    restErkenning
        .setErkenningsType(toEnum(GbaRestErkenningsType.values(), erkenning.getErkenningsType().getCode()));
    restErkenning.setNamenrecht(GbaRestGeboorteService.toGbaRestGeboorteNamenrecht(erkenning));
    return restErkenning;
  }

}
