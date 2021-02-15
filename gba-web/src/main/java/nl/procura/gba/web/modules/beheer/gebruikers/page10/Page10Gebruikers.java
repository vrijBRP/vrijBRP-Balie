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

package nl.procura.gba.web.modules.beheer.gebruikers.page10;

import java.util.List;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.tabsheet.BeheerTabsheet;
import nl.procura.gba.web.modules.beheer.gebruikers.KoppelProfielenAanGebruikersPage;
import nl.procura.gba.web.modules.beheer.gebruikers.KoppelbaarAanGebruikerType;
import nl.procura.gba.web.modules.beheer.gebruikers.SelectedUsersPage;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.vaadin.component.layout.tabsheet.LazyTabChangeListener;

public class Page10Gebruikers extends NormalPageTemplate {

  private final BeheerTabsheet tabs = new BeheerTabsheet();

  public Page10Gebruikers(List<Gebruiker> userList) {

    super("Profielen koppelen aan gebruikers");
    addButton(buttonPrev);

    final SelectedUsersPage usersTab = new SelectedUsersPage(userList, KoppelbaarAanGebruikerType.PROFIEL);
    final KoppelProfielenAanGebruikersPage profTab = new KoppelProfielenAanGebruikersPage(userList);

    tabs.addTab("Geselecteerde gebruikers", (LazyTabChangeListener) () -> usersTab);

    tabs.addTab("Profielen", (LazyTabChangeListener) () -> profTab);

    addComponent(tabs);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }
}
