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

package nl.procura.gba.web.modules.account.meldingen.pages.page;

import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory.*;

import nl.procura.gba.web.components.layouts.page.GbaPageTemplate;
import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.modules.account.meldingen.pages.page.tab1.MeldingenTab1;
import nl.procura.gba.web.modules.account.meldingen.pages.page.tab2.MeldingenTab2;
import nl.procura.gba.web.modules.account.meldingen.pages.page.tab3.MeldingenTab3;
import nl.procura.gba.web.services.applicatie.meldingen.MeldingService;
import nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class MeldingenTabPage extends GbaPageTemplate {

  private final ServiceMeldingCategory category;

  public MeldingenTabPage(ServiceMeldingCategory category) {
    this.category = category;
    addStyleName("meldingtab");
    setMargin(false);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setSizeFull();

      GbaTabsheet tabs = new GbaTabsheet();
      tabs.setSizeFull();
      tabs.setNoBorderTop();

      MeldingService meldingService = getApplication().getServices().getMeldingService();
      int countM = meldingService.getMeldingen(WORK).size();
      int countS = meldingService.getMeldingen(SYSTEM).size();
      int countF = meldingService.getMeldingen(FAULT).size();

      tabs.addTab(MeldingenTab3.class, "Foutmeldingen (" + countF + ")", null);
      tabs.addTab(MeldingenTab2.class, "Systeemmeldingen (" + countS + ")", null);
      tabs.addTab(MeldingenTab1.class, "Herinneringen (" + countM + ")", null);

      boolean isWerkmeldingen = true;
      boolean isSysteemmeldingen = true;
      boolean isFoutmeldingen = true;

      tabs.getTab(0).setVisible(isFoutmeldingen);
      tabs.getTab(1).setVisible(isSysteemmeldingen);
      tabs.getTab(2).setVisible(isWerkmeldingen);

      if (FAULT.equals(category)) {
        tabs.setSelectedTab(0);
      }

      if (SYSTEM.equals(category)) {
        tabs.setSelectedTab(1);
      }

      if (WORK.equals(category)) {
        tabs.setSelectedTab(2);
      }

      addComponent(tabs);
    }

    super.event(event);
  }
}
