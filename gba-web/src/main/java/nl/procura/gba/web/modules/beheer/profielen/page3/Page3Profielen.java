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

package nl.procura.gba.web.modules.beheer.profielen.page3;

import nl.procura.gba.web.modules.beheer.gebruikers.KoppelGebruikerTabel;
import nl.procura.gba.web.modules.beheer.gebruikers.KoppelGebruikersPage;
import nl.procura.gba.web.modules.beheer.profielen.ModuleProfielPageTemplate;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page3Profielen extends ModuleProfielPageTemplate {

  private KoppelGebruikerTabel<Profiel> table = null;
  private final Profiel                 profiel;

  public Page3Profielen(Profiel profiel) {

    super("Overzicht van gekoppelde gebruikers van profiel " + profiel.getProfiel());
    this.profiel = profiel;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      KoppelGebruikersPage<Profiel> couple = new KoppelGebruikersPage<Profiel>(profiel, "profielen") {

        @Override
        public void onPreviousPage() {
          Page3Profielen.this.getNavigation().goBackToPreviousPage();
        }
      };

      addExpandComponent(couple);
    }

    super.event(event);
  }

  public KoppelGebruikerTabel<Profiel> getTable() {
    return table;
  }

  public void setTable(KoppelGebruikerTabel<Profiel> table) {
    this.table = table;
  }
}
