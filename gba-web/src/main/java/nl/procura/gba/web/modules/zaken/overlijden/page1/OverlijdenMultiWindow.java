/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.overlijden.page1;

import nl.procura.gba.common.ZaakFragment;
import nl.procura.gba.web.modules.zaken.common.ZaakMultiWindow;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.levenloos.DossierLevenloos;
import nl.procura.gba.web.services.bs.overlijden.DossierOverlijden;
import nl.procura.gba.web.services.bs.overlijden.gemeente.DossierOverlijdenGemeente;
import nl.procura.gba.web.services.bs.overlijden.lijkvinding.DossierLijkvinding;

public class OverlijdenMultiWindow extends ZaakMultiWindow {

  public OverlijdenMultiWindow() {
    super("Selecteer een zaak");
  }

  @Override
  protected void setKeuzes() {

    addKeuze(new KeuzeInGemeente());
    addKeuze(new KeuzeLijkvinding());
    addKeuze(new KeuzeLevenloos());
  }

  private void setAangever(DossierLevenloos zaakDossier) {
    BsPersoonUtils.kopieDossierPersoon(getGbaApplication().getServices().getPersonenWsService().getHuidige(),
        zaakDossier.getAangever());
    getGbaApplication().getServices().getMemoryService().setObject(Dossier.class, zaakDossier.getDossier());
  }

  private void setAangever(DossierOverlijden zaakDossier) {
    BsPersoonUtils.kopieDossierPersoon(getGbaApplication().getServices().getPersonenWsService().getHuidige(),
        zaakDossier.getAangever());
    getGbaApplication().getServices().getMemoryService().setObject(Dossier.class, zaakDossier.getDossier());
  }

  public class KeuzeInGemeente extends FragmentKeuze {

    public KeuzeInGemeente() {
      super("In gemeente", ZaakFragment.FR_OVERL_GEMEENTE);
    }

    @Override
    public void buttonClick(ClickEvent event) {
      setAangever(new DossierOverlijdenGemeente());
      super.buttonClick(event);
    }
  }

  public class KeuzeLevenloos extends FragmentKeuze {

    public KeuzeLevenloos() {
      super("Levenloos geboren kind", ZaakFragment.FR_LEVENLOOS);
    }

    @Override
    public void buttonClick(ClickEvent event) {
      setAangever(new DossierLevenloos());
      super.buttonClick(event);
    }
  }

  public class KeuzeLijkvinding extends FragmentKeuze {

    public KeuzeLijkvinding() {
      super("Lijkvinding", ZaakFragment.FR_LIJKVINDING);
    }

    @Override
    public void buttonClick(ClickEvent event) {
      setAangever(new DossierLijkvinding());
      super.buttonClick(event);
    }
  }
}
