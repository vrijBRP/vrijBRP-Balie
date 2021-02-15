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

package nl.procura.gba.web.modules.zaken.vog.page2;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.modules.zaken.common.ZakenOverzichtPage;
import nl.procura.gba.web.modules.zaken.vog.overzicht.VogOverzichtBuilder;
import nl.procura.gba.web.modules.zaken.vog.page10.Page10Vog;
import nl.procura.gba.web.modules.zaken.vog.page3.Page3Vog;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;

/**
 * Tonen verklaring omtrent gedrag
 */

public class Page2Vog extends ZakenOverzichtPage<VogAanvraag> {

  private final Button buttonFindStatus = new Button("Status opvragen (F3)");

  public Page2Vog(VogAanvraag aanvraag) {

    super(aanvraag, "Verklaring omtrent gedrag");

    addButton(buttonPrev);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if ((button == buttonFindStatus) || (keyCode == KeyCode.F3)) {
      onStatusCheck();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  protected void addOptieButtons() {
    if (ZaakStatusType.INCOMPLEET.equals(getZaak().getStatus())) {
      addOptieButton(buttonAanpassen);
    }
    addOptieButton(buttonDoc);
    addOptieButton(buttonFindStatus);
  }

  @Override
  protected void addTabs() {
    VogOverzichtBuilder.addTab(getTabsheet(), getZaak());
  }

  @Override
  protected void goToZaak() {
    getNavigation().goToPage(new Page10Vog(getZaak()));
  }

  @Override
  protected void goToDocument() {
    getNavigation().goToPage(new Page3Vog(getZaak()));
  }

  private void onStatusCheck() {
    getServices().getVogBerichtService().findStatus(getZaak());
  }
}
