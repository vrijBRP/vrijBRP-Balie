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

package nl.procura.gba.web.modules.hoofdmenu.inbox.page2;

import static nl.procura.gba.web.modules.hoofdmenu.inbox.page2.Page2InboxBean1.ZAAK_ID;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.inbox.InboxRecord;

public class Page2InboxForm1 extends GbaForm<Page2InboxBean1> {

  public Page2InboxForm1(InboxRecord inboxRecord) {

    setColumnWidths(WIDTH_130, "");
    setCaption("Bericht");
    setOrder(ZAAK_ID);

    Page2InboxBean1 bean = new Page2InboxBean1();
    bean.setZaakId(inboxRecord.getZaakId());
    setBean(bean);
  }

  public void resetForms() {
  }
}
