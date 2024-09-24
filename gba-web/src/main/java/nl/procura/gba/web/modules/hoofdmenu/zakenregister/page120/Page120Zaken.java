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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page120;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.AANGEVER;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.OVERLEDENE;

import nl.procura.gba.common.ZaakFragment;
import nl.procura.gba.web.modules.bs.overlijden.gemeente.overzicht.OverlijdenGemeenteOverzichtBuilder;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page101.Page101Zaken;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.overlijden.DossierOverlijden;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.windows.home.HomeWindow;

/**
 * Tonen overlijdendossier
 */
public class Page120Zaken extends ZakenregisterOptiePage<Dossier> {

  public Page120Zaken(Dossier zaak) {
    super(zaak, "Zakenregister - dossier - Overlijden in gemeente");
    addButton(buttonPrev);
  }

  @Override
  protected void addOptieButtons() {
    addOptieButton(buttonAanpassen);
    addOptieButton(buttonDoc);
    addOptieButton(buttonPersonen);
    addOptieButton(buttonKlappers);
    addOptieButton(buttonFiat);
    addOptieButton(buttonVerwerken);

    buttonAanpassen.setEnabled(getApplication().isProfielActie(ProfielActie.UPDATE_ZAAK_OVERLIJDEN));
  }

  @Override
  protected void addTabs(ZaakTabsheet<Dossier> tabsheet) {
    OverlijdenGemeenteOverzichtBuilder.addTab(tabsheet, getZaak().getZaakDossier());
  }

  @Override
  protected ZaakPersoonType[] getZoekpersoonTypes() {
    return new ZaakPersoonType[]{ AANGEVER, OVERLEDENE };
  }

  @Override
  protected void goToDocument() {
    getNavigation().goToPage(new Page101Zaken(getZaak(), getTitle(), DocumentType.OVERLIJDEN));
  }

  @Override
  protected void goToPersoon(ZaakPersoonType type) {

    DossierOverlijden dossier = (DossierOverlijden) getZaak().getZaakDossier();

    switch (type) {
      case OVERLEDENE:
        goToPersoon("zaken.overlijden", dossier.getOverledene().getAnummer(),
            dossier.getOverledene().getBurgerServiceNummer());
        break;

      case AANGEVER:
      default:
        goToPersoon("zaken.overlijden", getZaak().getAnummer(), getZaak().getBurgerServiceNummer());
        break;
    }
  }

  @Override
  protected void goToZaak() {
    getApplication().getServices().getMemoryService().setObject(Dossier.class, getZaak());
    getApplication().openWindow(getParentWindow(), new HomeWindow(), ZaakFragment.FR_OVERL_GEMEENTE);
    getApplication().closeAllModalWindows(getApplication().getWindows());
  }
}
