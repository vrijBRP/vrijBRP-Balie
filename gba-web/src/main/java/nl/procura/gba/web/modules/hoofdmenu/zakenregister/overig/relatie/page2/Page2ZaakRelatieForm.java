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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.relatie.page2;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.relatie.page2.Page2ZaakRelatieBean.*;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;

public class Page2ZaakRelatieForm extends GbaForm<Page2ZaakRelatieBean> {

  public Page2ZaakRelatieForm(Zaak zaak) {

    setCaption("Zaakrelatie");
    setOrder(ZAAK_OMS, ZAAK_ID, ZAAK_ID_REL);
    setColumnWidths(WIDTH_130, "");

    Page2ZaakRelatieBean bean = new Page2ZaakRelatieBean();
    bean.setZaakOms(ZaakUtils.getTypeEnOmschrijving(zaak));
    bean.setZaakId(zaak.getZaakId());
    bean.setZaakIdRel("");
    setBean(bean);
  }
}
