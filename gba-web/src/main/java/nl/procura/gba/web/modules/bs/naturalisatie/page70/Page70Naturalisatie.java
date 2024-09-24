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

package nl.procura.gba.web.modules.bs.naturalisatie.page70;

import static nl.procura.gba.common.ZaakStatusType.INBEHANDELING;
import static nl.procura.gba.common.ZaakStatusType.INCOMPLEET;
import static nl.procura.gba.common.ZaakStatusType.VERWERKT;

import nl.procura.gba.web.modules.bs.naturalisatie.BsPageNaturalisatie;
import nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakregisterNavigator;
import nl.procura.gba.web.services.bs.naturalisatie.NaturalisatieService;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page70Naturalisatie extends BsPageNaturalisatie {

  public Page70Naturalisatie() {
    super("Nationaliteit - overzicht");
  }

  @Override
  public void event(PageEvent event) {

    try {

      if (event.isEvent(InitPage.class)) {

        buttonNext.setCaption("Proces voltooien (F2)");
        addButton(buttonPrev);
        addButton(buttonNext);

        setInfo("Controleer de gegevens. Druk op Volgende (F2) om verder te gaan.");
        addComponent(new NaturalisatieOverzichtLayout(getZaakDossier()));
      }
    } finally {
      super.event(event);
    }
  }

  @Override
  public void onNextPage() {

    if (getDossier().isStatus(INCOMPLEET, INBEHANDELING)) {
      String msg = "Wilt u de status van de zaak wijzigen naar <b><u>Verwerkt</u></b>?";
      getApplication().getParentWindow().addWindow(new ConfirmDialog("Status wijzigen?", msg, "400px") {

        @Override
        public void buttonYes() {
          updateStatus();
          saveAndEndProcess();
          super.buttonYes();
        }

        @Override
        public void buttonNo() {
          saveAndEndProcess();
          super.buttonNo();
        }
      });

    } else {
      saveAndEndProcess();
    }
    super.onNextPage();
  }

  private void updateStatus() {
    ZaakStatusService statussen = getApplication().getServices().getZaakStatusService();
    statussen.updateStatus(getDossier(), getDossier().getStatus(), VERWERKT, "");
  }

  private void saveAndEndProcess() {
    NaturalisatieService service = getApplication().getServices().getNaturalisatieService();
    service.save(getDossier());
    ZaakregisterNavigator.navigatoTo(getDossier(), this, true);
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }
}
