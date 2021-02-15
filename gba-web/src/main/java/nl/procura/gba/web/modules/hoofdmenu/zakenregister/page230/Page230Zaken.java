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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page230;

import com.vaadin.ui.Component;

import nl.procura.gba.web.modules.hoofdmenu.inbox.overzicht.InboxOverzichtLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.services.inbox.InboxRecord;

/**
 * Tonen inbox record
 */

public class Page230Zaken extends ZakenregisterOptiePage<InboxRecord> {

  private InboxOverzichtLayout inboxOverzichtLayout;

  public Page230Zaken(InboxRecord record) {
    super(record, "Zakenregister - inbox berichten");
    addButton(buttonPrev);
  }

  @Override
  protected void addOptieButtons() {
    addOptieButton(buttonVerwerken);
  }

  @Override
  protected void addTabs() {
    inboxOverzichtLayout = new InboxOverzichtLayout(getZaak());
    addTab(inboxOverzichtLayout, "Bericht");
  }

  @Override
  protected Component getSelectedTab() {
    // Als de vorige pagina ook deze pagina is,
    // dan direct het Bericht tabje tonen
    if (getNavigation().getPreviousPage() instanceof Page230Zaken) {
      return inboxOverzichtLayout;
    }
    return null;
  }
}
