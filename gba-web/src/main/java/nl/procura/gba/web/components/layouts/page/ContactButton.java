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

package nl.procura.gba.web.components.layouts.page;

import nl.procura.gba.web.modules.zaken.contact.ContactWindow;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;

public class ContactButton extends MainButton<ContactgegevensService> {

  public ContactButton() {
    setCaption("Contact");
    setWidth("70px");
  }

  @Override
  protected void doCheck() {

    if (getApplication() != null) {
      if (getService().isVastGesteld(getServices().getPersonenWsService().getHuidige(), true)) {
        setDescription("Van deze persoon zijn de contactgegevens vandaag (opnieuw) vastgesteld.");
        addStyleName("indicatie yes");
      } else {
        setDescription("Van deze persoon zijn de contactgegevens vandaag nog niet (opnieuw) vastgesteld.");
        addStyleName("indicatie no");
      }
    }
  }

  @Override
  protected String getListenerId() {
    return "contactListener";
  }

  @Override
  protected ContactgegevensService getService() {
    return getServices().getContactgegevensService();
  }

  @Override
  protected void onClick() {
    getWindow().addWindow(new ContactWindow());
  }
}
