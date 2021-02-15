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
import nl.procura.gba.web.modules.beheer.profielen.KoppelProfielenPage;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;

public class KoppelProfielenAanGebruikersPage extends NormalPageTemplate {

  private KoppelProfielenPage<Gebruiker> koppelProfielenPage = null;

  public KoppelProfielenAanGebruikersPage(Gebruiker user) {

    super("Overzicht van gekoppelde profielen van gebruiker " + user);

    zetVorigeKnopUit();

    toevoegenPage(Arrays.asList(user), false);
  }

  public KoppelProfielenAanGebruikersPage(List<Gebruiker> userList) {

    super("");

    zetVorigeKnopUit();

    toevoegenPage(userList, true);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  private void toevoegenPage(List<Gebruiker> userList, boolean isKoppelMeerdereGebruikers) {

    if (isKoppelMeerdereGebruikers) {
      koppelProfielenPage = new KoppelProfielenPage<>(userList, "gebruikers");
      koppelProfielenPage.disablePreviousButton();
    } else {
      koppelProfielenPage = new KoppelProfielenPage<Gebruiker>(userList, "gebruikers") {

        @Override
        public void onPreviousPage() {
          KoppelProfielenAanGebruikersPage.this.onPreviousPage();
        }
      };
    }

    addExpandComponent(koppelProfielenPage);
  }

  private void zetVorigeKnopUit() {
    buttonPrev.setVisible(false);
  }
}
