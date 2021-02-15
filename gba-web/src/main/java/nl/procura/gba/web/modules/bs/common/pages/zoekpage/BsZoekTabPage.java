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

package nl.procura.gba.web.modules.bs.common.pages.zoekpage;

import java.util.List;

import nl.procura.gba.web.components.layouts.page.GbaPageTemplate;
import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class BsZoekTabPage extends GbaPageTemplate {

  private final List<BsZoekTab> extraTabs;
  private final DossierPersoon  dossierPersoon;

  public BsZoekTabPage(DossierPersoon dossierPersoon, List<BsZoekTab> extraTabs) {
    this.dossierPersoon = dossierPersoon;
    this.extraTabs = extraTabs;
    setMargin(false);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      GbaTabsheet tabs = new GbaTabsheet();
      tabs.setNoBorderTop();

      tabs.addTab(new BsZoekPage(dossierPersoon), "Zoek in de BRP", null);
      for (BsZoekTab tab : extraTabs) {
        tabs.addTab(tab.getPage(), tab.getCaption(), null);
      }

      addComponent(tabs);
    }

    super.event(event);
  }
}
