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

package nl.procura.gba.web.modules.zaken.curatele.page3;

import static nl.procura.gba.web.modules.zaken.curatele.page3.Page3CurateleBean.*;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.rechtspraak.namespaces.ccr.CCRWS.Curandus.Beschikkingen.Beschikking;

public class Page3CurateleForm extends GbaForm<Page3CurateleBean> {

  public Page3CurateleForm(Beschikking beschikking) {

    setColumnWidths("100px", "");
    setOrder(INSTANTIE, REKEST, BESCHIKKINGSOORT, DATUM, NAAM, ADRES, GEMEENTE);
    setBean(new Page3CurateleBean(beschikking));
  }
}
