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

package nl.procura.gba.web.modules.bs.overlijden.lijkvinding.page30;

import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsPersoonPage;
import nl.procura.gba.web.modules.bs.geboorte.checks.DeclarationCheckButton;
import nl.procura.gba.web.modules.bs.overlijden.checks.DeceasedInfoCheckWindow;
import nl.procura.gba.web.modules.bs.overlijden.lijkvinding.ModuleLijkvinding;
import nl.procura.gba.web.services.bs.overlijden.lijkvinding.DossierLijkvinding;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.VaadinUtils;

/**

 * <p>
 * 6 Feb. 2013 8:00:00
 */

public class Page30Lijkvinding extends BsPersoonPage<DossierLijkvinding> {

  private DeclarationCheckButton declarationButton;

  public Page30Lijkvinding() {
    super("Lijkvinding - overledene");
  }

  @Override
  public boolean checkPage() {

    if (super.checkPage()) {

      // Een ID opvragen in het Zaak-DMS
      getApplication().getServices().getZaakIdentificatieService().getDmsZaakId(getDossier());

      getServices().getLijkvindingService().save(getDossier());
      return true;
    }

    return false;
  }

  @Override
  public void addButtons() {
    super.addButtons();
    declarationButton = new DeclarationCheckButton();
    getOptieLayout().getRight().addComponent(declarationButton);
    checkDeclaration();
  }

  @Override
  public void update() {
    super.update();
    checkDeclaration();
  }

  private void checkDeclaration() {
    declarationButton.setPopupState(() -> getOverlijdenModule().getVerzoekPopupStates());
    declarationButton.check(new DeceasedInfoCheckWindow(
        getZaakDossier().getVerzoek(),
        getZaakDossier().getOverledene()),
        getZaakDossier().isVerzoekInd());
  }

  private ModuleLijkvinding getOverlijdenModule() {
    return VaadinUtils.getParent(Page30Lijkvinding.this, ModuleLijkvinding.class);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      addButton(buttonNext);

      setDossierPersoon(getZaakDossier().getOverledene());
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  @Override
  protected String getInfo() {
    return "Zoek de persoon, controleer de gegevens of vul deze in. Druk op Volgende (F2) om verder te gaan.";
  }
}
