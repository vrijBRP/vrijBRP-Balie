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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page230;

import com.vaadin.ui.Component;

import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.modules.zaken.inbox.overzicht.InboxOverzichtLayout;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.inbox.GbaRestInboxVerwerkingType;
import nl.procura.gba.web.services.zaken.gemeenteinbox.GemeenteInboxRecord;

/**
 * Tonen inbox record
 */

public class Page230Zaken extends ZakenregisterOptiePage<GemeenteInboxRecord> {

  private InboxOverzichtLayout inboxOverzichtLayout;

  public Page230Zaken(GemeenteInboxRecord record) {
    super(record, "Zakenregister - gemeente inbox");
    addButton(buttonPrev);
  }

  @Override
  protected void addOptieButtons() {
    if (GbaRestInboxVerwerkingType.get(getZaak().getVerwerkingId()) != GbaRestInboxVerwerkingType.ONBEKEND) {
      addOptieButton(buttonVerwerken);
    } else {
      addOptieButton(buttonVerwerken);
      buttonVerwerken.setEnabled(false);
    }
  }

  @Override
  protected void addTabs() {
    inboxOverzichtLayout = new InboxOverzichtLayout(getZaak());
    addTab(inboxOverzichtLayout, "Gemeente inbox");
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
