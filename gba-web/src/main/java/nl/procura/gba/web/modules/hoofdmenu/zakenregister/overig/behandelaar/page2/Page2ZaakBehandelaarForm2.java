/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.behandelaar.page2;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.behandelaar.page2.Page2ZaakBehandelaarBean.*;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakBehandelaar;

public class Page2ZaakBehandelaarForm2 extends ReadOnlyForm<Page2ZaakBehandelaarBean> {

  public Page2ZaakBehandelaarForm2(ZaakBehandelaar zaakBehandelaar) {
    setOrder(DATUM_TIJD, BEHANDELAAR_LABEL, TOEWEZEN_DOOR_LABEL, OPMERKING);
    setColumnWidths(WIDTH_130, "");
    setReadonlyAsText(false);

    Page2ZaakBehandelaarBean bean = new Page2ZaakBehandelaarBean();
    bean.setDatumTijd(zaakBehandelaar.getDatumTijdInvoer());
    bean.setBehandelaarLabel(zaakBehandelaar.getBehandelaar());
    bean.setToegewezenDoorLabel(zaakBehandelaar.getGebruiker());
    bean.setOpmerking(zaakBehandelaar.getOpm());
    setBean(bean);
  }
}
