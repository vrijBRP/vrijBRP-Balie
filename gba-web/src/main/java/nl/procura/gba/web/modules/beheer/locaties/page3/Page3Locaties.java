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

package nl.procura.gba.web.modules.beheer.locaties.page3;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.gebruikers.KoppelGebruikersPage;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page3Locaties extends NormalPageTemplate {

  private final Locatie locatie;

  public Page3Locaties(Locatie locatie) {

    super("Overzicht van gekoppelde gebruikers van locatie " + locatie.getLocatie());
    this.locatie = locatie;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      KoppelGebruikersPage<Locatie> couple = new KoppelGebruikersPage<Locatie>(locatie, "locaties") {

        @Override
        public void onPreviousPage() {
          Page3Locaties.this.getNavigation().goBackToPreviousPage();
        }
      };

      addExpandComponent(couple);
    }
    super.event(event);
  }
}
