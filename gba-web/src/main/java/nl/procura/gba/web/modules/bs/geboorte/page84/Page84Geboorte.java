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

package nl.procura.gba.web.modules.bs.geboorte.page84;

import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.geboorte.BsPageGeboorte;
import nl.procura.gba.web.modules.bs.geboorte.overzicht.GeboorteOverzichtLayout;
import nl.procura.gba.web.modules.bs.overlijden.levenloos.overzicht.LevenloosOverzichtLayout;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.levenloos.DossierLevenloos;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Geboorte
 */
public class Page84Geboorte<T extends DossierGeboorte> extends BsPageGeboorte<T> {

  public Page84Geboorte() {
    this("Geboorte - overzicht");
  }

  public Page84Geboorte(String caption) {
    super(caption);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      addButton(buttonNext);

      addComponent(new BsStatusForm(getDossier()));
      setInfo("Controleer de gegevens");

      if (getZaakDossier() instanceof DossierLevenloos) {
        addComponent(new LevenloosOverzichtLayout((DossierLevenloos) getZaakDossier()));
      } else {
        addComponent(new GeboorteOverzichtLayout(getZaakDossier()));
      }
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

    super.onPreviousPage();
  }
}
