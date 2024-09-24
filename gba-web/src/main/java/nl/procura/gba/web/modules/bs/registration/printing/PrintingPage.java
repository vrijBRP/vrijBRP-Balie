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

package nl.procura.gba.web.modules.bs.registration.printing;

import static nl.procura.gba.web.services.zaken.documenten.DocumentType.REGISTRATION;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.form.document.PrintMultiLayout;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.services.bs.registration.document.RegistrationTemplateData;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * first registration - print
 */
public class PrintingPage extends NormalPageTemplate {

  private PrintRegistrationForm  printForm;
  private final VLayout          soortLayout = new VLayout();
  private final PrintMultiLayout printLayout;

  private final Button              buttonPreview;
  private final Button              buttonPrint;
  private final DossierRegistration firstRegistrationDossier;

  PrintingPage(DossierRegistration firstRegistrationDossier) {
    this.firstRegistrationDossier = firstRegistrationDossier;
    printLayout = new PrintMultiLayout(firstRegistrationDossier, firstRegistrationDossier.getDossier(), null,
        REGISTRATION);

    final Button[] button = printLayout.getButtons();
    buttonPreview = button[0];
    buttonPrint = button[1];
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      addButton(buttonPreview);
      addButton(buttonPrint, 1F);
      addButton(buttonClose);

      printForm = new PrintRegistrationForm(firstRegistrationDossier);

      setInfo("Selecteer de persoon en het document. Druk het document af. "
          + "<br/>Druk op Volgende (F2) om verder te gaan.");
      addComponent(printForm);
      addComponent(soortLayout);
      setSoort();
    }

    super.event(event);
  }

  private void setSoort() {
    soortLayout.removeAllComponents();
    soortLayout.addComponent(getPrintLayout());
  }

  private Fieldset getPrintLayout() {
    final VLayout layout = new VLayout();
    layout.addComponent(printLayout);
    return new Fieldset("Documenten", layout);
  }

  public boolean checkPage() {
    printForm.commit();
    return false;
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonPreview || button == buttonPrint || keyCode == ShortcutAction.KeyCode.F3) {
      setModel();
      printLayout.handleActions(button, keyCode);
    }

    super.handleEvent(button, keyCode);
  }

  private void setModel() {
    checkPage();

    final DossierPersoon person = (DossierPersoon) printForm.getField(PrintingBean.F_PERSON).getValue();
    printForm.getField(PrintingBean.F_PERSON).commit();
    printLayout.setModel(new RegistrationTemplateData(getServices(), firstRegistrationDossier, person));
  }
}
