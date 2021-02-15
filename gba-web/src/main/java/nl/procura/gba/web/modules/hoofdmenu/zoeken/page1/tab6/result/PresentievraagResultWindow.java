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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.result;

import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.hoofdmenu.pv.page1.Page1Pv;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakregisterNavigator;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.results.PresentatievraagResultsLayout;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.results.PresentievraagResultsWindow;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagMatch;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.functies.VaadinUtils;

public class PresentievraagResultWindow extends GbaModalWindow {

  public PresentievraagResultWindow(Presentievraag presentievraag) {
    super("Presentievraag", "1000px");
    addComponent(new MainModuleContainer(false, new ResultPage(presentievraag)));
  }

  private static final class ResultPage extends NormalPageTemplate {

    private Button         buttonZaak = new Button("Toon zaak");
    private Presentievraag presentievraag;

    private ResultPage(Presentievraag presentievraag) {
      super("Presentievraag - resultaat");
      this.presentievraag = presentievraag;
      addComponent(new PresentatievraagResultsLayout(presentievraag) {

        @Override
        protected void navigateToResult(Presentievraag presentievraag1, PresentievraagMatch match) {
          getParentWindow().addWindow(new PresentievraagResultsWindow(presentievraag1, match));
        }
      });
    }

    @Override
    protected void initPage() {
      addGotoCaseButton();
      buttonClose.addListener(this);
      getMainbuttons().addComponent(buttonClose);
      super.initPage();
    }

    @Override
    public void onClose() {
      super.onClose();
      getWindow().closeWindow();
    }

    /**
     * Is the window is called from the presence question module then add the show case button
     */
    private void addGotoCaseButton() {
      Page1Pv page1pv = VaadinUtils.getChild(getApplication().getParentWindow(), Page1Pv.class);
      if (page1pv != null && fil(presentievraag.getZaakId())) {
        buttonZaak.addListener((Button.ClickListener) event -> {
          ZaakregisterNavigator.navigatoTo(getZaak(presentievraag), page1pv, false);
          getWindow().closeWindow();
        });
        getMainbuttons().addComponent(buttonZaak);
      }
    }

    private Zaak getZaak(Presentievraag p) {
      ZaakArgumenten z = new ZaakArgumenten(p.getZaakId());
      return getServices().getZakenService().getMinimaleZaken(z).stream().findFirst().orElse(null);
    }
  }
}
