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

package nl.procura.gba.web.modules.bs.ontbinding.page50;

import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.ontbinding.BsPageOntbinding;
import nl.procura.gba.web.modules.bs.ontbinding.overzicht.form1.OntbindingOverzichtLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page50Ontbinding extends BsPageOntbinding {

  public Page50Ontbinding() {

    super("Ontbinding/einde huwelijk/GPS in gemeente - overzicht");
  }

  @Override
  public boolean checkPage() {

    if (getDossier().getZaakDossier().isVolledig()) {

      getApplication().getServices().getOntbindingService().save(getDossier());
    }

    return true;
  }

  @Override
  public void event(PageEvent event) {

    try {

      if (event.isEvent(InitPage.class)) {

        addButton(buttonPrev);
        addButton(buttonNext);

        addComponent(new BsStatusForm(getDossier()));

        setInfo("Controleer de gegevens. Druk op Volgende (F2) om verder te gaan.");

        addComponent(new OntbindingOverzichtLayout(getZaakDossier()));
      }
    } finally {
      super.event(event);
    }
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }
}
