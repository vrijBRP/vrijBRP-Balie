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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page300;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.KIND;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.ZaakFragment;
import nl.procura.gba.web.modules.bs.lv.overzicht.LvOverzichtBuilder;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page101.Page101Zaken;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.lv.afstamming.DossierLv;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;

public class Page300Zaken extends ZakenregisterOptiePage<Dossier> {

  public Page300Zaken(Dossier zaak) {
    super(zaak, "Zakenregister - latere vermelding");
    addButton(buttonPrev);
  }

  @Override
  protected void addOptieButtons() {
    addOptieButton(buttonAanpassen);
    addOptieButton(buttonDoc);
    addOptieButton(buttonPersonen);
  }

  @Override
  protected void addTabs(ZaakTabsheet<Dossier> tabsheet) {
    LvOverzichtBuilder.addTab(tabsheet, getZaak().getZaakDossier());
  }

  @Override
  protected ZaakPersoonType[] getZoekpersoonTypes() {
    return new ZaakPersoonType[]{ KIND };
  }

  @Override
  protected void goToDocument() {
    List<DocumentType> types = new ArrayList<>();
    DossierLv zaakDossier = (DossierLv) getZaak().getZaakDossier();
    types.add(zaakDossier.getSoort().getDocumentType());
    getNavigation().goToPage(new Page101Zaken(getZaak(), getTitle(), types.toArray(new DocumentType[0])));
  }

  @Override
  protected void goToPersoon(ZaakPersoonType type) {
    switch (type) {
      case KIND:
      default:
        goToPersoon("zaken.lv", getZaak().getAnummer(), getZaak().getBurgerServiceNummer());
        break;
    }
  }

  @Override
  protected void goToZaak() {
    goToZaak(ZaakFragment.FR_LV);
  }
}
