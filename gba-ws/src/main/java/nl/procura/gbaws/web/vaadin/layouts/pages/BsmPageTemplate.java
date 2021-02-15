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

package nl.procura.gbaws.web.vaadin.layouts.pages;

import nl.procura.gbaws.web.vaadin.application.ExceptionHandler;
import nl.procura.gbaws.web.vaadin.application.GbaWsApplication;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.PageLayout;
import nl.procura.vaadin.component.window.Message;
import nl.procura.vaadin.functies.VaadinUtils;
import nl.procura.vaadin.theme.ProcuraWindow;

public class BsmPageTemplate extends PageLayout {

  @Override
  public void attach() {

    try {
      super.attach();
    } catch (RuntimeException e) {
      ExceptionHandler.handle(getWindow(), e);
    }
  }

  public InfoLayout setInfo(String msg) {
    return setInfo("Ter informatie", msg);
  }

  public InfoLayout setInfo(String header, String msg) {
    InfoLayout info = new InfoLayout(header, msg);
    addComponent(info);
    return info;
  }

  public InfoLayout addInfo(String info) {
    InfoLayout infoLayout = VaadinUtils.getChild(this, InfoLayout.class);
    if (infoLayout != null) {
      infoLayout.append(info);
    }
    return infoLayout;
  }

  // Gooit een melding op
  protected void successMessage(String message) {
    new Message(getWindow(), message, Message.TYPE_SUCCESS);
  }

  // Geeft melding bij verwijderen record(s) in tabel
  protected void deleteMessage(int aantRec) {
    new Message(getWindow(), (aantRec > 1) ? "Records zijn verwijderd" : "Record is verwijderd",
        Message.TYPE_SUCCESS);
  }

  protected void infoMessage(String message) {
    new Message(getWindow(), message, Message.TYPE_INFO);
  }

  protected void warningMessage(String message) {
    new Message(getWindow(), message, Message.TYPE_WARNING_MESSAGE);
  }

  protected void errorMessage(String message) {
    new Message(getWindow(), message, Message.TYPE_ERROR_MESSAGE);
  }

  @Override
  public GbaWsApplication getApplication() {
    return (GbaWsApplication) super.getApplication();
  }

  public ProcuraWindow getParentWindow() {
    return getApplication().getParentWindow(getWindow());
  }

  @Override
  public ProcuraWindow getWindow() {
    return (ProcuraWindow) super.getWindow();
  }
}
