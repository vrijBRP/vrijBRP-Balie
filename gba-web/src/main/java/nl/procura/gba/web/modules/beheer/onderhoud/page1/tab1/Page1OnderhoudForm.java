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

package nl.procura.gba.web.modules.beheer.onderhoud.page1.tab1;

import static nl.procura.gba.web.modules.beheer.onderhoud.page1.tab1.Page1OnderhoudBean.ANR;
import static nl.procura.gba.web.modules.beheer.onderhoud.page1.tab1.Page1OnderhoudBean.BSN;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.applicatie.onderhoud.OnderhoudService.IdVoorraad;

public class Page1OnderhoudForm extends ReadOnlyForm<Page1OnderhoudBean> {

  public Page1OnderhoudForm(IdVoorraad idVoorraad) {
    setColumnWidths("80px", "");
    setOrder(ANR, BSN);
    Page1OnderhoudBean bean = new Page1OnderhoudBean();
    bean.setAnr(idVoorraad.getAnr());
    bean.setBsn(idVoorraad.getBsn());
    setBean(bean);
  }
}
