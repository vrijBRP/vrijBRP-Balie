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
import nl.procura.gba.web.modules.beheer.documenten.KoppelDocumentPage;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class KoppelDocumentenAanGebruikersPage extends NormalPageTemplate {

  private final List<Gebruiker> userList;
  private boolean               koppelMeerdereGebruikers = false;
  private KoppelDocumentPage    koppelDocumentenPage     = null;

  public KoppelDocumentenAanGebruikersPage(Gebruiker user) {
    super("Overzicht van gekoppelde documenten van gebruiker " + user);
    userList = Arrays.asList(user);
  }

  public KoppelDocumentenAanGebruikersPage(List<Gebruiker> userList) {
    super("");
    this.userList = userList;
    koppelMeerdereGebruikers = true;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      addCoupleDocumentPage();
    }

    super.event(event);
  }

  public KoppelDocumentPage getCoupleDocs() {
    return koppelDocumentenPage;
  }

  public void setCoupleDocs(KoppelDocumentPage coupleDocs) {
    this.koppelDocumentenPage = coupleDocs;
  }

  private void addCoupleDocumentPage() {

    koppelDocumentenPage = new KoppelDocumentPage<Gebruiker>(userList, "gebruikers") {

      @Override
      public void onPreviousPage() {
        KoppelDocumentenAanGebruikersPage.this.getNavigation().goBackToPreviousPage();
      }
    };

    if (koppelMeerdereGebruikers) {
      koppelDocumentenPage.disablePreviousButton();
    }

    addExpandComponent(koppelDocumentenPage);
  }
}
