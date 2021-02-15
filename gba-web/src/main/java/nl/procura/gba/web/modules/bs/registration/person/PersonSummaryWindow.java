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

package nl.procura.gba.web.modules.bs.registration.person;

import com.vaadin.ui.Button;

import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.bs.algemeen.functies.BsDossierNaamgebruikUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public class PersonSummaryWindow extends GbaModalWindow {

  private DossierPersoon person;
  private boolean        allowToPersonList;

  public PersonSummaryWindow(DossierPersoon person, boolean allowToPersonList) {
    super("Persoonsgegevens (Druk op escape om te sluiten)", "800px");
    this.person = person;
    this.allowToPersonList = allowToPersonList;
    PersonSummaryForm personForm = new PersonSummaryForm(person);
    String title = BsDossierNaamgebruikUtils.getNormalizedNameWithAge(person);
    addComponent(new MainModuleContainer(false, new Page(personForm, title)));
  }

  private class Page extends NormalPageTemplate {

    protected final Button buttonPL = new Button("Persoonslijst");

    private Page(GbaForm form, String title) {
      super(title);
      buttonPL.addListener(this);
      buttonClose.addListener(this);
      if (allowToPersonList) {
        getMainbuttons().add(buttonPL);
      }
      getMainbuttons().add(buttonClose);
      addComponent(form);
    }

    @Override
    public void handleEvent(Button button, int keyCode) {
      if (button == buttonPL) {
        String bsn = person.getBurgerServiceNummer().getStringValue();
        getApplication().goToPl(getApplication().getParentWindow(), "#pl.persoon", PLEDatasource.STANDAARD, bsn);
        closeWindow();
      }
      super.handleEvent(button, keyCode);
    }

    @Override
    public void onClose() {
      super.onClose();
      getWindow().closeWindow();
    }
  }
}
