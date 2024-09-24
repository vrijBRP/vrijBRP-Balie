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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.common.ZaakStatusType.OPGENOMEN;
import static nl.procura.gba.common.ZaakStatusType.WACHTKAMER;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.ZakenregisterUtils.getZakenregisterAccordionTab;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.hoofdmenu.klapper.windows.KlapperInzageWindow;
import nl.procura.gba.web.modules.hoofdmenu.klapper.windows.KlapperOverzichtWindow;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.verwerken.BsmVerwerkingWindow;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.status.ZaakStatusUpdater;
import nl.procura.gba.web.modules.zaken.common.ZaakAanpassenButton;
import nl.procura.gba.web.modules.zaken.common.ZaakInwonerAppButton;
import nl.procura.gba.web.modules.zaken.common.ZaakRequestInboxButton;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.windows.home.HomeWindow;
import nl.procura.gba.web.windows.home.navigatie.ZakenregisterAccordionTab;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.layout.tabsheet.LazyTabsheet.LazyTab;

public class ZakenregisterOptiePage<T extends Zaak> extends ZakenregisterPage<T> {

  protected final ZaakAanpassenButton    buttonAanpassen   = new ZaakAanpassenButton();
  protected final ZaakRequestInboxButton buttonVerzoek     = new ZaakRequestInboxButton();
  protected final ZaakInwonerAppButton   buttonInwonerSign = new ZaakInwonerAppButton();
  protected final ZaakPersonenButton     buttonPersonen    = new ZaakPersonenButton();
  protected final Button                 buttonDoc         = new Button("Document afdrukken");
  protected final Button                 buttonFiat        = new Button("Fiatteren");
  protected final Button                 buttonVerwerken   = new Button("Nu verwerken");
  protected final Button                 buttonKlappers    = new Button("Klappers");

  private ZaakTabsheet<T> tabsheet = null;

  public ZakenregisterOptiePage(T zaak, String title) {
    super(zaak, title);
    buttonVerzoek.setZaak(zaak);
    buttonInwonerSign.setZaak(zaak);
    buttonInwonerSign.addCloseListener(this::reloadTabs);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      // Als er geen vorige pagina is dan de knop verwijderen
      if (getNavigation().getPreviousPage() == null) {
        getButtonLayout().removeComponent(buttonPrev);
      }
      // Als het een modal window is dan de standaard knoppen verwijderen
      if (getWindow() instanceof GbaModalWindow) {
        removeComponent(getMainbuttons());
        removeComponent(getButtonLayout());
      }

      tabsheet = new ZaakTabsheet<T>(this, getZaak()) {

        @Override
        protected void addOptieButtons() {
          ZakenregisterOptiePage.this.addOptieButtons();
          if (getServices().getInwonerAppService().supportZaak(getZaak())) {
            addOptieButton(buttonInwonerSign);
          }
        }

        @Override
        protected void addTabs() {
          ZakenregisterOptiePage.this.addTabs();
          ZakenregisterOptiePage.this.addTabs(tabsheet);
        }

        @Override
        protected void reloadTree() {
          getZakenregisterAccordionTab(getParentWindow()).ifPresent(ZakenregisterAccordionTab::recountTree);
        }
      };

      addComponent(tabsheet);

      // Refresh specific tab
      tabsheet.addListener((SelectedTabChangeListener) tabChangeEvent -> {
        Component selectedTab = tabChangeEvent.getTabSheet().getSelectedTab();
        for (LazyTab lazytab : tabsheet.getLazyTabs()) {
          if (lazytab.getLayout() == selectedTab) {
            if (lazytab.getComponent() instanceof Component) {
              tabsheet.refreshTab((Component) lazytab.getComponent());
            }
          }
        }
      });

      LazyTab lazyTab = tabsheet.getLazyTab(getSelectedTab());
      if (lazyTab != null) {
        tabsheet.setSelectedTab(lazyTab.getLayout());
      }
    } else if (event.isEvent(AfterReturn.class)) {
      tabsheet.reloadTabs();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonAanpassen) {
      buttonAanpassen.onClick(getZaak(), this::goToZaak);

    } else if (button == buttonVerzoek) {
      buttonVerzoek.onClick();

    } else if (button == buttonInwonerSign) {
      buttonInwonerSign.onClick();

    } else if (button == buttonPersonen) {
      getParentWindow().addWindow(new ZaakPersonenMultiWindow(getZoekpersoonTypes()) {

        @Override
        public void onSelectPersoon(ZaakPersoonType type) {
          goToPersoon(type);
        }
      });
    } else if (button == buttonDoc) {
      goToDocument();

    } else if (button == buttonFiat) {
      ZaakStatusUpdater.checkUpdatenStatusMogelijk(getApplication(), getZaak());

      if (getZaak().getStatus() != WACHTKAMER) {
        throw new ProException(ProExceptionSeverity.INFO,
            "Alleen zaken met status 'Wachtkamer' kunnen worden gefiatteerd");
      }

      doFiat();
    } else if (button == buttonVerwerken) {
      if (getZaak().getStatus() != OPGENOMEN) {
        throw new ProException(ProExceptionSeverity.INFO,
            "Alleen zaken met status 'Opgenomen' kunnen worden verwerkt");
      }

      doVerwerken();
    } else if (button == buttonKlappers) {
      goToKlappers();
    }

    super.handleEvent(button, keyCode);
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

  protected void goToZaak(String fragment) {
    getApplication().getServices().getMemoryService().setObject(Dossier.class, getZaak());
    getApplication().openWindow(getParentWindow(), new HomeWindow(), fragment);
    getApplication().closeAllModalWindows(getApplication().getWindows());
  }

  protected void doFiat() {
    new ZaakStatusUpdater(getParentWindow(), getZaak(), "Gefiatteerd.", ZaakStatusType.OPGENOMEN) {

      @Override
      protected void reload() {
        tabsheet.reloadTabs();
        tabsheet.reloadTree();
      }
    };
  }

  protected void doVerwerken() {
    BsmVerwerkingWindow<T> window = new BsmVerwerkingWindow<T>(getZaak()) {

      @Override
      public void reload() {
        tabsheet.reloadTabs();
        tabsheet.reloadTree();
      }
    };
    getParentWindow().addWindow(window);
  }

  protected void goToDocument() {
  }

  protected ZaakPersoonType[] getZoekpersoonTypes() {
    return null;
  }

  protected void goToPersoon(String fragment, FieldValue... fieldValues) {
    for (FieldValue fieldValue : fieldValues) {
      if (fieldValue != null && pos(fieldValue.getValue())) {
        getApplication().goToPl(getParentWindow(), fragment, PLEDatasource.STANDAARD, fieldValue.getStringValue());
        return;
      }
    }

    throw new ProException(INFO, "Geen a-nummer of burgerservicenummer gevonden");
  }

  @SuppressWarnings("unused")
  protected void goToPersoon(ZaakPersoonType type) {
  }

  protected void goToZaak() {
  }

  protected Component getSelectedTab() {
    return null;
  }

  protected void reloadTabs() {
    tabsheet.reloadTabs();
  }

  private void goToKlappers() {
    if (getZaak() instanceof Dossier) {
      Dossier dossier = to(getZaak(), Dossier.class);
      List<DossierAkte> klappers = getServices().getAkteService().getAktes(dossier);

      if (klappers.size() == 1) { // Direct naar inzage scherm
        getParentWindow().addWindow(new KlapperInzageWindow(klappers.get(0)));
      } else {
        getParentWindow().addWindow(new KlapperOverzichtWindow(klappers));
      }
    }
  }
}
