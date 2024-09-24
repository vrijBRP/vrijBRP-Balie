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

package nl.procura.gba.web.modules.zaken.personmutations.overview;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabLayout;
import nl.procura.gba.web.modules.zaken.personmutations.page5.Page5PersonListMutationsLayout;
import nl.procura.gba.web.services.beheer.personmutations.PersonListMutation;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;

public class PersonMutationOverviewLayout extends VerticalLayout implements ZaakTabLayout {

  public PersonMutationOverviewLayout(final PersonListMutation mutation) {
    setSpacing(true);
    setLayout(mutation);
  }

  @Override
  public void reloadLayout(GbaApplication application, Zaak zaak) {
    setLayout((PersonListMutation) zaak);
  }

  private void setLayout(PersonListMutation mutation) {
    removeAllComponents();
    addComponent(new Page5PersonListMutationsLayout(mutation));
  }
}
