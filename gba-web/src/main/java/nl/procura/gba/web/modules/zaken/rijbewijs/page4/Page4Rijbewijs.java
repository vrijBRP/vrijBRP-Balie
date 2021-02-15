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

package nl.procura.gba.web.modules.zaken.rijbewijs.page4;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.isTru;

import com.vaadin.ui.TabSheet.Tab;

import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.modules.zaken.rijbewijs.RijbewijsPage;
import nl.procura.gba.web.modules.zaken.rijbewijs.page14.Page14Rijbewijs;
import nl.procura.gba.web.modules.zaken.rijbewijs.page15.Page15Rijbewijs;
import nl.procura.rdw.messages.P0252;
import nl.procura.rdw.processen.p0252.f08.NATPERSOONGEG;
import nl.procura.rdw.processen.p0252.f08.NATPRYBMAATR;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Zoekargumentenscherm
 */
public class Page4Rijbewijs extends RijbewijsPage {

  private final P0252   p0252f1;            // Eerste raadpleging
  private final boolean naSuccesRegistratie;
  private P0252         p0252f2;            // Raadpleging als er maatregelen zijn

  public Page4Rijbewijs(P0252 p0252, boolean naSuccesRegistratie) {
    super("Overzicht raadpleging");
    this.p0252f1 = p0252;
    this.naSuccesRegistratie = naSuccesRegistratie;
    setMargin(true);
    addButton(buttonPrev);
    addStyleName("rbwtab");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      if (isMaatregelen()) {
        p0252f2 = stuur0252f2();
      }

      GbaTabsheet tabs = new GbaTabsheet();
      Page4RijbewijsSubpage1 subPage1 = new Page4RijbewijsSubpage1(p0252f1, p0252f2) {

        @Override
        public void onButtonOntvangst() {
          Page4Rijbewijs.this.getNavigation().addPage(new Page14Rijbewijs(p0252f1));
        }

        @Override
        public void onButtonOngeldig() {
          Page4Rijbewijs.this.getNavigation().addPage(new Page15Rijbewijs(p0252f1, p0252f2));
        }
      };

      tabs.addTab(subPage1, "Basis");
      tabs.addTab(new Page4RijbewijsSubpage2(this, p0252f1), "Rijbewijsgegevens");
      Tab tab3 = tabs.addTab(new Page4RijbewijsSubpage4(this, p0252f1, p0252f2), "Categoriegegevens");

      if (isMaatregelen() && p0252f2 != null) {
        nl.procura.rdw.processen.p0252.f07.NATPRYBMAATR maatregelen = (nl.procura.rdw.processen.p0252.f07.NATPRYBMAATR) p0252f2
            .getResponse().getObject();
        tabs.addTab(new Page4RijbewijsSubpage3(this, p0252f2),
            "Maatregelen (" + maatregelen.getUitgmaatrtab().getUitgmaatrgeg().size() + ")");
      } else {
        tabs.addTab(new Page4RijbewijsSubpage3(this, null), "Maatregelen (0)");
      }

      addComponent(tabs);

      if (naSuccesRegistratie) {
        successMessage("De registratie is verstuurd");
        tabs.setSelectedTab(tab3);
      }
    }

    super.event(event);
  }

  public NATPERSOONGEG getPersoon() {
    NATPRYBMAATR a = (NATPRYBMAATR) p0252f1.getResponse().getObject();
    return a.getNatpersoontab().getNatpersoongeg().get(0);
  }

  private boolean isMaatregelen() {
    return isTru(getPersoon().getMaatregelind());
  }

  /**
   * Raadplegen persoonsgegevens op fiscaal nummer (bsn)
   */
  private P0252 stuur0252f2() {
    P0252 p0252Process = new P0252();
    p0252Process.newF2(astr(getPersoon().getFiscnrnatp()));

    if (sendMessage(p0252Process)) {
      return p0252Process;
    }

    return null;
  }
}
