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

package nl.procura.gba.web.modules.beheer.profielen.page12;

import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.modules.beheer.profielen.page12.tab1.Page12Profielen;
import nl.procura.gba.web.modules.beheer.profielen.page12.tab2.ModuleZaakConfiguraties;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.layout.page.PageLayout;

public class ZaakConfiguratiesTab extends PageLayout {

  private final GbaTabsheet tabs = new GbaTabsheet();

  public ZaakConfiguratiesTab(Profiel profiel) {
    setMargin(false);
    tabs.addStyleName(GbaWebTheme.TABSHEET_TOP);
    tabs.addTab(new Page12Profielen(profiel), "Koppelen");
    tabs.addTab(new ModuleZaakConfiguraties(), "Configuraties beheren");
    addExpandComponent(tabs);
  }
}
