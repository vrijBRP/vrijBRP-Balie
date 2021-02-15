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

package nl.procura.gba.web.modules.beheer.gebruikers;

import java.util.Arrays;
import java.util.List;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.locaties.KoppelLocatiesPage;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;

public class KoppelLocatiesAanGebruikers extends NormalPageTemplate {

  private KoppelLocatiesPage<Gebruiker> koppelLocatiesPage = null;

  public KoppelLocatiesAanGebruikers(Gebruiker gebruiker) {
    super("Overzicht van gekoppelde locaties van gebruiker " + gebruiker);
    disablePreviousButton();
    toevoegenLocatiesPage(Arrays.asList(gebruiker), false);
  }

  public KoppelLocatiesAanGebruikers(List<Gebruiker> gebruikers) {
    super("");
    disablePreviousButton();
    toevoegenLocatiesPage(gebruikers, true);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  private void disablePreviousButton() {
    buttonPrev.setVisible(false);
  }

  private void toevoegenLocatiesPage(List<Gebruiker> userList, boolean koppelMeedereGebruikers) {

    if (koppelMeedereGebruikers) {

      koppelLocatiesPage = new KoppelLocatiesPage<>(userList, "gebruikers");
      koppelLocatiesPage.disablePreviousButton();
    } else {
      koppelLocatiesPage = new KoppelLocatiesPage<Gebruiker>(userList, "gebruikers") {

        @Override
        public void onPreviousPage() {
          KoppelLocatiesAanGebruikers.this.onPreviousPage();
        }
      };
    }

    addExpandComponent(koppelLocatiesPage);
  }
}
