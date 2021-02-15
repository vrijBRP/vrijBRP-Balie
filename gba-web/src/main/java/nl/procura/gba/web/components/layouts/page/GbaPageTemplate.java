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

package nl.procura.gba.web.components.layouts.page;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.common.exceptions.ExceptionHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.PageLayout;
import nl.procura.vaadin.component.window.Message;
import nl.procura.vaadin.functies.VaadinUtils;
import nl.procura.vaadin.theme.ProcuraWindow;

public class GbaPageTemplate extends PageLayout {

  public GbaPageTemplate() {
    setMargin(true);
  }

  @Override
  public void attach() {

    try {
      super.attach();
    } catch (Exception e) {
      ExceptionHandler.handle(getWindow(), e);
    }
  }

  @Override
  public GbaApplication getApplication() {
    return (GbaApplication) super.getApplication();
  }

  public ProcuraWindow getParentWindow() {
    return getApplication().getParentWindow(getWindow());
  }

  @Override
  public ProcuraWindow getWindow() {
    return (ProcuraWindow) super.getWindow();
  }

  public InfoLayout addInfo(String info) {
    InfoLayout infoLayout = VaadinUtils.getChild(this, InfoLayout.class);
    if (infoLayout != null) {
      infoLayout.append(info);
    }
    return infoLayout;
  }

  public InfoLayout setInfo(String msg) {
    return setInfo("Ter informatie", msg);
  }

  public InfoLayout setInfo(String header, String msg) {
    InfoLayout infoLayout = VaadinUtils.getChild(this, InfoLayout.class);
    if (infoLayout != null) {
      InfoLayout newInfoLayout = new InfoLayout(header, msg);
      replaceComponent(infoLayout, newInfoLayout);
      return newInfoLayout;
    } else {
      infoLayout = new InfoLayout(header, msg);
      addComponent(infoLayout);
    }
    return infoLayout;
  }

  protected void errorMessage(String message) {
    new Message(getWindow(), message, Message.TYPE_ERROR_MESSAGE);
  }

  protected BasePLExt getPl() {
    return getServices().getPersonenWsService().getHuidige();
  }

  protected Services getServices() {
    return getApplication().getServices();
  }

  protected void infoMessage(String message) {
    new Message(getWindow(), message, Message.TYPE_INFO);
  }

  // Gooit een melding op
  protected void successMessage(String message) {
    new Message(getWindow(), message, Message.TYPE_SUCCESS);
  }

  protected void warningMessage(String message) {
    new Message(getWindow(), message, Message.TYPE_WARNING_MESSAGE);
  }
}
