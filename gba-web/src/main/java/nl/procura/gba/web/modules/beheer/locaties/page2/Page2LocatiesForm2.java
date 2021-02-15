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

package nl.procura.gba.web.modules.beheer.locaties.page2;

import static nl.procura.gba.web.modules.beheer.locaties.page2.Page2LocatiesBean.*;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.beheer.locatie.Locatie;

public class Page2LocatiesForm2 extends GbaForm<Page2LocatiesBean> {

  public Page2LocatiesForm2(Locatie locatie) {

    setCaption("Normale locatie");
    setOrder(IPADRES, IPADRESSEN, RASCODE, RAASCODE, JCCGKASID);
    setColumnWidths("200px", "");
    setReadThrough(true);

    Page2LocatiesBean bean = new Page2LocatiesBean();

    if (locatie.isStored()) {

      bean.setIPadressen(locatie.getIp());
      bean.setRasCode(locatie.getCodeRas());
      bean.setRaasCode(locatie.getCodeRaas());
      bean.setJCCGKASid(locatie.getGkasId());
    }

    setBean(bean);
  }

  @Override
  public void attach() {
    getBean().setIPadres(getApplication().getHttpRequest().getRemoteAddr());
    super.attach();
  }
}
