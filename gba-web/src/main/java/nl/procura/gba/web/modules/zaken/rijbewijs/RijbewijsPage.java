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

package nl.procura.gba.web.modules.zaken.rijbewijs;

import static nl.procura.standard.Globalfunctions.isTru;

import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.modules.zaken.rijbewijs.errorpage.RdwInfoLayout;
import nl.procura.gba.web.modules.zaken.rijbewijs.errorpage.RijbewijsErrorWindow;
import nl.procura.gba.web.modules.zaken.rijbewijs.page11.Page11Rijbewijs;
import nl.procura.gba.web.modules.zaken.rijbewijs.page5.Page5Rijbewijs;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.rijbewijs.NrdServices;
import nl.procura.rdw.functions.*;
import nl.procura.rdw.messages.P1651;
import nl.procura.vaadin.component.dialog.ConfirmDialog;

public class RijbewijsPage extends ZakenPage {

  private boolean askToReturn = false;

  public RijbewijsPage(String title) {

    super(title);
  }

  @Override
  public void onPreviousPage() {

    if (askToReturn) {
      if (getNavigation().getPages().size() > 1) {
        getWindow().addWindow(new ConfirmDialog("Terug naar vorig scherm",
            "Weet u zeker dat u terug wilt? <hr/>De aanvraag is reeds verzonden en kan niet worden gewijzigd.",
            "430px") {

          @Override
          public void buttonYes() {
            try {
              closeWindow();
              toPreviousPage();
            } catch (Exception e) {
              e.printStackTrace();
            }
          }

        });
      }
    } else {
      toPreviousPage();
    }
  }

  /**
   * Stuur bericht naar RDW en toon foutmelding indien van toepassing
   */
  public boolean sendMessage(RdwMessage message) {

    if (NrdServices.sendMessage(getServices().getRijbewijsService(), message)) {
      return true;
    }

    getParentWindow().addWindow(new RijbewijsErrorWindow(message.getResponse().getMelding()));
    return false;
  }

  /**
   * Voordat er naar het vorige scherm wordt teruggegaan. Eerst vragen
   */
  protected void setAskToReturn(boolean askToReturn) {
    this.askToReturn = askToReturn;
  }

  /**
   * Ga naar het vergelijkscherm of door naar aanvraagscherm
   */
  protected void goTo(P1651 p1651) {

    if (p1651.getResponse().isFoutMelding()) {

      if (p1651.getResponse().getMelding().getNr() == RdwMeldingNr.GEEN_PERSONEN.code) {

        getNavigation().goToPage(new Page5Rijbewijs(p1651));
      }
    } else {
      boolean isVergelijken = isTru(getApplication().getParmValue(ParameterConstant.RYB_VERGELIJKSCHERM));

      if (isVergelijken) {

        getNavigation().goToPage(new Page11Rijbewijs(p1651));

      } else {

        getNavigation().goToPage(new Page5Rijbewijs(p1651));
      }
    }
  }

  /**
   * Toont een rdw melding. Dit is geen showstopper voor de rest van het proces, maar wel iets
   * dat de gebruiker moet weten
   */
  protected void setRdwInfo(RdwMessage m) {
    RdwProces rdwProces = RdwProces.get(m.getResponse());
    if (rdwProces != null && RdwProcesType.OUT == rdwProces.t) {
      ProcesMelding meld = m.getResponse().getMelding();
      if (m.getResponse().isMelding()) {
        addComponent(new RdwInfoLayout("Informatie van RDW: " + meld.getNr() + " - " + meld.getMeldingKort()));
      }
    }
  }

  private void toPreviousPage() {
    super.onPreviousPage();
  }
}
