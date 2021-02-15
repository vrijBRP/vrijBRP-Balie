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

package nl.procura.gba.web.modules.bs.erkenning.page35;

import static nl.procura.gba.common.MiscUtils.to;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.erkenning.BsPageErkenning;
import nl.procura.gba.web.modules.bs.erkenning.overzicht.ErkenningOverzichtLayout;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Erkenning
 */

public class Page35Erkenning extends BsPageErkenning {

  public Page35Erkenning() {

    super("Erkenning - overzicht");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      addButton(buttonNext, 1f);

      // Als het een popup is en de zaak is reeds afgerond. Dan close button tonen
      if (getWindow() instanceof ModalWindow && getZaakDossier().getDossier().getStatus().isMinimaal(
          ZaakStatusType.OPGENOMEN)) {
        addButton(buttonClose);
      }

      addComponent(new BsStatusForm(getZaakDossier().getDossier()));

      setInfo("Controleer de gegevens");

      addComponent(new ErkenningOverzichtLayout(getZaakDossier()));
    }

    super.event(event);
  }

  @Override
  public void onClose() {

    to(getWindow(), ModalWindow.class).closeWindow();

    super.onClose();
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }
}
