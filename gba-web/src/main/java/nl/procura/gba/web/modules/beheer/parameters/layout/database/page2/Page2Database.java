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

package nl.procura.gba.web.modules.beheer.parameters.layout.database.page2;

import java.io.File;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.gba.config.GbaProperties;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.window.Message;

public class Page2Database extends NormalPageTemplate {

  private final Button      buttonConnection = new Button("Test verbinding (F3)");
  private final File        propertiesFile;
  private Page2DatabaseForm form             = null;

  public Page2Database(File propertiesFile) {
    this.propertiesFile = propertiesFile;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonSave);
      addButton(buttonConnection, 1f);
      addButton(buttonClose);

      GbaProperties config = null;
      if (propertiesFile.exists()) {
        config = new GbaProperties(propertiesFile);
      }

      Page2DatabaseBean bean = new Page2DatabaseBean(config);
      form = new Page2DatabaseForm(bean);
      addComponent(form);
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
  public void onClose() {
    getWindow().closeWindow();
    super.onClose();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
    super.onPreviousPage();
  }

  @Override
  public void onSave() {

    form.commit();

    form.checkConnection();

    form.getBean().save(propertiesFile);

    successMessage("De gegevens zijn opgeslagen");
  }

  private void testConnectie() {
    form.checkConnection();
    new Message(getWindow(), "Verbinding is gelukt.", Message.TYPE_SUCCESS);
  }
}
