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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page70;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.AANGEVER;
import static nl.procura.standard.Globalfunctions.along;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;

import nl.procura.dto.raas.aanvraag.DocAanvraagDto;
import nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.windows.RaasWindow;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page71.Page71Zaken;
import nl.procura.gba.web.modules.zaken.reisdocument.overzicht.ReisdocumentOverzichtBuilder;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.raas.rest.domain.aanvraag.FindAanvraagRequest;

/**
 * Tonen reisdocumentaanvraag
 */

public class Page70Zaken extends ZakenregisterOptiePage<ReisdocumentAanvraag> {

  private final Button buttonRaas = new Button("RAAS aanvraag");

  public Page70Zaken(ReisdocumentAanvraag aanvraag) {
    super(aanvraag, "Zakenregister - reisdocument");
    addButton(buttonPrev);
  }

  @Override
  protected void addOptieButtons() {

    addOptieButton(buttonDoc);
    addOptieButton(buttonPersonen);
    addOptieButton(buttonFiat);

    if (getServices().getRaasService().isRaasServiceActive()) {
      addOptieButton(buttonVerwerken);
      addOptieButton(buttonRaas);
    }
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if ((button == buttonRaas) || (keyCode == ShortcutAction.KeyCode.F3)) {
      onRaas();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  protected void addTabs(ZaakTabsheet<ReisdocumentAanvraag> tabsheet) {
    ReisdocumentOverzichtBuilder.addTab(tabsheet, getZaak());
  }

  @Override
  protected ZaakPersoonType[] getZoekpersoonTypes() {
    return new ZaakPersoonType[]{ AANGEVER };
  }

  @Override
  protected void goToDocument() {
    getNavigation().goToPage(new Page71Zaken(getZaak()));
  }

  @Override
  protected void goToPersoon(ZaakPersoonType type) {
    goToPersoon("zaken.reisdocument", getZaak().getAnummer(), getZaak().getBurgerServiceNummer());
  }

  private void onRaas() {
    FindAanvraagRequest request = new FindAanvraagRequest(along(getZaak().getAanvraagnummer().getNummer()));
    DocAanvraagDto raasAanvraag = getServices().getRaasService().get(request);
    getParentWindow().addWindow(new RaasWindow(raasAanvraag));
  }
}
