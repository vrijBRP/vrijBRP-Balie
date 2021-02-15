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

package nl.procura.gba.web.modules.bs.registration.page50;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.registration.AbstractRegistrationPage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakregisterNavigator;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.services.bs.registration.RegistrationService;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagService;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;

/**
 * First registration - summary
 */
public class Page50Registration extends AbstractRegistrationPage {

  public Page50Registration() {
    super("Eerste inschrijving - overzicht");
  }

  @Override
  public void initPage() {
    super.initPage();
    addButton(buttonPrev);
    buttonNext.setEnabled(true);
    buttonNext.setCaption("Proces voltooien (F2)");
    addButton(buttonNext);
    addComponent(new BsStatusForm(getDossier()));
    setInfo("Controleer de gegevens. Druk op Volgende (F2) om verder te gaan.");
    final DossierRegistration zaakDossier = getZaakDossier();
    final PresentievraagService presenceQService = getApplication().getServices().getPresentievraagService();
    addComponent(new RegistrationSummaryLayout(zaakDossier, presenceQService));
  }

  @Override
  public boolean checkPage() {
    if (super.checkPage()) {
      getServices().getRegistrationService().saveRegistration(getZaakDossier());
      return true;
    }

    return false;
  }

  @Override
  public void onNextPage() {
    RegistrationService registration = getServices().getRegistrationService();
    ZaakStatusService statussen = getServices().getZaakStatusService();

    if (getDossier().isStatus(ZaakStatusType.INCOMPLEET)) {
      statussen.updateStatus(getDossier(), statussen.getInitieleStatus(getDossier()), "");
    }

    registration.saveRegistration(getZaakDossier());
    ZaakregisterNavigator.navigatoTo(getDossier(), this, true);
  }
}
