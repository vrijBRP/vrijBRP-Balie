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

import nl.procura.gba.web.services.applicatie.onderhoud.OnderhoudService.IdVoorraad;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public class IdVoorraadWindow extends ModalWindow {

  public IdVoorraadWindow(IdVoorraad idVoorraad) {
    setWidth("500px");
    VLayout vLayout = new VLayout()
        .spacing(true);
    setCaption("Voorraad a-nummers en BSN's (Druk op escape om te sluiten)");
    if (idVoorraad.getMin() > 0) {
      vLayout.addComponent(new InfoLayout("Bij minder dan " + idVoorraad.getMin()
          + " a-nummers of BSN's wordt een melding gegeven bij inloggen. <br/>De applicatiebeheerder kan deze dan aanvullen."));
    }
    vLayout.addComponent(new Page1OnderhoudForm(idVoorraad));
    addComponent(vLayout);
  }
}
