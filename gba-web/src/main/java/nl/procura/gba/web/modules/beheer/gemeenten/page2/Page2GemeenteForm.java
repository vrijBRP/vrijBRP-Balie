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

package nl.procura.gba.web.modules.beheer.gemeenten.page2;

import static nl.procura.gba.web.modules.beheer.gemeenten.page2.Page2GemeenteBean.*;
import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class Page2GemeenteForm extends GbaForm<Page2GemeenteBean> {

  public Page2GemeenteForm(Gemeente gemeente) {

    setOrder(CBSCODE, GEMEENTE, ADRES, PC, PLAATS);
    setColumnWidths(WIDTH_130, "");

    Page2GemeenteBean bean = new Page2GemeenteBean();
    bean.setCbscode(astr(gemeente.getCbscode()));
    bean.setGemeente(gemeente.getGemeente());
    bean.setAdres(gemeente.getAdres());
    bean.setPc(new FieldValue(gemeente.getPc()));
    bean.setPlaats(gemeente.getPlaats());

    setBean(bean);
  }
}
