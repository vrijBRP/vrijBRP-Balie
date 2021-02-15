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

package nl.procura.gba.web.services.bs.overlijden.buitenland;

import static nl.procura.gba.web.common.tables.GbaTables.LAND;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.overlijden.OverlijdenService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;

public class OverlijdenBuitenlandService extends OverlijdenService {

  public OverlijdenBuitenlandService() {
    super("Overlijden buitenland", ZaakType.OVERLIJDEN_IN_BUITENLAND);
  }

  @Override
  public Zaak getNewZaak() {
    return aanvullenZaak(new DossierOverlijdenBuitenland().getDossier());
  }

  @Override
  @ThrowException("Het is niet mogelijk om de zaak op te vragen")
  public Dossier getStandardZaak(Dossier zaak) {

    DossierOverlijdenBuitenland zaakDossier = getServices().getDossierService().getZaakDossier(zaak,
        DossierOverlijdenBuitenland.class);
    zaakDossier.setLandOverlijden(LAND.get(zaakDossier.getcLandOverl()));
    zaakDossier.setLandAfgifte(LAND.get(zaakDossier.getcLandAfg()));

    getServices().getDossierService().getOverigDossier(zaak);

    return super.getStandardZaak(zaak);
  }

  @Override
  @Transactional
  @ThrowException("Fout bij opslaan overlijden dossier")
  public void save(Dossier zaak) {
    aanvullenZaak(zaak);
    getServices().getDossierService().saveDossier(zaak).savePersonen(zaak).saveAktes(zaak);
    getServices().getDossierService().saveZaakDossier(zaak.getZaakDossier());
  }
}
