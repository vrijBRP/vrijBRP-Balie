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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet.Tab;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.verwerken.BsmVerwerkingWindow;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.status.ZaakStatusUpdater;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.windows.home.navigatie.ZakenregisterAccordionTab;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.layout.tabsheet.LazyTabsheet.LazyTab;
import nl.procura.vaadin.functies.VaadinUtils;

public class ZakenregisterOptiePage<T extends Zaak> extends ZakenregisterPage<T> {

  private ZaakTabsheet<T> tabsheet = null;

  public ZakenregisterOptiePage(T zaak, String title) {
    super(zaak, title);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      tabsheet = new ZaakTabsheet<T>(this, getZaak()) {

        @Override
        protected void addOptieButtons() {
          ZakenregisterOptiePage.this.addOptieButtons();
        }

        @Override
        protected void addTabs() {
          ZakenregisterOptiePage.this.addTabs();
          ZakenregisterOptiePage.this.addTabs(tabsheet);
        }

        @Override
        protected void reloadTree() {
          VaadinUtils.getChild(getWindow(), ZakenregisterAccordionTab.class).recountTree();
        }
      };

      addComponent(tabsheet);
      LazyTab lazyTab = tabsheet.getLazyTab(getSelectedTab());
      if (lazyTab != null) {
        tabsheet.setSelectedTab(lazyTab.getLayout());
      }
    } else if (event.isEvent(AfterReturn.class)) {
      tabsheet.reloadTabs();
    }

    super.event(event);
  }

  protected void addOptieButton(Button button) {
    tabsheet.getAlgemeneLayout().getRight().addButton(button, this);
  }

  protected void addOptieButtons() {
  }

  protected Tab addTab(Component c, String h) {
    return tabsheet.addTab(c, h, null);
  }

  protected void addTabs() {
  }

  @SuppressWarnings("unused")
  protected void addTabs(ZaakTabsheet<T> tabsheet) {
  }

  @Override
  protected void doFiat() {

    new ZaakStatusUpdater(getWindow(), getZaak(), "Gefiatteerd.", ZaakStatusType.OPGENOMEN) {

      @Override
      protected void reload() {
        tabsheet.reloadTabs();
        tabsheet.reloadTree();
      }
    };
  }

  @Override
  protected void doVerwerken() {

    BsmVerwerkingWindow<T> window = new BsmVerwerkingWindow<T>(getZaak()) {

      @Override
      public void reload() {
        tabsheet.reloadTabs();
        tabsheet.reloadTree();
      }
    };
    getWindow().addWindow(window);
  }

  protected Component getSelectedTab() {
    return null;
  }

  protected void reloadTabs() {
    tabsheet.reloadTabs();
  }
}
