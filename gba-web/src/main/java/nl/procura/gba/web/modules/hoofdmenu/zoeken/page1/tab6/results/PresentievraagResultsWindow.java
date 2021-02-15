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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.results;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.result.PresentatievraagResultLayout;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagMatch;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public class PresentievraagResultsWindow extends GbaModalWindow {

  public PresentievraagResultsWindow(Presentievraag presentievraag, PresentievraagMatch match) {
    super("Presentievraag", "950px");
    Page page = new Page(presentievraag, match, "Presentievraag - gevonden persoon");
    addComponent(new MainModuleContainer(false, page));
  }

  private static final class Page extends NormalPageTemplate {

    private Page(Presentievraag presentievraag, PresentievraagMatch match, String title) {
      super(title);
      buttonClose.addListener(this);
      getMainbuttons().addComponent(buttonClose);
      addComponent(new PresentatievraagResultLayout(presentievraag, match));
    }

    @Override
    public void onClose() {
      super.onClose();
      getWindow().closeWindow();
    }
  }
}
