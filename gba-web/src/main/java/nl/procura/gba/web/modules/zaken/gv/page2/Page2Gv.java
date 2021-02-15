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

package nl.procura.gba.web.modules.zaken.gv.page2;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.modules.hoofdmenu.gv.overzicht.GvOverzichtBuilder;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.zaken.common.ZakenOverzichtPage;
import nl.procura.gba.web.services.zaken.gv.GvAanvraag;
import nl.procura.gba.web.windows.home.HomeWindow;

/**
 * Tonen Gegevensverstrekking (Gv)
 */

public class Page2Gv extends ZakenOverzichtPage<GvAanvraag> {

  public Page2Gv(GvAanvraag aanvraag) {

    super(aanvraag, "Gegevensverstrekking");

    addButton(buttonPrev);
  }

  @Override
  protected void addOptieButtons() {

    boolean isInBehandeling = getZaak().getStatus() == ZaakStatusType.INBEHANDELING;
    if (isInBehandeling) {
      buttonAanpassen.setCaption("Zaak vervolgen");
      addOptieButton(buttonAanpassen);
      buttonAanpassen.setVisible(isInBehandeling);
    }
  }

  @Override
  protected void addTabs(ZaakTabsheet<GvAanvraag> tabsheet) {
    GvOverzichtBuilder.addTab(tabsheet, getZaak());
  }

  @Override
  protected void goToZaak() {

    getApplication().getServices().getMemoryService().setObject(GvAanvraag.class, getZaak());
    getApplication().openWindow(getWindow(), new HomeWindow(), "bs.gv");
  }
}
