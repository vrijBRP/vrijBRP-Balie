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

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.beheer.parameters.form.ConfigParameterForm;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.window.Message;

public class ConfigParameterLayout extends ParameterLayout<ConfigParameterForm> {

  private final Button buttonConnection = new Button("Test verbinding (F3)");

  public ConfigParameterLayout(GbaApplication application, String naam, String category) {
    super(application, fil(naam) ? (naam + " - parameters - " + category) : ("Parameters - " + category));
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setMargin(false);

      addButton(buttonSave);
      addButton(buttonConnection);

      ConfigParameterForm form = new ConfigParameterForm();
      form.setDatabases(getApplication().getServices().getOnderhoudService().getDatabases());

      addComponent(form);
      setForm(form);
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if ((button == buttonConnection) || (keyCode == KeyCode.F3)) {
      testConnectie();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onSave() {

    getWindow().addWindow(new ConfirmDialog("Weet u zeker dat u deze gegevens wilt wijzigen?", 350) {

      @Override
      public void buttonYes() {

        close();

        doSave();
      }
    });
  }

  private void doSave() {

    ConfigParameterForm form = getForm();

    form.save();

    successMessage("Het configuratiebestand is opgeslagen.");
  }

  private void testConnectie() {

    ConfigParameterForm form = getForm();

    form.checkConnection();

    new Message(getWindow(), "Verbinding is gelukt.", Message.TYPE_SUCCESS);
  }
}
