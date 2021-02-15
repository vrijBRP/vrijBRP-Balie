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

package nl.procura.gba.web.modules.zaken.gpk.page2;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionType.CONFIG;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.modules.zaken.gpk.page3.Page3Gpk;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.gpk.GpkAanvraag;
import nl.procura.standard.ProcuraDate;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Nieuwe aanvraag
 */

public class Page2Gpk extends ZakenPage {

  private Page2GpkForm form = null;

  public Page2Gpk() {

    super("Gehandicapten parkeerkaart: nieuw");

    addButton(buttonPrev);
    addButton(buttonNext);

    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event instanceof InitPage) {

      Page2GpkBean bean = new Page2GpkBean();
      bean.setVervalDatum(new ProcuraDate().addYears(5).getDateFormat());

      String afgever = astr(getApplication().getParmValue(ParameterConstant.GPK_AFGEVER));
      bean.setAfgever(afgever);

      if (emp(afgever)) {
        throw new ProException(CONFIG, ERROR,
            "Vul bij de parameters in wie de afgever is van de parkeerkaarten.");
      }

      form = new Page2GpkForm(bean);

      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {

    form.commit();

    Page2GpkBean b = form.getBean();

    GpkAanvraag aanvraag = (GpkAanvraag) getServices().getGpkService().getNewZaak();

    AnrFieldValue anr = new AnrFieldValue(getPl().getPersoon().getAnr().getVal());
    BsnFieldValue bsn = new BsnFieldValue(getPl().getPersoon().getBsn().getVal());

    aanvraag.setAnummer(anr);
    aanvraag.setBurgerServiceNummer(bsn);
    aanvraag.setNr(b.getNummer());
    aanvraag.setKaart(b.getKaart());
    aanvraag.setDatumVerval(new DateTime(b.getVervalDatum()));
    aanvraag.setDatumPrint(new DateTime());
    aanvraag.setAfgever(b.getAfgever());
    aanvraag.setDatumTijdInvoer(new DateTime());

    super.onSave();

    getNavigation().goToPage(new Page3Gpk(aanvraag));
  }
}
