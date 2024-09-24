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

package nl.procura.gba.web.modules.beheer.parameters.layout.instances.page2;

import static nl.procura.standard.Globalfunctions.toBigDecimal;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.gba.jpa.personen.db.App;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.applicatie.onderhoud.Application;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.window.Message;

public class Page2Instance extends NormalPageTemplate {

  private final Button       buttonConnection = new Button("Test verbinding (F3)");
  private final Application  app;
  private Page2InstanceForm1 form1            = null;
  private Page2InstanceForm2 form2            = null;

  public Page2Instance(Application app) {
    this.app = app;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonSave);
      addButton(buttonConnection, 1f);
      addButton(buttonClose);

      Page2InstanceBean bean = new Page2InstanceBean(app);
      form1 = new Page2InstanceForm1(bean);
      form2 = new Page2InstanceForm2(bean);
      addComponent(form1);
      addComponent(form2);
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

    form1.commit();
    form2.commit();

    app.getAttributes().setSyncUsers(form2.getBean().getSyncUsers());

    app.getEntity().setActive(toBigDecimal(form1.getBean().getActive() ? 1 : 0));
    app.getEntity().setName(form1.getBean().getName());
    app.getEntity().setUrl(form1.getBean().getUrl());
    app.getEntity().setUsername(form1.getBean().getUsername());
    app.getEntity().setAttr(app.getAttributes().save());

    getServices().getOnderhoudService().save(app);
    successMessage("De gegevens zijn opgeslagen");
  }

  private void testConnectie() {
    checkConnection();
    new Message(getWindow(), "Verbinding is gelukt.", Message.TYPE_SUCCESS);
  }

  private void checkConnection() {

    form1.commit();

    App testSync = new App();
    testSync.setUrl(form1.getBean().getUrl());
    testSync.setUsername(form1.getBean().getUsername());

    Application app = getServices().getOnderhoudService().getApp(testSync, true);
    if (!app.isLoginOk()) {
      throw new ProException("Verbinding is mislukt: " + app.getProblem());
    }
  }
}
