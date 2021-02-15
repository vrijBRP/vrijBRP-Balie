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

package nl.procura.gba.web.modules.bs.registration.page10;

import nl.procura.gba.web.modules.bs.common.layouts.relocation.AddressLayout;
import nl.procura.gba.web.modules.bs.registration.AbstractRegistrationPage;

/**
 * First registration - declaration
 */
public class Page10Registration extends AbstractRegistrationPage {

  private Page10DeclarationForm declarationForm;
  private InterpreterForm       interpreterForm;
  private AddressLayout         addressLayout;

  public Page10Registration() {
    super("Eerste inschrijving - aangifte");
  }

  @Override
  public boolean checkPage() {
    if (super.checkPage()) {
      declarationForm.save();
      interpreterForm.save();

      if (addressLayout.isSaved()) {
        getServices().getRegistrationService().saveRegistration(getZaakDossier());
        return true;
      }
    }

    return false;
  }

  @Override
  public void initPage() {
    super.initPage();

    buttonPrev.setEnabled(false);
    addButton(buttonPrev);
    addButton(buttonNext);

    declarationForm = new Page10DeclarationForm(getDossier());
    interpreterForm = new InterpreterForm(getZaakDossier());
    addressLayout = new AddressLayout(new RegistrationAddress(getZaakDossier()), getServices());

    addComponent(declarationForm);
    addComponent(interpreterForm);
    addComponent(addressLayout);
  }
}
