/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.beheer.documenten;

import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab1.Tab1DocumentenModule;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab2.Tab2DocumentenPage;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab3.Tab3DocumentenModule;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab4.Tab4DocumentenModule;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab5.Tab5DocumentenModule;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab6.Tab6DocumentenModule;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab7.Tab7DocumentenModule;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab8.Tab8DocumentenModule;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab9.Tab9DocumentenModule;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.layout.page.PageLayout;

public class DocumentenTab extends PageLayout {

  public DocumentenTab() {
    GbaTabsheet tabs = new GbaTabsheet();
    tabs.addStyleName(GbaWebTheme.TABSHEET_TOP);

    tabs.addTab(new Tab1DocumentenModule(), "Documenten");
    tabs.addTab(new Tab2DocumentenPage(), "Sjablonen");
    tabs.addTab(new Tab3DocumentenModule(), "Afnemers");
    tabs.addTab(new Tab4DocumentenModule(), "Doelen");
    tabs.addTab(new Tab5DocumentenModule(), "Printopties");
    tabs.addTab(new Tab6DocumentenModule(), "Stempels");
    tabs.addTab(new Tab7DocumentenModule(), "Kenmerken");
    tabs.addTab(new Tab8DocumentenModule(), "DMS documenttypes");
    tabs.addTab(new Tab9DocumentenModule(), "Vertalingen");

    addExpandComponent(tabs);
  }
}
