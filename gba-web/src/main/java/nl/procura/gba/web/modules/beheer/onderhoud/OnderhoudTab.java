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
import nl.procura.gba.web.modules.beheer.onderhoud.page1.tab9.Tab9OnderhoudPage;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.layout.page.PageLayout;

public class OnderhoudTab extends PageLayout {

  private final GbaTabsheet          tabs                 = new GbaTabsheet();
  private final Tab1OnderhoudPage    tab1OnderhoudPage    = new Tab1OnderhoudPage();
  private final Tab2CertificatenPage tab2CertificatenPage = new Tab2CertificatenPage();
  private final Tab3OnderhoudPage    tab3OnderhoudPage    = new Tab3OnderhoudPage();
  private final Tab6OnderhoudPage    tab6OnderhoudPage    = new Tab6OnderhoudPage();
  private final Tab8OnderhoudPage    tab8OnderhoudPage    = new Tab8OnderhoudPage();
  private final Tab9OnderhoudPage    tab9OnderhoudPage    = new Tab9OnderhoudPage();

  public OnderhoudTab() {
    tabs.addStyleName(GbaWebTheme.TABSHEET_TOP);
    tabs.addTab(tab1OnderhoudPage, "Info", null);
    tabs.addTab(tab2CertificatenPage, "Certificaten", null);
    tabs.addTab(tab3OnderhoudPage, "Systeeminformatie", null);
    tabs.addTab(tab6OnderhoudPage, "Geladen gegevens", null);
    tabs.addTab(tab8OnderhoudPage, "Sessies", null);
    tabs.addTab(tab9OnderhoudPage, "Licentie", null);

    addExpandComponent(tabs);
  }
}
