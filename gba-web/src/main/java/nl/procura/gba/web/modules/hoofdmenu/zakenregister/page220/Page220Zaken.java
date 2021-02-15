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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page220;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.PARTNER_1;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.PARTNER_2;

import nl.procura.gba.web.modules.bs.ontbinding.ModuleOntbinding;
import nl.procura.gba.web.modules.bs.ontbinding.overzicht.form1.OntbindingOverzichtBuilder;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page101.Page101Zaken;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierPartners;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.functies.VaadinUtils;

/**
 * Tonen ontbindingdossier
 */
public class Page220Zaken extends ZakenregisterOptiePage<Dossier> {

  public Page220Zaken(Dossier zaak) {

    super(zaak, "Zakenregister - dossier - ontbinding/einde huwelijk/GPS in gemeente");

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

    buttonAanpassen.setEnabled(getApplication().isProfielActie(ProfielActie.UPDATE_ZAAK_ONTBINDING));
  }

  @Override
  protected void addTabs(ZaakTabsheet<Dossier> tabsheet) {
    OntbindingOverzichtBuilder.addTab(tabsheet, getZaak().getZaakDossier());
  }

  @Override
  protected ZaakPersoonType[] getZoekpersoonTypes() {
    return new ZaakPersoonType[]{ PARTNER_1, PARTNER_2 };
  }

  @Override
  protected void goToDocument() {
    getNavigation().goToPage(new Page101Zaken(getZaak(), getTitle(), DocumentType.ONTBINDING_GEMEENTE));
  }

  @Override
  protected void goToPersoon(ZaakPersoonType type) {

    final DossierPartners dossier = (DossierPartners) getZaak().getZaakDossier();

    switch (type) {
      case PARTNER_1:
        goToPersoon("zaken.ontbinding", dossier.getPartner1().getAnummer(),
            dossier.getPartner1().getBurgerServiceNummer());
        break;

      case PARTNER_2:
      default:
        goToPersoon("zaken.ontbinding", dossier.getPartner2().getAnummer(),
            dossier.getPartner2().getBurgerServiceNummer());
        break;
    }
  }

  @Override
  protected void goToZaak() {

    final MainModuleContainer mainModule = VaadinUtils.getChild(getWindow(), MainModuleContainer.class);
    mainModule.getNavigation().addPage(new ModuleOntbinding(getZaak()));
  }
}
