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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page200;

import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.AANGEVER;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.modules.bs.overlijden.levenloos.ModuleLevenloos;
import nl.procura.gba.web.modules.bs.overlijden.levenloos.overzicht.LevenloosOverzichtBuilder;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page101.Page101Zaken;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.levenloos.DossierLevenloos;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.functies.VaadinUtils;

/**
 * Tonen levenloos geboren kind
 */
public class Page200Zaken extends ZakenregisterOptiePage<Dossier> {

  public Page200Zaken(Dossier zaak) {

    super(zaak, "Zakenregister - dossier - levenloos geboren kind");

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
    LevenloosOverzichtBuilder.addTab(tabsheet, getZaak().getZaakDossier());
  }

  @Override
  protected ZaakPersoonType[] getZoekpersoonTypes() {
    return new ZaakPersoonType[]{ AANGEVER };
  }

  @Override
  protected void goToDocument() {

    DossierLevenloos levenloos = to(getZaak().getZaakDossier(), DossierLevenloos.class);

    List<DocumentType> types = new ArrayList<>();
    types.add(DocumentType.LEVENLOOS);

    if (levenloos.isSprakeLatereVermelding()) {
      types.add(DocumentType.LATERE_VERMELDING_ERK);
    }

    getNavigation().goToPage(new Page101Zaken(getZaak(), getTitle(), types.toArray(new DocumentType[0])));
  }

  @Override
  protected void goToPersoon(ZaakPersoonType type) {
    goToPersoon("zaken.overlijden", getZaak().getAnummer(), getZaak().getBurgerServiceNummer());
  }

  @Override
  protected void goToZaak() {

    MainModuleContainer mainModule = VaadinUtils.getChild(getWindow(), MainModuleContainer.class);

    mainModule.getNavigation().addPage(new ModuleLevenloos(getZaak()));
  }
}
