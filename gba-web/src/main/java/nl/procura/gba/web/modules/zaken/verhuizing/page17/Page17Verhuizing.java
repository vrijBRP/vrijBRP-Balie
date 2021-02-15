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

package nl.procura.gba.web.modules.zaken.verhuizing.page17;

import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisAanvraagPage;
import nl.procura.gba.web.modules.zaken.verhuizing.page18.Page18Verhuizing;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Samenvatting
 */

public class Page17Verhuizing extends VerhuisAanvraagPage {

  private Page17VerhuizingForm1  form1  = null;
  private Page17VerhuizingTable1 table1 = null;

  public Page17Verhuizing(VerhuisAanvraag verhuisAanvraag) {
    super("Verhuizing: overzicht", verhuisAanvraag);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      addButton(buttonNext);

      getServices().getVerhuizingService().setVolledigeZaakExtra(getAanvraag());

      form1 = new Page17VerhuizingForm1(getAanvraag());
      table1 = new Page17VerhuizingTable1(getAanvraag());

      setInfo("Controleer de gegevens en druk op Volgende (F2) om verder te gaan.");

      addComponent(form1);
      addExpandComponent(table1);
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {

    form1.commit();

    getNavigation().goToPage(new Page18Verhuizing(getAanvraag(), true));

    super.onNextPage();
  }
}
