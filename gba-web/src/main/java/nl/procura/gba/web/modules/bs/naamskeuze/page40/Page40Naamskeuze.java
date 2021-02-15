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

package nl.procura.gba.web.modules.bs.naamskeuze.page40;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.modules.bs.common.pages.printpage.BsPrintPage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakregisterNavigator;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.gba.web.services.bs.naamskeuze.NaamskeuzeService;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page40Naamskeuze extends BsPrintPage<DossierNaamskeuze> {

  public Page40Naamskeuze() {
    super("Naamskeuze - afdrukken", "Afdrukken documenten naamskeuzeaangifte");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      buttonNext.setCaption("Proces voltooien (F2)");
      setModel(getZaakDossier());
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {

    NaamskeuzeService naamskeuzeen = getApplication().getServices().getNaamskeuzeService();
    ZaakStatusService statussen = getApplication().getServices().getZaakStatusService();

    if (getDossier().isStatus(ZaakStatusType.INCOMPLEET)) {
      statussen.updateStatus(getDossier(), statussen.getInitieleStatus(getDossier()), "");
    }

    naamskeuzeen.save(getDossier());

    if (getWindow() instanceof ModalWindow) {
      getWindow().closeWindow();

    } else {
      ZaakregisterNavigator.navigatoTo(getDossier(), this, true);
    }

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
    List<DocumentType> types = new ArrayList<>();
    types.add(DocumentType.NAAMSKEUZE);

    if (getZaakDossier().isSprakeLatereVermelding()) {
      types.add(DocumentType.LATERE_VERMELDING_NK);
    }

    return types.toArray(new DocumentType[0]);
  }
}
