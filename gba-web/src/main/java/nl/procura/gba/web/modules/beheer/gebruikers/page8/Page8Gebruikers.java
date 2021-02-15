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

package nl.procura.gba.web.modules.beheer.gebruikers.page8;

import java.util.List;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.tabsheet.BeheerTabsheet;
import nl.procura.gba.web.modules.beheer.gebruikers.KoppelDocumentenAanGebruikersPage;
import nl.procura.gba.web.modules.beheer.gebruikers.KoppelbaarAanGebruikerType;
import nl.procura.gba.web.modules.beheer.gebruikers.SelectedUsersPage;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.layout.tabsheet.LazyTabChangeListener;

public class Page8Gebruikers extends NormalPageTemplate {

  private final BeheerTabsheet  tabs = new BeheerTabsheet();
  private final List<Gebruiker> userList;

  public Page8Gebruikers(List<Gebruiker> userList) {

    super("Documenten koppelen aan gebruikers");

    this.userList = userList;

    addButton(buttonPrev);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      final SelectedUsersPage usersTab = new SelectedUsersPage(userList, KoppelbaarAanGebruikerType.DOCUMENT);
      final KoppelDocumentenAanGebruikersPage docTab = new KoppelDocumentenAanGebruikersPage(userList);

      tabs.addTab("Geselecteerde gebruikers", (LazyTabChangeListener) () -> usersTab);

      tabs.addTab("Documenten", (LazyTabChangeListener) () -> docTab);
      addComponent(tabs);
    }
    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }
}
