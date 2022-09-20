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

package nl.procura.gba.web.windows.beheer.layouts;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.navigation.GbaAccordionTab;
import nl.procura.gba.web.modules.beheer.aktes.ModuleAktes;
import nl.procura.gba.web.modules.beheer.documenten.ModuleDocumenten;
import nl.procura.gba.web.modules.beheer.email.ModuleEmail;
import nl.procura.gba.web.modules.beheer.gebruikers.ModuleGebruikers;
import nl.procura.gba.web.modules.beheer.gemeenten.ModuleGemeenten;
import nl.procura.gba.web.modules.beheer.fileimport.ModuleBestandImport;
import nl.procura.gba.web.modules.beheer.kassa.ModuleKassa;
import nl.procura.gba.web.modules.beheer.locaties.ModuleLocaties;
import nl.procura.gba.web.modules.beheer.log.ModuleLog;
import nl.procura.gba.web.modules.beheer.logbestanden.ModuleLogbestanden;
import nl.procura.gba.web.modules.beheer.onderhoud.ModuleOnderhoud;
import nl.procura.gba.web.modules.beheer.overzicht.ModuleOverzichtBeheer;
import nl.procura.gba.web.modules.beheer.parameters.ModuleParameters;
import nl.procura.gba.web.modules.beheer.profielen.ModuleProfielen;
import nl.procura.gba.web.modules.beheer.protocollering.ModuleProtocollering;
import nl.procura.gba.web.modules.beheer.sms.ModuleSms;
import nl.procura.gba.web.modules.beheer.verkiezing.ModuleVerkiezing;
import nl.procura.gba.web.modules.tabellen.belanghebbende.ModuleBelanghebbende;
import nl.procura.gba.web.modules.tabellen.huwelijksambtenaar.ModuleHuwelijkAmbtenaar;
import nl.procura.gba.web.modules.tabellen.huwelijkslocatie.ModuleHuwelijkLocatie;
import nl.procura.gba.web.modules.tabellen.kennisbank.ModuleKennisbank;
import nl.procura.gba.web.modules.tabellen.overlijdenaangever.ModuleOverlijdenAangever;
import nl.procura.gba.web.modules.tabellen.riskprofile.ModuleRiskProfile;
import nl.procura.gba.web.windows.home.layouts.NavigationFooterLayout;

public class BeheerNavigation extends VerticalLayout {

  private static final long serialVersionUID = 5338958948102025352L;

  public BeheerNavigation() {

    setSizeFull();
    BeheerMenu menu = new BeheerMenu();
    BeheerAlgemeenTab beheerTab = new BeheerAlgemeenTab();

    addComponent(menu);
    addComponent(beheerTab);
    addComponent(new NavigationFooterLayout());
    setExpandRatio(beheerTab, 1f);
  }

  private class BeheerAlgemeenTab extends GbaAccordionTab {

    public BeheerAlgemeenTab() {
      getAccordeon().setSizeFull();
      setSizeFull();
    }

    @Override
    public void attach() {

      if (getComponentCount() == 0) {
        addTab(new BeheerAccordionTab1(getApplication()));
        addTab(new BeheerAccordionTab2(getApplication()));
      }

      super.attach();
    }
  }

  private class BeheerAccordionTab1 extends GbaAccordionTab {

    private static final long serialVersionUID = -7610163628334969966L;

    private BeheerAccordionTab1(GbaApplication application) {

      super("Beheer", application);
      addLink(ModuleOverzichtBeheer.class);
      addLink(ModuleParameters.class);
      addLink(ModuleLog.class);
      addLink(ModuleGebruikers.class);
      addLink(ModuleOnderhoud.class);
      addLink(ModuleProfielen.class);
      addLink(ModuleProtocollering.class);
      addLink(ModuleDocumenten.class);
      addLink(ModuleLocaties.class);
      addLink(ModuleKassa.class);
      addLink(ModuleLogbestanden.class);
      addLink(ModuleEmail.class);
      addLink(ModuleSms.class);
      addLink(ModuleVerkiezing.class);
      addLink(ModuleBestandImport.class);
    }
  }

  private class BeheerAccordionTab2 extends GbaAccordionTab {

    private static final long serialVersionUID = -5358162709951672857L;

    private BeheerAccordionTab2(GbaApplication application) {

      super("Basistabellen", application);
      addLink(ModuleBelanghebbende.class);
      addLink(ModuleHuwelijkLocatie.class);
      addLink(ModuleRiskProfile.class);
      addLink(ModuleHuwelijkAmbtenaar.class);
      addLink(ModuleOverlijdenAangever.class);
      addLink(ModuleKennisbank.class);
      //addLink (ModuleVoorraad.class);
      addLink(ModuleAktes.class);
      addLink(ModuleGemeenten.class);
    }

  }
}
