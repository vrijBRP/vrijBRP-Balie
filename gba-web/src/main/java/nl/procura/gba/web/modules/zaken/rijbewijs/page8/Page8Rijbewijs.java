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

package nl.procura.gba.web.modules.zaken.rijbewijs.page8;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.gba.web.modules.zaken.common.ZakenPrintPage;
import nl.procura.gba.web.modules.zaken.rijbewijs.page9.Page9Rijbewijs;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagAntwoord;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsStatusType;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Afdrukken aanvraag
 */
public class Page8Rijbewijs extends ZakenPrintPage {

  private static final String VERGEET_NIET = "Vergeet niet, na het afdrukken, de aanvraag nog te accorderen of annuleren.";

  private final RijbewijsAanvraag aanvraag;

  public Page8Rijbewijs(RijbewijsAanvraagAntwoord documentAanvraag, RijbewijsAanvraag aanvraag) {
    super("Rijbewijsaanvraag: afdrukken", documentAanvraag, aanvraag, DocumentType.RIJBEWIJS_AANVRAAG);
    this.aanvraag = aanvraag;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      InfoLayout il = getComponent(InfoLayout.class);

      // Extra waarschuwing nog wel de aanvraag te accorderen / annuleren.

      if (aanvraag.getStatussen().getStatus().getStatus().getCode() < RijbewijsStatusType.GEANNULEERD.getCode()) {

        il.setMessage(il.getMessage() + "<hr/>" + setClass("red", VERGEET_NIET));

        getComponent(InfoLayout.class).attach();
      }
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    super.handleEvent(button, keyCode);

    if (button == getPrintLayout().buttonPrint || keyCode == KeyCode.F3) {
      throw new ProException(INFO, VERGEET_NIET);
    }
  }

  @Override
  public void onNextPage() {

    getNavigation().goToPage(new Page9Rijbewijs(aanvraag));

    super.onNextPage();
  }

  @Override
  public void onPreviousPage() {

    switch (aanvraag.getStatussen().getStatus().getStatus()) {

      case GEACCORDEERD:
      case GEANNULEERD:
        toPreviousPage();
        break;

      default:
        getWindow().addWindow(new ConfirmDialog("Terug naar vorig scherm",
            "Weet u zeker dat u terug wilt? <hr/>De aanvraag is nog niet geaccordeerd of geannuleerd.",
            "400px") {

          @Override
          public void buttonYes() {

            try {

              closeWindow();
              Page8Rijbewijs.this.toPreviousPage();
            } catch (Exception e) {
              e.printStackTrace();
            }
          }

        });
    }
  }

  @Override
  public void setButtons() {

    addButton(buttonPrev);
    addButton(getPrintButtons());
    addButton(buttonNext);

    super.setButtons();
  }

  @Override
  public InfoLayout setInfo(String header, String msg) {

    msg += "<br/>Als de aanvrager het aanvraagformulier heeft ondertekend en de leges heeft betaald "
        + "dan kan de aanvraag worden geaccordeerd. Is dit niet het geval dan kan de aanvraag worden geannuleerd.";

    return super.setInfo(header, msg);
  }

  /**
   * Definitief door naar vorig scherm
   */
  private void toPreviousPage() {
    super.onPreviousPage();
  }
}
