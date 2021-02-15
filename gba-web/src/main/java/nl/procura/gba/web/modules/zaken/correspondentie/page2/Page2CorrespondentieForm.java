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

import static nl.procura.gba.web.modules.zaken.correspondentie.page2.Page2CorrespondentieBean.*;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.correspondentie.CorrespondentieType;

public abstract class Page2CorrespondentieForm extends GbaForm<Page2CorrespondentieBean> {

  public Page2CorrespondentieForm(Page2CorrespondentieBean bean) {
    setCaption("Nieuwe correspondentie");
    setReadThrough(true);
    setOrder(ROUTE, TYPE, AFSLUIT_TYPE, ANDERS, TOELICHTING);
    setColumnWidths("170px", "");
    setBean(bean);
  }

  @Override
  public void setBean(Object bean) {
    super.setBean(bean);
    getField(TYPE).addListener((ValueChangeListener) event -> {
      onchange((CorrespondentieType) event.getProperty().getValue());
    });
  }

  protected abstract void onchange(CorrespondentieType value);
}
