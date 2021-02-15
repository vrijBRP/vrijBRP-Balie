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

package nl.procura.gba.web.modules.zaken.curatele.page2;

import static nl.procura.gba.web.modules.zaken.curatele.page2.Page2CurateleBean.PERSOON;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.zaken.curatele.page1.CuratelePersoon;

public class Page2CurateleForm extends GbaForm<Page2CurateleBean> {

  public Page2CurateleForm(CuratelePersoon persoon) {
    setColumnWidths("100px", "");
    setOrder(PERSOON);

    Page2CurateleBean bean = new Page2CurateleBean();
    bean.setPersoon(persoon.getNaam().getNaamNaamgebruikEersteVoornaam());
    setBean(bean);
  }
}
