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

package nl.procura.gba.web.modules.bs.registration.page30;

import java.util.List;

import nl.procura.gba.web.modules.bs.registration.AbstractRegistrationPage;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;

public class Page30Registration extends AbstractRegistrationPage {

  private DeclarantForm declarantForm;

  public Page30Registration() {
    super("Eerste inschrijving - aangever");
  }

  @Override
  protected void initPage() {
    addButton(buttonPrev);
    addButton(buttonNext);

    List<DossierPersoon> people = getDossier().getPersonen(DossierPersoonType.INSCHRIJVER,
        DossierPersoonType.GERELATEERDE_BRP);
    setInfo("Selecteer uit de keuzelijst wie aangever is in deze aangifte en geef indien nodig hierbij een " +
        "toelichting. <br/>De lijst bevat personen die zich vestigen en eventuele gerelateerden van deze personen in de "
        +
        "BRP die 16 jaar of ouder zijn.");
    declarantForm = new DeclarantForm(getZaakDossier(), people);
    addComponent(declarantForm);

    super.initPage();
  }

  @Override
  public boolean checkPage() {
    if (super.checkPage()) {
      DossierRegistration registrationDossier = getZaakDossier();
      declarantForm.saveDeclarant(registrationDossier);
      getServices().getRegistrationService().saveRegistration(registrationDossier);
      return true;
    }

    return false;
  }

}
