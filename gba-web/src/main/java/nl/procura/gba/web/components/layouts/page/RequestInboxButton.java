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

package nl.procura.gba.web.components.layouts.page;

import nl.procura.gba.web.modules.hoofdmenu.requestinbox.ModuleRequestInboxParams;
import nl.procura.gba.web.modules.hoofdmenu.requestinbox.RequestInboxWindow;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxService;
import nl.procura.validation.Bsn;

public class RequestInboxButton extends MainButton<RequestInboxService> {

  public RequestInboxButton() {
    addStyleName("relaties");
  }

  @Override
  protected void doCheck() {
    if (getApplication() != null) {
      setCaption("Verzoeken");
      setDescription("De verzoeken van deze persoon");
    }
  }

  @Override
  protected String getListenerId() {
    return "verzoekListener";
  }

  @Override
  protected RequestInboxService getService() {
    return getServices().getRequestInboxService();
  }

  @Override
  protected void onClick() {
    getWindow().addWindow(new RequestInboxWindow(ModuleRequestInboxParams.builder()
        .bsn(getBsn())
        .build()));
  }

  private Bsn getBsn() {
    return new Bsn(getServices().getPersonenWsService().getHuidige().getPersoon().getBsn().toLong());
  }
}
