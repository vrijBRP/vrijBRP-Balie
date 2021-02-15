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

import static nl.procura.gba.web.modules.zaken.gpk.page2.Page2GpkBean.*;

import nl.procura.gba.web.components.layouts.form.GbaForm;

public class Page2GpkForm extends GbaForm<Page2GpkBean> {

  public Page2GpkForm(Page2GpkBean bean) {
    setCaption("Nieuwe gehandicapten parkeerkaart");
    setOrder(NUMMER, KAART, VERVALDATUM, AFGEVER);
    setColumnWidths(WIDTH_130, "");
    setBean(bean);
  }
}
