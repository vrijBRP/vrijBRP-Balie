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

package nl.procura.gba.web.modules.zaken.common;

import com.vaadin.ui.Button;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.hoofdmenu.requestinbox.ModuleRequestInboxParams;
import nl.procura.gba.web.modules.hoofdmenu.requestinbox.RequestInboxWindow;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType;

import lombok.Setter;

@Setter
public class ZaakRequestInboxButton extends Button {

  private Zaak zaak;

  public ZaakRequestInboxButton() {
    setCaption("Toon verzoek");
  }

  public boolean isRequest() {
    return zaak.getZaakHistorie().getAttribuutHistorie().is(ZaakAttribuutType.REQUEST_INBOX);
  }

  public void onClick() {
    GbaApplication app = (GbaApplication) getApplication();
    RequestInboxService service = app.getServices().getRequestInboxService();
    service.getRelatedRequestInboxItemId(zaak)
        .ifPresent(id -> {
          ModuleRequestInboxParams params = ModuleRequestInboxParams.builder()
              .item(service.getRequestInboxItem(id))
              .build();
          app.getParentWindow().addWindow(new RequestInboxWindow(params));
        });
  }
}
