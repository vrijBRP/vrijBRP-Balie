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

import static java.util.Arrays.asList;

import com.vaadin.ui.Button;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.hoofdmenu.pv.page3.Page3Pv;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.ZoekTabPage;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.result.Tab6ResultPage;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagMatch;
import nl.procura.gba.web.theme.GbaWebTheme;

public class Tab6ResultsPage extends ZoekTabPage {

  private final Presentievraag                presentievraag;
  private final PresentatievraagResultsLayout presentatievraagResultsLayout;
  private final Button                        buttonPrint = new Button("Afdrukken");

  public Tab6ResultsPage(Presentievraag presentievraag, boolean nieuweAanvraag) {

    super("Resultaat presentievraag");
    this.presentievraag = presentievraag;
    addButton(buttonPrev);

    if (nieuweAanvraag) {
      buttonSave.setStyleName(GbaWebTheme.BUTTON_DEFAULT);
      buttonSave.focus();
      addButton(buttonSave);
    }

    buttonPrint.setEnabled(false);
    addButton(buttonPrint);

    presentatievraagResultsLayout = new PresentatievraagResultsLayout(presentievraag) {

      @Override
      protected void navigateToResult(Presentievraag presentievraag, PresentievraagMatch match) {
        getNavigation().addPage(new Tab6ResultPage(presentievraag, match));
      }
    };

    addExpandComponent(presentatievraagResultsLayout);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonPrint) {
      getNavigation().goToPage(new Page3Pv(asList(presentievraag)));
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onSave() {

    if (buttonSave.getParent() != null) {
      PresentievraagToelichtingWindow commentaarWindow = new PresentievraagToelichtingWindow("") {

        @Override
        public void onSave(String comment) {
          presentievraag.setToelichting(comment);
          presentatievraagResultsLayout.update();
          GbaApplication.getInstance().getServices().getPresentievraagService().save(presentievraag);
          successMessage("De presentievraag is opgeslagen");
          buttonSave.setEnabled(false);
          buttonPrint.setEnabled(true);
        }
      };

      getParentWindow().addWindow(commentaarWindow);
    }

    super.onNextPage();
  }
}
