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

package nl.procura.gba.web.modules.beheer.onderhoud;

import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.modules.beheer.onderhoud.page1.tab1.Tab1OnderhoudPage;
import nl.procura.gba.web.modules.beheer.onderhoud.page1.tab2.Tab2CertificatenPage;
import nl.procura.gba.web.modules.beheer.onderhoud.page1.tab3.Tab3OnderhoudPage;
import nl.procura.gba.web.modules.beheer.onderhoud.page1.tab6.Tab6OnderhoudPage;
import nl.procura.gba.web.modules.beheer.onderhoud.page1.tab8.Tab8OnderhoudPage;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.layout.page.PageLayout;

public class OnderhoudTab extends PageLayout {

  public OnderhoudTab() {
    GbaTabsheet tabs = new GbaTabsheet();
    tabs.addStyleName(GbaWebTheme.TABSHEET_TOP);
    tabs.addTab(new Tab1OnderhoudPage(), "Info", null);
    tabs.addTab(new Tab2CertificatenPage(), "Certificaten", null);
    tabs.addTab(new Tab3OnderhoudPage(), "Systeeminformatie", null);
    tabs.addTab(new Tab6OnderhoudPage(), "Geladen gegevens", null);
    tabs.addTab(new Tab8OnderhoudPage(), "Sessies", null);

    addExpandComponent(tabs);
  }
}
