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

package nl.procura.gba.web.modules.bs.erkenning.page25;

import static nl.procura.gba.web.modules.bs.erkenning.page25.Page25ErkenningBean3.VERKLARING;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;

public class Page25ErkenningForm3 extends GbaForm<Page25ErkenningBean3> {

  public Page25ErkenningForm3(DossierErkenning erkenning) {
    setColumnWidths("160px", "");
    setOrder(VERKLARING);

    Page25ErkenningBean3 bean = new Page25ErkenningBean3();
    bean.setVerklaring(erkenning.isVerklaringGezag());
    setBean(bean);
  }
}
