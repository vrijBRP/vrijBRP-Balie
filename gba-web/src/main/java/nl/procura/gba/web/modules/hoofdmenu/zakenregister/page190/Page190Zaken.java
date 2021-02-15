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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page190;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.BETROKKENE;

import com.vaadin.ui.Button;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.modules.hoofdmenu.gv.overzicht.GvOverzichtBuilder;
import nl.procura.gba.web.modules.hoofdmenu.gv.page2.Page2Gv;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page191.Page191Zaken;
import nl.procura.gba.web.services.zaken.gv.GvAanvraag;

/**
 * Tonen gegevensverstrekking
 */

public class Page190Zaken extends ZakenregisterOptiePage<GvAanvraag> {

  protected final Button buttonVervolgen = new Button("Zaak vervolgen");

  public Page190Zaken(GvAanvraag zaak) {

    super(zaak, "Zakenregister - gegevensverstrekking");

    addButton(buttonPrev);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonVervolgen) {
      getNavigation().goToPage(new Page2Gv(getZaak()));
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  protected void addOptieButtons() {

    addOptieButton(buttonDoc);
    addOptieButton(buttonPersonen);

    boolean isInBehandeling = getZaak().getStatus() == ZaakStatusType.INBEHANDELING;
    if (isInBehandeling) {
      addOptieButton(buttonVervolgen);
      buttonVervolgen.setVisible(isInBehandeling);
    }
  }

  @Override
  protected void addTabs(ZaakTabsheet<GvAanvraag> tabsheet) {
    GvOverzichtBuilder.addTab(tabsheet, getZaak());
  }

  @Override
  protected ZaakPersoonType[] getZoekpersoonTypes() {
    return new ZaakPersoonType[]{ BETROKKENE };
  }

  @Override
  protected void goToDocument() {
    getNavigation().goToPage(new Page191Zaken(getZaak()));
  }

  @Override
  protected void goToPersoon(ZaakPersoonType type) {
    goToPersoon("zaken.gv", getZaak().getAnummer(), getZaak().getBurgerServiceNummer());
  }
}
