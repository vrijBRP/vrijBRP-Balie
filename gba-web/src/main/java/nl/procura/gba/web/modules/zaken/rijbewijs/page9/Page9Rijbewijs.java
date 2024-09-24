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

package nl.procura.gba.web.modules.zaken.rijbewijs.page9;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.WEBSERVICE;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.isTru;

import com.vaadin.ui.Button;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.gba.web.modules.zaken.rijbewijs.RijbewijsPage;
import nl.procura.gba.web.modules.zaken.rijbewijs.page1.Page1Rijbewijs;
import nl.procura.gba.web.modules.zaken.rijbewijs.page7.Page7Rijbewijs;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagStatus;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsStatusType;
import nl.procura.rdw.messages.P1653;
import nl.procura.rdw.messages.P1654;
import nl.procura.rdw.messages.P1656;
import nl.procura.rdw.messages.P1658;
import nl.procura.rdw.processen.p1656.f02.AANVRRYBKGEG;
import nl.procura.rdw.processen.p1656.f02.STATRYBKGEG;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Aanvraagresultaatscherm
 */

public class Page9Rijbewijs extends RijbewijsPage {

  private final Button            buttonAccorderen = new Button("Accorderen");
  private final Button            buttonAnnuleren  = new Button("Annuleren");
  private final RijbewijsAanvraag aanvraag;
  private Page9RijbewijsForm1     form1            = null;

  public Page9Rijbewijs(RijbewijsAanvraag aanvraag) {

    super("Accorderen of Annuleren");

    this.aanvraag = aanvraag;

    setMargin(true);

    addButton(buttonPrev);
    addButton(buttonAccorderen);
    addButton(buttonAnnuleren);
    addButton(buttonNext);

    buttonNext.setCaption("Proces voltooien (F2)");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setInfo("Accorderen of annuleren van de registratie",
          "Als de aanvrager het aanvraagformulier heeft ondertekend en de leges heeft betaald dan kan de aanvraag worden geaccordeerd. "
              + "Is dit niet het geval dan kan de aanvraag worden geannuleerd.");

      form1 = new Page9RijbewijsForm1(aanvraag);
      addComponent(form1);

      getServices().getKassaService().addToWinkelwagen(aanvraag);
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonAccorderen) {

      getWindow().addWindow(
          new ConfirmDialog("Terug naar vorig scherm", "U wilt de aanvraag accorderen?", "400px") {

            @Override
            public void buttonYes() {

              try {

                setStatus(RijbewijsStatusType.GEACCORDEERD);
                closeWindow();
              } catch (Exception e) {
                throw new ProException(WEBSERVICE, WARNING, "Fout bij accorderen", e);
              }
            }
          });
    }

    if (button == buttonAnnuleren) {

      getWindow().addWindow(
          new ConfirmDialog("Terug naar vorig scherm", "U wilt de aanvraag annuleren?", "400px") {

            @Override
            public void buttonYes() {

              try {

                setStatus(RijbewijsStatusType.GEANNULEERD);
                closeWindow();
              } catch (Exception e) {
                throw new ProException(WEBSERVICE, WARNING, "Fout bij annuleren", e);
              }
            }

          });
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNextPage() {

    if (!aanvraag.getStatussen().getStatus().isStatus(RijbewijsStatusType.GEACCORDEERD,
        RijbewijsStatusType.GEANNULEERD)) {
      throw new ProException(WARNING, "Accordeer of annuleer eerst de aanvraag");
    }

    getApplication().getProcess().endProcess();

    getNavigation().goToPage(new Page1Rijbewijs());
    getNavigation().removeOtherPages();

    super.onNextPage();
  }

  @Override
  public void onPreviousPage() {

    switch (aanvraag.getStatussen().getStatus().getStatus()) {

      case GEACCORDEERD:
        throw new ProException(WARNING, "U kunt niet meer terug. De aanvraag is reeds geaccordeerd.");

      case GEANNULEERD:
        throw new ProException(WARNING, "U kunt niet meer terug. De aanvraag is reeds geannuleerd.");

      default:
        getWindow().addWindow(new ConfirmDialog("Terug naar vorig scherm",
            "Weet u zeker dat u terug wilt? <hr/>De aanvraag is nog niet geaccordeerd of geannuleerd.",
            "400px") {

          @Override
          public void buttonYes() {

            closeWindow();

            Page9Rijbewijs.this.toPreviousPage();
          }

        });
    }
  }

  /**
   * Definitief door naar vorig scherm
   */
  protected void toPreviousPage() {
    super.onPreviousPage();
  }

  private void setStatus(RijbewijsStatusType statusType) {

    form1.commit();

    boolean spoed = form1.getBean().isSpoed();

    Page7Rijbewijs rijbewijsPage = getPage(Page7Rijbewijs.class);

    long userId = getApplication().getServices().getGebruiker().getCUsr();
    P1653 p1653 = rijbewijsPage.getP1653();
    P1654 p1654 = rijbewijsPage.getP1654();
    P1658 p1658 = rijbewijsPage.getP1658();
    P1656 p1656 = new P1656();

    if (p1653 != null) { // Bij sprake van normaal aanvraag < 10

      p1656.newF1(statusType.getCode(), spoed, userId, p1653);
    } else if (p1654 != null) { // Bij sprake van omwisseling >= 10

      p1656.newF1(statusType.getCode(), spoed, userId, p1654);
    } else if (p1658 != null) {

      p1656.newF1(statusType.getCode(), spoed, userId, p1658);
    }

    if (sendMessage(p1656)) {

      nl.procura.rdw.processen.p1656.f02.AANVRRYBKRT p1656f2 = (nl.procura.rdw.processen.p1656.f02.AANVRRYBKRT) p1656
          .getResponse().getObject();

      AANVRRYBKGEG aanvr_geg = p1656f2.getAanvrrybkgeg();
      STATRYBKGEG stat_geg = p1656f2.getStatrybkgeg();

      long dIn = along(stat_geg.getStatdatrybk());
      long tIn = along(stat_geg.getStattydrybk());
      RijbewijsStatusType stat = RijbewijsStatusType.get(along(stat_geg.getStatcoderybk()));

      Page9RijbewijsBean1 b = new Page9RijbewijsBean1();

      b.setSpoed(isTru(aanvr_geg.getSpoedafhind()));
      b.setStatus(stat.toString());

      aanvraag.setSpoed(b.isSpoed());
      aanvraag.getStatussen().addStatus(
          new RijbewijsAanvraagStatus(dIn, tIn, stat, getApplication().getServices().getGebruiker()));

      // Opslaan
      getServices().getRijbewijsService().save(aanvraag);

      // Naar kassa sturen bij accordering
      if (stat == RijbewijsStatusType.GEACCORDEERD) {
        getServices().getKassaService().addToWinkelwagen(aanvraag);
      }

      form1.setBean(b);
    }
  }
}
