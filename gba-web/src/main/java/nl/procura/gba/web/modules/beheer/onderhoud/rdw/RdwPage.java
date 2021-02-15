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

package nl.procura.gba.web.modules.beheer.onderhoud.rdw;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.zaken.rijbewijs.errorpage.RijbewijsErrorWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.rijbewijs.NrdServices;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAccount;
import nl.procura.rdw.functions.RdwMessage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class RdwPage extends NormalPageTemplate {

  private final RdwWindowListener listener;

  public RdwPage(RdwWindowListener listener) {
    this.listener = listener;
  }

  @Override
  public void event(PageEvent event) {
    setMargin(true);
    super.event(event);
  }

  public RdwWindowListener getListener() {
    return listener;
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  protected RijbewijsAccount getAccount() {
    return Services.getInstance().getRijbewijsService().getAccount();
  }

  /**
   * Stuur bericht naar RDW en toon foutmelding indien van toepassing
   */
  protected boolean sendMessage(RdwMessage message) {

    if (NrdServices.sendMessage(getServices().getRijbewijsService(), message)) {
      return true;
    }

    getParentWindow().addWindow(new RijbewijsErrorWindow(message.getResponse().getMelding()));
    return false;
  }
}
