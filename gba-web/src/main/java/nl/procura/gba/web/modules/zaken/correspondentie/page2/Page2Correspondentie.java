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

package nl.procura.gba.web.modules.zaken.correspondentie.page2;

import static nl.procura.gba.web.modules.zaken.correspondentie.page2.Page2CorrespondentieBean.ANDERS;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.modules.zaken.correspondentie.page1.Page1Correspondentie;
import nl.procura.gba.web.services.zaken.correspondentie.CorrespondentieService;
import nl.procura.gba.web.services.zaken.correspondentie.CorrespondentieType;
import nl.procura.gba.web.services.zaken.correspondentie.CorrespondentieZaak;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Nieuwe aanvraag
 */
public class Page2Correspondentie extends ZakenPage {

  private Page2CorrespondentieForm form = null;

  public Page2Correspondentie() {

    super("Correspondentie: nieuw");

    addButton(buttonPrev);
    addButton(buttonNext);

    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event instanceof InitPage) {

      buttonNext.setCaption("Proces voltooien (F2)");

      Page2CorrespondentieBean bean = new Page2CorrespondentieBean();

      form = new Page2CorrespondentieForm(bean) {

        @Override
        protected void onchange(CorrespondentieType value) {

          if (CorrespondentieType.ANDERS == value) {
            getField(ANDERS).setVisible(true);
          } else {
            getField(ANDERS).setVisible(false);
            getField(ANDERS).setValue("");
            getBean().setAnders("");
          }

          repaint();
        }
      };

      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {

    form.commit();

    Page2CorrespondentieBean b = form.getBean();

    CorrespondentieService service = getServices().getCorrespondentieService();

    CorrespondentieZaak zaak = (CorrespondentieZaak) service.getNewZaak();

    AnrFieldValue anr = new AnrFieldValue(getPl().getPersoon().getAnr().getVal());
    BsnFieldValue bsn = new BsnFieldValue(getPl().getPersoon().getBsn().getVal());

    zaak.setAnummer(anr);
    zaak.setBurgerServiceNummer(bsn);
    zaak.setRoute(b.getRoute().getCode());
    zaak.setCorrespondentie(toBigDecimal(b.getType().getCode()));
    zaak.setToelichting(b.getToelichting());
    zaak.setAnders(b.getAnders());
    zaak.setDatumTijdInvoer(new DateTime());
    zaak.setAfsluitType(b.getAfsluitType());

    service.save(zaak);

    successMessage("De gegevens zijn opgeslagen.");

    getApplication().getProcess().endProcess();

    getNavigation().goToPage(new Page1Correspondentie());
    getNavigation().removeOtherPages();

    super.onSave();
  }
}
