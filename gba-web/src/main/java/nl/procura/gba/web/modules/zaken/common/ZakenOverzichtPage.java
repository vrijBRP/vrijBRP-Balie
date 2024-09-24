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

package nl.procura.gba.web.modules.zaken.common;

import static nl.procura.gba.common.ZaakStatusType.WACHTKAMER;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;

import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.verwerken.BsmVerwerkingWindow;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.status.ZaakStatusUpdater;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.layout.tabsheet.LazyTabChangeListener;
import nl.procura.vaadin.component.layout.tabsheet.LazyTabsheet.LazyTab;

public class ZakenOverzichtPage<T extends Zaak> extends ZakenPage<T> {

  protected final ZaakAanpassenButton    buttonAanpassen = new ZaakAanpassenButton();
  protected final ZaakRequestInboxButton buttonVerzoek   = new ZaakRequestInboxButton();
  protected final Button                 buttonDoc       = new Button("Document afdrukken");
  protected final Button                 buttonFiat      = new Button("Fiatteren");
  protected final Button                 buttonVerwerken = new Button("Nu verwerken");

  private ZaakTabsheet<T> tabsheet = null;

  public ZakenOverzichtPage(T zaak, String title) {
    super(zaak, title);
    addButton(buttonPrev);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      // Vraag de volledige zaak op
      setZaak(Services.getInstance().getZakenService().getVolledigeZaak(getZaak()));

      buttonVerzoek.setZaak(getZaak());

      tabsheet = new ZaakTabsheet<T>(this, getZaak()) {

        @Override
        protected void addOptieButtons() {
          ZakenOverzichtPage.this.addOptieButtons();
        }

        @Override
        protected void addTabs() {
          ZakenOverzichtPage.this.addTabs();
          ZakenOverzichtPage.this.addTabs(tabsheet);
        }
      };

      tabsheet.addListener((SelectedTabChangeListener) e -> {
        onSelectedTabChange(tabsheet.getLazyTab(e));
      });

      addComponent(tabsheet);

    } else if (event.isEvent(AfterReturn.class)) {
      tabsheet.reloadTabs();
    }

    super.event(event);
  }

  public ZaakTabsheet<T> getTabsheet() {
    return tabsheet;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonAanpassen) {
      buttonAanpassen.onClick(getZaak(), this::goToZaak);

    } else if (button == buttonVerzoek) {
      buttonVerzoek.onClick();

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
      doVerwerken();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPage(getNavigation().getPreviousPage());
  }

  protected void addOptieButton(Button button) {
    tabsheet.getAlgemeneLayout().getRight().addButton(button, this);
  }

  protected void addOptieButtons() {
  }

  protected Tab addTab(Component c, String h) {
    return tabsheet.addTab(c, h, null);
  }

  protected Tab addTab(Component c, String h, LazyTabChangeListener l) {
    return tabsheet.addTab(c, h, null, l);
  }

  protected void addTabs() {
  }

  @SuppressWarnings("unused")
  protected void addTabs(ZaakTabsheet<T> tabsheet) {
  }

  private void doFiat() {
    new ZaakStatusUpdater(getWindow(), getZaak(), "Gefiatteerd.", ZaakStatusType.OPGENOMEN) {

      @Override
      protected void reload() {
        tabsheet.reloadTabs();
      }
    };
  }

  private void doVerwerken() {
    getWindow().addWindow(new BsmVerwerkingWindow<T>(getZaak()) {

      @Override
      public void reload() {
        tabsheet.reloadTabs();
      }
    });
  }

  protected void goToDocument() {
  }

  protected void goToPersoon(String fragment, FieldValue... fieldValues) {

    for (FieldValue fieldValue : fieldValues) {

      if (fieldValue != null && pos(fieldValue.getValue())) {

        getApplication().goToPl(getWindow(), fragment, PLEDatasource.STANDAARD, astr(fieldValue.getValue()));

        return;
      }
    }

    throw new ProException(INFO, "Geen a-nummer of burgerservicenummer gevonden");
  }

  protected void goToZaak() {
  }

  // Override please
  @SuppressWarnings("unused")
  protected void onSelectedTabChange(LazyTab lazyTab) {
  }
}
