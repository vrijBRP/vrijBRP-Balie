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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page150;

import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.ERKENNER;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.MOEDER;

import java.util.ArrayList;
import java.util.List;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.gba.common.ZaakFragment;
import nl.procura.gba.web.modules.bs.erkenning.overzicht.ErkenningOverzichtBuilder;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page101.Page101Zaken;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.windows.home.HomeWindow;

/**
 * Tonen erkenning
 */
public class Page150Zaken extends ZakenregisterOptiePage<Dossier> {

  public Page150Zaken(Dossier zaak) {
    super(zaak, "Zakenregister - erkenning");
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

    buttonAanpassen.setEnabled(getApplication().isProfielActie(ProfielActie.UPDATE_ZAAK_AFSTAMMING));
  }

  @Override
  protected void addTabs(ZaakTabsheet<Dossier> tabsheet) {
    ErkenningOverzichtBuilder.addTab(tabsheet, getZaak().getZaakDossier());
  }

  @Override
  protected ZaakPersoonType[] getZoekpersoonTypes() {
    return new ZaakPersoonType[]{ MOEDER, ERKENNER };
  }

  @Override
  protected void goToDocument() {

    DossierErkenning dossierErkenning = to(getZaak().getZaakDossier(), DossierErkenning.class);

    List<DocumentType> types = new ArrayList<>();
    types.add(DocumentType.ERKENNING);

    if (dossierErkenning.isSprakeLatereVermelding()) {
      types.add(DocumentType.LATERE_VERMELDING_ERK);
    }

    getNavigation().goToPage(new Page101Zaken(getZaak(), getTitle(), types.toArray(new DocumentType[0])));
  }

  @Override
  protected void goToPersoon(ZaakPersoonType type) {

    DossierErkenning dossier = (DossierErkenning) getZaak().getZaakDossier();

    switch (type) {
      case MOEDER:
        goToPersoon("zaken.afstamming", dossier.getMoeder().getAnummer(),
            dossier.getMoeder().getBurgerServiceNummer());
        break;

      case AANGEVER:
      default:
        goToPersoon("zaken.afstamming", getZaak().getAnummer(), getZaak().getBurgerServiceNummer());
        break;
    }

  }

  @Override
  protected void goToZaak() {
    DossierErkenning dossierErkenning = to(getZaak().getZaakDossier(), DossierErkenning.class);
    if (dossierErkenning.isErkenningBijAangifte()) {
      DossierGeboorte geboorte = getServices().getErkenningService().getGekoppeldeGeboorte(dossierErkenning);

      if (geboorte != null) {
        getApplication().getServices().getMemoryService().setObject(Dossier.class, geboorte.getDossier());
        getApplication().openWindow(getParentWindow(), new HomeWindow(), ZaakFragment.FR_GEBOORTE);
        getApplication().closeAllModalWindows(getApplication().getWindows());

      } else {
        throw new ProException("De geboortezaak kan niet meer worden gevonden");
      }
    } else {
      goToZaak(ZaakFragment.FR_ERKENNING);
    }
  }
}
