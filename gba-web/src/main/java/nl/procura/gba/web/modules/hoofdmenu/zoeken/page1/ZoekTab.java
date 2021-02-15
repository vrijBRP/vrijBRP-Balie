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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1;

import nl.procura.gba.web.components.layouts.page.GbaPageTemplate;
import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab1.ModuleZoekTab1;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab2.ModuleZoekTab2;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab3.ModuleZoekTab3;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab4.ModuleZoekTab4;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.ModuleZoekTab6;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class ZoekTab extends GbaPageTemplate {

  public ZoekTab() {
    addStyleName("zoektab");
    setMargin(false);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setSizeFull();

      GbaTabsheet tabs = new GbaTabsheet();
      tabs.setSizeFull();
      tabs.setNoBorderTop();

      tabs.addTab(ModuleZoekTab1.class, "Gemeentelijk", null);
      tabs.addTab(ModuleZoekTab2.class, "Landelijk", null);
      tabs.addTab(ModuleZoekTab3.class, "Woningkaart", null);
      tabs.addTab(ModuleZoekTab4.class, "Verificatievraag", null);
      tabs.addTab(ModuleZoekTab6.class, "Presentievraag", null);

      PersonenWsService gbaWs = getServices().getPersonenWsService();

      boolean isGemeentelijk = gbaWs.isGemeentelijkZoeken();
      boolean isLandelijk = gbaWs.isGbavZoeken();
      boolean isWoningkaart = getApplication().isProfielActie(ProfielActie.SELECT_HOOFD_WONINGKAART);
      boolean isVerificatie = getApplication().isProfielActie(ProfielActie.SELECT_HOOFD_VERIFICATIEVRAAG);
      boolean isPresentievraag = getApplication().isProfielActie(
          ProfielActie.SELECT_HOOFD_UITVOEREN_PRESENTIEVRAAG);

      tabs.getTab(0).setVisible(isGemeentelijk);
      tabs.getTab(1).setVisible(isLandelijk);
      tabs.getTab(2).setVisible(isGemeentelijk && isWoningkaart);
      tabs.getTab(3).setVisible(isVerificatie);
      tabs.getTab(4).setVisible(isPresentievraag);

      addComponent(tabs);
    }

    super.event(event);
  }
}
