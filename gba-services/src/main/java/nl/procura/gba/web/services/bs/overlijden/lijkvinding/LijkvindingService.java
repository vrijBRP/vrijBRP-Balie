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

package nl.procura.gba.web.services.bs.overlijden.lijkvinding;

import static nl.procura.gba.web.common.tables.GbaTables.LAND;
import static nl.procura.gba.web.common.tables.GbaTables.PLAATS;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.overlijden.AbstractDossierOverlijden;
import nl.procura.gba.web.services.bs.overlijden.DossierOverlijden;
import nl.procura.gba.web.services.bs.overlijden.OverlijdenService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;

public class LijkvindingService extends OverlijdenService {

  public LijkvindingService() {
    super("Lijkvinding", ZaakType.LIJKVINDING);
  }

  @Override
  public Zaak getNewZaak() {
    return aanvullenZaak(new DossierLijkvinding().getDossier());
  }

  @Override
  @ThrowException("Het is niet mogelijk om de zaak op te vragen")
  public Dossier getStandardZaak(Dossier zaak) {
    DossierLijkvinding zaakDossier = getServices().getDossierService().getZaakDossier(zaak, DossierLijkvinding.class);
    zaakDossier.setPlaatsLijkvinding(PLAATS.get(zaakDossier.getcOverlGem()));
    zaakDossier.setLandBestemming(LAND.get(zaakDossier.getcLandBest()));
    getServices().getDossierService().getOverigDossier(zaak);
    return super.getStandardZaak(zaak);
  }

  @Override
  @Transactional
  @ThrowException("Fout bij opslaan overlijden dossier")
  public void save(Dossier zaak) {
    aanvullenZaak(zaak);
    zaak.setDescr(getDescription((DossierOverlijden) zaak.getZaakDossier()));
    getServices().getDossierService().saveDossier(zaak).savePersonen(zaak).saveAktes(zaak);
    getServices().getDossierService().saveZaakDossier(zaak.getZaakDossier());
    saveUittreksels((AbstractDossierOverlijden) zaak.getZaakDossier());
    saveCorrespondentie((AbstractDossierOverlijden) zaak.getZaakDossier());
  }
}
