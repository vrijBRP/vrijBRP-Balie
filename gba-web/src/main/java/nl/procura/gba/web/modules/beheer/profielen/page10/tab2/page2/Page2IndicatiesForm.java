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

package nl.procura.gba.web.modules.beheer.profielen.page10.tab2.page2;

import static nl.procura.gba.web.modules.beheer.profielen.page10.tab2.page2.Page2IndicatiesBean.*;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningIndicatie;

public class Page2IndicatiesForm extends GbaForm<Page2IndicatiesBean> {

  public Page2IndicatiesForm(PlAantekeningIndicatie indicatie) {

    setCaption("Indicatie");

    setOrder(INDICATIE, PROBEV, BUTTON, OMS);

    setColumnWidths(WIDTH_130, "");

    setIndicatie(indicatie);
  }

  public void setIndicatie(PlAantekeningIndicatie indicatie) {

    Page2IndicatiesBean bean = new Page2IndicatiesBean();

    bean.setProbev(indicatie.getProbev());
    bean.setIndicatie(indicatie.getIndicatie());
    bean.setOms(indicatie.getOmschrijving());
    bean.setButton(indicatie.getButton());

    setBean(bean);
  }
}
