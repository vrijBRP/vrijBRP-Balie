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

package nl.procura.gba.web.modules.hoofdmenu.gv.page2;

import static nl.procura.gba.web.modules.hoofdmenu.gv.overzicht.GvOverzichtLayoutBean.*;
import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.modules.hoofdmenu.gv.overzicht.GvOverzichtLayoutBean;
import nl.procura.gba.web.services.zaken.gv.GvAanvraag;

public class Page2GvForm3 extends ReadOnlyForm {

  public Page2GvForm3(GvAanvraag aanvraag) {

    setCaption("Adressering");
    setColumnWidths(WIDTH_130, "");
    setOrder(INFORMATIEVRAGER, TAV, ADRES_AANVRAGER, PC, EMAIL, KENMERK);

    GvOverzichtLayoutBean b = new GvOverzichtLayoutBean();
    b.setInformatievrager(aanvraag.getAanvrager());
    b.setTav(aanvraag.getTav());
    b.setAdresAanvrager(aanvraag.getAdres());
    b.setPc(astr(aanvraag.getPostcode() + " " + aanvraag.getPlaats()));
    b.setEmail(aanvraag.getEmail());
    b.setKenmerk(aanvraag.getKenmerk());

    setBean(b);
  }
}
