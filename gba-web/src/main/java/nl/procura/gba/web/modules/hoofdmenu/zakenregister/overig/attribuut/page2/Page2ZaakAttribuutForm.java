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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.attribuut.page2;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.attribuut.page2.Page2ZaakAttribuutBean.ATTRIBUUT;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.attribuut.page2.Page2ZaakAttribuutBean.TYPE;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.attribuut.page2.Page2ZaakAttribuutBean.WAARDE;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuut;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType;

public class Page2ZaakAttribuutForm extends GbaForm<Page2ZaakAttribuutBean> {

  public Page2ZaakAttribuutForm(ZaakAttribuut attribuut) {

    setOrder(TYPE, ATTRIBUUT, WAARDE);
    setColumnWidths("100px", "");

    Page2ZaakAttribuutBean bean = new Page2ZaakAttribuutBean();
    bean.setType(ZaakAttribuutType.get(attribuut.getAttribuut()));
    bean.setAttribuut(attribuut.getAttribuut());
    bean.setWaarde(attribuut.getWaarde());
    setBean(bean);
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();

    getField(TYPE).addListener((ValueChangeListener) event -> {
      if (event.getProperty().getValue() == ZaakAttribuutType.ANDERS) {
        getField(ATTRIBUUT).setVisible(true);
      } else {
        getField(ATTRIBUUT).setVisible(false);
        getField(ATTRIBUUT).setValue("");
      }

      repaint();
    });

    getField(ATTRIBUUT).setReadOnly(fil(getBean().getAttribuut()));
  }
}
