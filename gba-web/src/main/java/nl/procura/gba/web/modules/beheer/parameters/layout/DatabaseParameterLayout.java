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

package nl.procura.gba.web.modules.beheer.parameters.layout;

import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.beheer.gebruikers.page4.Page4Gebruikers;
import nl.procura.gba.web.modules.beheer.parameters.bean.ParameterBean;
import nl.procura.gba.web.modules.beheer.parameters.form.DatabaseParameterForm;
import nl.procura.gba.web.modules.beheer.profielen.page11.Page11Profielen;
import nl.procura.gba.web.services.beheer.parameter.ParameterType;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.form.tableform.TableForm.Property;
import nl.procura.vaadin.functies.VaadinUtils;

public class DatabaseParameterLayout extends ParameterLayout<DatabaseParameterForm> {

  public DatabaseParameterLayout(GbaApplication gbaApplication, String naam, String category) {

    super(gbaApplication, fil(naam) ? (naam + " - parameters - " + category) : ("Parameters - " + category));

    setGbaApplication(gbaApplication);
    setForm(category);
    setMargin(false);

    addButton(buttonPrev);
    addButton(buttonSave);

    addComponent(getForm());
  }

  @Override
  public void attach() {
    hidePreviousButton();
    super.attach();
  }

  @Override
  public void onPreviousPage() {

    if (isModuleGebruiker()) {
      Page4Gebruikers page = VaadinUtils.getChild(getWindow(), Page4Gebruikers.class);
      page.getNavigation().goBackToPage(page.getNavigation().getPreviousPage());
    } else if (isModuleProfiel()) {
      Page11Profielen page = VaadinUtils.getChild(getWindow(), Page11Profielen.class);
      page.getNavigation().goBackToPage(page.getNavigation().getPreviousPage());
    }
  }

  @Override
  public void onSave() {

    getForm().commit();
    getWindow().addWindow(new ConfirmDialog("Weet u zeker dat u deze gegevens wilt wijzigen?", 350) {

      @Override
      public void buttonYes() {
        close();
        doSave();
      }
    });
  }

  public void setForm(String category) {
    setForm(new DatabaseParameterForm(category));
  }

  protected void doSave() {

    DatabaseParameterForm form = getForm();
    long cUsr = ParameterBean.GEEN_GEBRUIKER;
    long cProfile = ParameterBean.GEEN_PROFIEL;

    if (isModuleGebruiker()) {
      cUsr = VaadinUtils.getChild(getWindow(), Page4Gebruikers.class).getGebruiker().getCUsr();
    }
    if (isModuleProfiel()) {
      cProfile = VaadinUtils.getChild(getWindow(), Page11Profielen.class).getProfiel().getCProfile();
    }

    form.save(cUsr, cProfile);
  }

  protected boolean isParameter(Property property, ParameterType type) {
    return !ParameterBean.getFields((b) -> b.isFieldName(property.getId()) && b.isType(type)).isEmpty();
  }

  private void hidePreviousButton() {
    if (!isPreviousButton()) {
      getButtonLayout().removeComponent(buttonPrev);
    }
  }
}
