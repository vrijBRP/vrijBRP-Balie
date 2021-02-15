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

package nl.procura.gba.web.modules.beheer.locaties.page4;

import java.util.List;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.tabsheet.BeheerTabsheet;
import nl.procura.gba.web.modules.beheer.locaties.CouplePrintOptionsToLocsPage;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.vaadin.component.layout.tabsheet.LazyTabChangeListener;

public class Page4Locaties extends NormalPageTemplate {

  private final BeheerTabsheet tabs = new BeheerTabsheet();

  public Page4Locaties(List<Locatie> locations) {

    super("Printopties koppelen aan locaties");

    addButton(buttonPrev);

    final LocationsTab locationTab = new LocationsTab(locations);

    final CouplePrintOptionsToLocsPage printOptionsTab = new CouplePrintOptionsToLocsPage(locations);

    tabs.addTab("Geselecteerde locaties", (LazyTabChangeListener) () -> locationTab);

    tabs.addTab("Printopties", (LazyTabChangeListener) () -> printOptionsTab);

    addComponent(tabs);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }
}
