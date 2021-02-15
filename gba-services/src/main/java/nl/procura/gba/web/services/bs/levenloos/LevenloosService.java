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

package nl.procura.gba.web.services.bs.levenloos;

import static nl.procura.gba.web.common.tables.GbaTables.LAND;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.geboorte.GeboorteService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;

public class LevenloosService extends GeboorteService {

  public LevenloosService() {
    super("Levenloos", ZaakType.LEVENLOOS);
  }

  @Override
  public Zaak getNewZaak() {
    return new DossierLevenloos().getDossier();
  }

  @Override
  public Dossier getStandardZaak(Dossier zaak) {

    Dossier returnZaak = super.getStandardZaak(zaak);
    DossierLevenloos zaakDossier = (DossierLevenloos) returnZaak.getZaakDossier();
    zaakDossier.setLandBestemming(LAND.get(zaakDossier.getcLandBest()));

    return zaak;
  }
}
