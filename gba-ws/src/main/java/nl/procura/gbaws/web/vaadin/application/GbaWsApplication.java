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

package nl.procura.gbaws.web.vaadin.application;

import static nl.procura.standard.Globalfunctions.astr;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.Terminal;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.config.GbaConfigProperty;
import nl.procura.gbaws.web.vaadin.layouts.footer.LoginFooterLayout;
import nl.procura.gbaws.web.vaadin.login.GbaWsLoginValidator;
import nl.procura.gbaws.web.vaadin.windows.home.HomeWindow;
import nl.procura.vaadin.component.application.ApplicationRequestHandler;
import nl.procura.vaadin.component.window.WindowListener;
import nl.procura.vaadin.component.window.windowEvents.WindowInit;
import nl.procura.vaadin.theme.ProcuraApplication;
import nl.procura.vaadin.theme.ProcuraWindow;
import nl.procura.vaadin.theme.twee.window.LoginWindow3;

public class GbaWsApplication extends ProcuraApplication {

  public GbaWsApplication() {

    setTheme("gba-ws");

    setLoginEnabled(true);

    String gemeente = GbaConfig.get(GbaConfigProperty.GEMEENTE);
    LoginWindow3 lw = new LoginWindow3("vrijBRP", gemeente);
    lw.setCaption("vrijBRP | " + gemeente);
    lw.setFooterLayout(new LoginFooterLayout());
    lw.setLoginValidator(new GbaWsLoginValidator());
    lw.getLoginPanel().setRememberMe(true);

    setLoginWindow(lw);
  }

  @Override
  public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
    super.onRequestStart(request, response);
    new ApplicationRequestHandler(this);
  }

  @Override
  public void terminalError(Terminal.ErrorEvent event) {

    if (getMainWindow() != null) {
      ExceptionHandler.handle(getMainWindow(), event.getThrowable());
    } else {
      super.terminalError(event);
    }
  }

  @Override
  protected void loadWindows() {

    HomeWindow h = new HomeWindow();
    StringBuilder url = new StringBuilder(astr(getURL()) + astr(h.getName()));
    getLoginWindow().open(new ExternalResource(url.toString()));
    setMainWindow(h);
    ((WindowListener) h).event(new WindowInit());
  }

  public ProcuraWindow getParentWindow() {
    return getParentWindow(null);
  }

  /**
   * Geef parentWindow terug
   */
  public ProcuraWindow getParentWindow(ProcuraWindow window) {

    ProcuraWindow newWindow = (ProcuraWindow) (window != null ? window : getCurrentWindow());

    if (newWindow.getParent() != null) {
      return getParentWindow((ProcuraWindow) newWindow.getParent());
    }

    return newWindow;
  }
}
