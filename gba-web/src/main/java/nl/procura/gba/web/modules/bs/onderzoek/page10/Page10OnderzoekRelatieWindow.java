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

package nl.procura.gba.web.modules.bs.onderzoek.page10;

import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.QuickSearchPersonConfig;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.page3.Page3QuickSearch;

public class Page10OnderzoekRelatieWindow extends GbaModalWindow {

  private final QuickSearchPersonConfig config;

  public Page10OnderzoekRelatieWindow(QuickSearchPersonConfig config) {
    super("Zoek personen (Druk op escape om te sluiten)", "900px");
    this.config = config;
  }

  @Override
  public void attach() {
    super.attach();
    if (config.hasBsnOrAnr()) {
      GbaTabsheet tabSheet = new GbaTabsheet();
      tabSheet.setSizeFull();
      tabSheet.setNoBorderTop();
      tabSheet.addTab(new Page3QuickSearch(config), "Gerelateerden");
      config.getPages().forEach((key, value) -> tabSheet.addTab(value, key));
      setContent(tabSheet);
    }
  }
}
