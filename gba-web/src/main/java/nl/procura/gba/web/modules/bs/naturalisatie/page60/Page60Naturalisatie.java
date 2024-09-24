/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.bs.naturalisatie.page60;

import nl.procura.gba.web.modules.bs.naturalisatie.BsPageNaturalisatie;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page60Naturalisatie extends BsPageNaturalisatie {

  private Page60NaturalisatieForm form;

  public Page60Naturalisatie() {
    super("Nationaliteit - ceremonie");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      addButton(buttonPrev);
      addButton(buttonNext);
      form = new Page60NaturalisatieForm(getZaakDossier());

      setInfo("Leg de afspraak voor de ceremonie vast en of aanvrager hierbij aanwezig is geweest. "
          + "Druk op Volgende (F2) om verder te gaan.");

      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public boolean checkPage() {
    if (super.checkPage()) {
      form.commit();
      getServices().getNaturalisatieService().save(getDossier());
      return true;
    }

    return false;
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }
}
