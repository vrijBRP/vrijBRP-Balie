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

package nl.procura.gba.web.modules.zaken.contact;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.contact.Contact;
import nl.procura.gba.web.services.zaken.contact.ContactStatusListener;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public class ContactWindow extends GbaModalWindow {

  private final Contact               contact;
  private final ContactStatusListener succesListener;

  public ContactWindow() {
    this(null);
  }

  public ContactWindow(ContactStatusListener succesListener) {
    this(Services.getInstance().getContactgegevensService().getCurrentContact(), succesListener);
  }

  public ContactWindow(Contact contact, ContactStatusListener succesListener) {
    super("Contactgegevens (Druk op escape om te sluiten)", "800px");
    this.contact = contact;
    this.succesListener = succesListener;
  }

  @Override
  public void attach() {

    super.attach();

    MainModuleContainer mainModule = new MainModuleContainer();
    addComponent(mainModule);
    mainModule.getNavigation().addPage(new ModuleContact(contact, succesListener));
  }
}
