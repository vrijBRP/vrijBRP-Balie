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

package nl.procura.gba.web.modules.bs.lv.page60;

import static nl.procura.gba.web.services.zaken.documenten.DocumentType.LATERE_VERMELDING_AFST;

import com.vaadin.ui.Button;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.modules.bs.common.pages.printpage.BsPrintPage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakregisterNavigator;
import nl.procura.gba.web.services.bs.lv.afstamming.DossierLv;
import nl.procura.gba.web.services.bs.lv.afstamming.DossierLvTemplateData;
import nl.procura.gba.web.services.bs.lv.afstamming.LvService;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page60Lv extends BsPrintPage<DossierLv> {

  public Page60Lv() {
    super("Latere vermelding - afdrukken", "Afdrukken documenten");
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      buttonNext.setCaption("Proces voltooien (F2)");
      setModel(new DossierLvTemplateData(getZaakDossier()), getZaakDossier());
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    getApplication().getServices().getLvService().save(getDossier());
    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNextPage() {
    LvService lvService = getApplication().getServices().getLvService();
    ZaakStatusService statussen = getApplication().getServices().getZaakStatusService();

    if (getDossier().isStatus(ZaakStatusType.INCOMPLEET)) {
      statussen.updateStatus(getDossier(), statussen.getInitieleStatus(getDossier()), "");
    }

    lvService.save(getDossier());
    ZaakregisterNavigator.navigatoTo(getDossier(), this, true);

    super.onNextPage();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  @Override
  public void setButtons() {
    addButton(buttonPrev);
    addButton(getPrintButtons());
    addButton(buttonNext);

    super.setButtons();
  }

  @Override
  protected DocumentType[] getDocumentTypes() {
    return new DocumentType[]{getZaakDossier().getSoort().getDocumentType(), LATERE_VERMELDING_AFST};
  }
}
