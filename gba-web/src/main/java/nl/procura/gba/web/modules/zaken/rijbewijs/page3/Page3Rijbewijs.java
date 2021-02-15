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

package nl.procura.gba.web.modules.zaken.rijbewijs.page3;

import static nl.procura.standard.Globalfunctions.*;

import nl.procura.gba.web.modules.zaken.rijbewijs.RijbewijsPage;
import nl.procura.gba.web.modules.zaken.rijbewijs.page10.Page10Rijbewijs;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsService;
import nl.procura.rdw.functions.RdwMeldingNr;
import nl.procura.rdw.messages.P1651;
import nl.procura.rdw.messages.P1652;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

/**
 * Zoekargumentenscherm
 */
public class Page3Rijbewijs extends RijbewijsPage {

  private Page3RijbewijsForm1 form1 = null;
  private Page3RijbewijsForm2 form2 = null;

  public Page3Rijbewijs() {

    super("Rijbewijsaanvraag");

    setMargin(true);

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      if (isTru(getApplication().getParmValue(ParameterConstant.RYB_TEST))) {

        setInfo("Let op! Er worden testberichten gebruikt.").setIcon(ProcuraTheme.ICOON_24.WARNING);
      }

      form1 = new Page3RijbewijsForm1(getPl());
      form2 = new Page3RijbewijsForm2(getPl());

      addComponent(form1);
      addComponent(form2);
    }

    super.event(event);
  }

  public int getAantalAanvragen() {

    RijbewijsService d = getApplication().getServices().getRijbewijsService();

    return d.getZakenCount(new ZaakArgumenten(getPl()));
  }

  @Override
  public void onNextPage() {

    getApplication().getProcess().startProcess();

    form1.commit();

    stuur1651F1(); // Eerste aanvraag

    super.onNextPage();
  }

  /**
   * Zoek persoon op basis persoonsgegevens
   */
  private void stuur1651F1() {

    final P1651 p1651 = new P1651();

    String bsn = astr(form1.getBean().getBsn().getValue());
    String anr = astr(form1.getBean().getAnr().getValue());
    long dGeb = along(form1.getBean().getGeboortedatum().getValue());
    String gesl = form1.getBean().getGeslachtsnaam();
    String voorn = form1.getBean().getVoornamen();

    p1651.newF1(bsn, anr, dGeb, gesl, voorn);

    if (sendMessage(p1651)) {

      if (p1651.getResponse().getMelding().getNr() == RdwMeldingNr.GEEN_PERSONEN.code) {

        String msg = "Er zijn geen personen gevonden met dit zoekgegeven.<br/>Dit houdt in dat de persoon niet bekend is bij het RDW.<hr>"
            + "Wilt u toch doorgaan en een rijbewijs aanvragen.";

        getWindow().addWindow(new ConfirmDialog("Geen personen gevonden", msg, "400px") {

          @Override
          public void buttonYes() {

            goTo(p1651);

            super.buttonYes();
          }

        });
      } else if (p1651.getResponse().getMelding().getNr() == RdwMeldingNr.MEEDERE_PERSONEN.code) {

        stuur1652f1(p1651);
      } else {

        goTo(p1651);
      }
    }
  }

  /**
   * 1651 heeft melding 'meerdere personen gevonden' teruggegeven. Dus stuur 1652. Opvragen personen
   */
  private void stuur1652f1(P1651 p1651) {

    P1652 p1652 = new P1652();

    p1652.newF1(p1651);

    if (sendMessage(p1652)) {

      getNavigation().goToPage(new Page10Rijbewijs(p1652));
    }
  }
}
