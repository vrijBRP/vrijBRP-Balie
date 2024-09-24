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

package nl.procura.gba.web.modules.zaken.reisdocument.page2;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.dto.raas.aanvraag.DocAanvraagDto;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.jpa.personen.dao.GenericDao;
import nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.windows.RaasWindow;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.zaken.common.ZakenOverzichtPage;
import nl.procura.gba.web.modules.zaken.reisdocument.bezorging.BezorgingWindow;
import nl.procura.gba.web.modules.zaken.reisdocument.overzicht.ReisdocumentOverzichtBuilder;
import nl.procura.gba.web.modules.zaken.reisdocument.page10.Page10Reisdocument;
import nl.procura.gba.web.modules.zaken.reisdocument.page18.Page18Reisdocument;
import nl.procura.gba.web.modules.zaken.reisdocument.page19.Page19Reisdocument;
import nl.procura.gba.web.modules.zaken.reisdocument.page24.Page24Reisdocument;
import nl.procura.gba.web.modules.zaken.reisdocument.page3.Page3Reisdocument;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.raas.rest.domain.aanvraag.FindAanvraagRequest;

/**
 * Tonen reisdocumentaanvraag
 */
public class Page2Reisdocument extends ZakenOverzichtPage<ReisdocumentAanvraag> {

  private final Button buttonUitreiken = new Button("Uitreiken");
  private final Button buttonRaas      = new Button("RAAS aanvraag");
  private final Button buttonBezorg    = new Button("Thuisbezorging");

  public Page2Reisdocument(ReisdocumentAanvraag aanvraag) {
    super(aanvraag, "Reisdocument");
    addButton(buttonPrev);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (!GenericDao.isSaved(getZaak()) && ((button == buttonNext) || (keyCode == KeyCode.F2))) {
      getNavigation().goToPage(new Page19Reisdocument(getZaak()));

    } else if (button == buttonUitreiken) {
      getNavigation().goToPage(new Page24Reisdocument(getZaak()));

    } else if (button == buttonRaas) {
      FindAanvraagRequest request = new FindAanvraagRequest(getZaak().getAanvraagnummer().toLong());
      DocAanvraagDto raasAanvraag = getServices().getRaasService().get(request);
      getParentWindow().addWindow(new RaasWindow(raasAanvraag));

    } else if ((button == buttonBezorg)) {
      getParentWindow().addWindow(new BezorgingWindow(getZaak()));
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  protected void addOptieButtons() {
    if (ZaakStatusType.INCOMPLEET.equals(getZaak().getStatus())) {
      addOptieButton(buttonAanpassen);
    }

    addOptieButton(buttonDoc);
    addOptieButton(buttonUitreiken);
    addOptieButton(buttonFiat);

    if (getServices().getRaasService().isRaasServiceActive()) {
      addOptieButton(buttonVerwerken);
      addOptieButton(buttonRaas);
    }

    if (buttonVerzoek.isRequest()) {
      addOptieButton(buttonVerzoek);
    }

    if (getServices().getReisdocumentBezorgingService().isEnabled() || getZaak().getThuisbezorging().isMelding()) {
      addOptieButton(buttonBezorg);
    }
  }

  @Override
  protected void addTabs(ZaakTabsheet<ReisdocumentAanvraag> tabsheet) {
    ReisdocumentOverzichtBuilder.addTab(tabsheet, getZaak());
  }

  @Override
  protected void goToZaak() {
    if (getZaak().getAanvraagnummer().isCorrect()) {
      getNavigation().goToPage(new Page10Reisdocument(getZaak()));
    } else {
      getNavigation().goToPage(new Page18Reisdocument(getZaak()));
    }
  }

  @Override
  protected void goToDocument() {
    getNavigation().goToPage(new Page3Reisdocument(getZaak()));
  }
}
