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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page130;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.OVERLEDENE;

import nl.procura.gba.common.ZaakFragment;
import nl.procura.gba.web.modules.bs.overlijden.lijkvinding.overzicht.LijkvindingOverzichtBuilder;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page101.Page101Zaken;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;

/**
 * Tonen Lijkvindingdossier
 */

public class Page130Zaken extends ZakenregisterOptiePage<Dossier> {

  public Page130Zaken(Dossier zaak) {
    super(zaak, "Zakenregister - dossier - Lijkvinding");
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
    LijkvindingOverzichtBuilder.addTab(tabsheet, getZaak().getZaakDossier());
  }

  @Override
  protected ZaakPersoonType[] getZoekpersoonTypes() {
    return new ZaakPersoonType[]{ OVERLEDENE };
  }

  @Override
  protected void goToDocument() {
    getNavigation().goToPage(new Page101Zaken(getZaak(), getTitle(), DocumentType.LIJKVINDING));
  }

  @Override
  protected void goToPersoon(ZaakPersoonType type) {
    goToPersoon("zaken.lijkvinding", getZaak().getAnummer(), getZaak().getBurgerServiceNummer());
  }

  @Override
  protected void goToZaak() {
    goToZaak(ZaakFragment.FR_LIJKVINDING);
  }
}
