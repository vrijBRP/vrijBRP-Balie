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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page100;

import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.*;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.modules.bs.geboorte.ModuleGeboorte;
import nl.procura.gba.web.modules.bs.geboorte.overzicht.GeboorteOverzichtBuilder;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page101.Page101Zaken;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.functies.VaadinUtils;

/**
 * Tonen geboorte dossier
 */

public class Page100Zaken extends ZakenregisterOptiePage<Dossier> {

  public Page100Zaken(Dossier zaak) {

    super(zaak, "Zakenregister - dossier - geboorte");

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
    GeboorteOverzichtBuilder.addTab(tabsheet, getZaak().getZaakDossier());
  }

  @Override
  protected ZaakPersoonType[] getZoekpersoonTypes() {
    return new ZaakPersoonType[]{ AANGEVER, MOEDER, VADER };
  }

  @Override
  protected void goToDocument() {

    DossierGeboorte geboorte = to(getZaak().getZaakDossier(), DossierGeboorte.class);

    List<DocumentType> types = new ArrayList<>();
    types.add(DocumentType.GEBOORTE);

    if (geboorte.isSprakeLatereVermeldingErkenning()) {
      types.add(DocumentType.LATERE_VERMELDING_ERK);

    } else if (geboorte.isSprakeLatereVermeldingNaamskeuze()) {
      types.add(DocumentType.LATERE_VERMELDING_NK);
    }

    getNavigation().goToPage(new Page101Zaken(getZaak(), getTitle(), types.toArray(new DocumentType[0])));
  }

  @Override
  protected void goToPersoon(ZaakPersoonType type) {

    DossierGeboorte dossier = (DossierGeboorte) getZaak().getZaakDossier();

    switch (type) {
      case AANGEVER:
        goToPersoon("pl.persoon", getZaak().getAnummer(), getZaak().getBurgerServiceNummer());
        break;

      case MOEDER:
        goToPersoon("pl.persoon", dossier.getMoeder().getAnummer(),
            dossier.getMoeder().getBurgerServiceNummer());
        break;

      case VADER:
        goToPersoon("pl.persoon", dossier.getVader().getAnummer(), dossier.getVader().getBurgerServiceNummer());
        break;

      default:
        throw new ProException("Onbekende persoon: " + type);
    }
  }

  @Override
  protected void goToZaak() {

    MainModuleContainer mainModule = VaadinUtils.getChild(getWindow(), MainModuleContainer.class);

    mainModule.getNavigation().addPage(new ModuleGeboorte(getZaak()));
  }
}
