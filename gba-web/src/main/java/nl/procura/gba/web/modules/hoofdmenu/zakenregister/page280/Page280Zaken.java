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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page280;

import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.MOEDER;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.PARTNER;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.modules.bs.naamskeuze.ModuleNaamskeuze;
import nl.procura.gba.web.modules.bs.naamskeuze.overzicht.NaamskeuzeOverzichtBuilder;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page101.Page101Zaken;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page280Zaken extends ZakenregisterOptiePage<Dossier> {

  public Page280Zaken(Dossier zaak) {
    super(zaak, "Zakenregister - naamskeuze");
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
    NaamskeuzeOverzichtBuilder.addTab(tabsheet, getZaak().getZaakDossier());
  }

  @Override
  protected ZaakPersoonType[] getZoekpersoonTypes() {
    return new ZaakPersoonType[]{ MOEDER, PARTNER };
  }

  @Override
  protected void goToDocument() {

    DossierNaamskeuze dossierNaamskeuze = to(getZaak().getZaakDossier(), DossierNaamskeuze.class);

    List<DocumentType> types = new ArrayList<>();
    types.add(DocumentType.NAAMSKEUZE);

    if (dossierNaamskeuze.isSprakeLatereVermelding()) {
      types.add(DocumentType.LATERE_VERMELDING_NK);
    }

    getNavigation().goToPage(new Page101Zaken(getZaak(), getTitle(), types.toArray(new DocumentType[0])));
  }

  @Override
  protected void goToPersoon(ZaakPersoonType type) {

    DossierNaamskeuze dossier = (DossierNaamskeuze) getZaak().getZaakDossier();

    switch (type) {
      case MOEDER:
        goToPersoon("zaken.afstamming", dossier.getMoeder().getAnummer(),
            dossier.getMoeder().getBurgerServiceNummer());
        break;

      case PARTNER:
      default:
        goToPersoon("zaken.afstamming", getZaak().getAnummer(), getZaak().getBurgerServiceNummer());
        break;
    }

  }

  @Override
  protected void goToZaak() {
    MainModuleContainer mainModule = VaadinUtils.getChild(getWindow(), MainModuleContainer.class);
    mainModule.getNavigation().addPage(new ModuleNaamskeuze(getZaak()));
  }
}
