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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page40;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.AANGEVER;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page41.Page41Zaken;
import nl.procura.gba.web.modules.zaken.vog.overzicht.VogOverzichtBuilder;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;

/**
 * Tonen verklaring omtrent gedrag
 */

public class Page40Zaken extends ZakenregisterOptiePage<VogAanvraag> {

  private final Button buttonStatus = new Button("Status opvragen (F3)");

  public Page40Zaken(VogAanvraag aanvraag) {
    super(aanvraag, "Zakenregister - verklaring omtrent gedrag");
    addButton(buttonPrev);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if ((button == buttonStatus) || (keyCode == KeyCode.F3)) {
      onStatusCheck();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  protected void addOptieButtons() {
    addOptieButton(buttonDoc);
    addOptieButton(buttonPersonen);
    addOptieButton(buttonStatus);
  }

  @Override
  protected void addTabs(ZaakTabsheet<VogAanvraag> tabsheet) {
    VogOverzichtBuilder.addTab(tabsheet, getZaak());
  }

  @Override
  protected ZaakPersoonType[] getZoekpersoonTypes() {
    return new ZaakPersoonType[]{ AANGEVER };
  }

  @Override
  protected void goToDocument() {
    getNavigation().goToPage(new Page41Zaken(getZaak()));
  }

  @Override
  protected void goToPersoon(ZaakPersoonType type) {
    goToPersoon("zaken.vog", getZaak().getAnummer(), getZaak().getBurgerServiceNummer());
  }

  private void onStatusCheck() {
    getServices().getVogBerichtService().findStatus(getZaak());
  }
}
