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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.identificatie.page2;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.identificatie.page2.Page2ZaakIdentificatieBean.EXTERN_ID;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.identificatie.page2.Page2ZaakIdentificatieBean.TYPE;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificatie;

public class Page2ZaakIdentificatieForm extends GbaForm<Page2ZaakIdentificatieBean> {

  public Page2ZaakIdentificatieForm(ZaakIdentificatie id) {
    setCaption("Zaakidentificatie");
    setOrder(TYPE, EXTERN_ID);
    setColumnWidths("70px", "");

    Page2ZaakIdentificatieBean bean = new Page2ZaakIdentificatieBean();
    bean.setType(id.getZaakIdType());
    bean.setExternId(id.getExternId());

    setBean(bean);
  }
}
