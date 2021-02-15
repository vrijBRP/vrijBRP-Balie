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

package nl.procura.gba.web.application;

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.*;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.NO_RESULTS;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.Terminal;
import com.vaadin.ui.Window;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.config.GbaConfigProperty;
import nl.procura.gba.web.common.exceptions.ExceptionHandler;
import nl.procura.gba.web.common.login.GBALoginValidator;
import nl.procura.gba.web.common.window.WindowHandler;
import nl.procura.gba.web.components.layouts.footer.GbaWebLoginFooterLayout;
import nl.procura.gba.web.modules.beheer.parameters.container.SchermopbouwtypeContainer;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenParameters;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.Services.TYPE;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.parameter.ParameterType;
import nl.procura.gba.web.services.beheer.persoonhistorie.PersoonHistorieType;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActieType;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.gba.web.windows.home.HomeWindow;
import nl.procura.gba.web.windows.persoonslijst.PersoonslijstWindow;
import nl.procura.gba.web.windows.verwijzing.VerwijzingWindow;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.window.Message;
import nl.procura.vaadin.component.window.windowEvents.WindowInit;
import nl.procura.vaadin.theme.ProcuraApplication;
import nl.procura.vaadin.theme.ProcuraWindow;
import nl.procura.vaadin.theme.twee.window.LoginWindow3;
import nl.procura.validation.Bsn;

public class GbaApplication extends ProcuraApplication {

  private static final ThreadLocal<GbaApplication> threadLocal = new ThreadLocal<>();
  private final GbaWindowNameStack                 windowNames = new GbaWindowNameStack();
  private final Services                           services;
  private final ProcessInterceptor                 process;

  public GbaApplication() {

    setTheme("gba-web");
    setLoginEnabled(true);

    // Met automatisch inloggen
    services = new Services(TYPE.PROWEB);
    process = new ProcessInterceptor();

    // Nieuw loginWindow
    GbaLoginWindow lw = new GbaLoginWindow(GbaConfig.get(GbaConfigProperty.GEMEENTE));
    lw.setFooterLayout(new GbaWebLoginFooterLayout());
    lw.setLoginValidator(new GBALoginValidator(this));
    lw.getLoginPanel().setRememberMe(isRememberMe());

    setLoginWindow(lw);
  }

  public static GbaApplication getInstance() {
    return threadLocal.get();
  }

  private static void setInstance(GbaApplication application) {
    threadLocal.set(application);
    Services.setInstance(application.getServices());
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

  public String getParmValue(ParameterType parameterType) {
    return getServices().getGebruiker().getParameters().get(parameterType).getValue();
  }

  public Window getPreviousWindow() {
    return getWindow(windowNames.getPrevious());
  }

  public ProcessInterceptor getProcess() {
    return process;
  }

  public Services getServices() {
    return services;
  }

  @Override
  @Deprecated
  public Gebruiker getUser() {
    return (Gebruiker) super.getUser();
  }

  @Override
  public void setUser(Object user) {
    services.setGebruiker((Gebruiker) user);
    super.setUser(user);
  }

  /**
   * Opent een window. Als deze al bestaat dan erheen
   */
  public void goBackToWindow(Window currentWindow, Window newWindow) {
    goBackToWindow(new WindowChangeListener(currentWindow, newWindow));
  }

  public void goBackToWindow(WindowChangeListener wcl) {

    ProcessChangeInterceptor processChangeInterceptor = new ProcessChangeInterceptor(wcl) {

      @Override
      protected void proceed(WindowChangeListener wcl) {
        proceedOpenWindow(wcl);
      }
    };

    process.intercept(processChangeInterceptor);
  }

  /**
   * Ga naar pl op basis van een eerder gezochte PL.
   */
  public void goToPl(Window currentWindow, String fragment, PLEDatasource databron, BasePLExt pl) {
    String nummer = pl.getPersoon().getNummer().getCode();
    goToPl(currentWindow, fragment, databron, nummer);
  }

  /**
   * Reload the current personlist
   */
  public void reloadCurrentPersonList() {
    Window currentWindow = getCurrentWindow();
    if (currentWindow instanceof PersoonslijstWindow) {
      PersoonslijstWindow plWindow = (PersoonslijstWindow) currentWindow;
      PersonenWsService personenWsService = getServices().getPersonenWsService();
      BasePLExt currentPL = personenWsService.getHuidige();
      if (currentPL != null) {
        // Removed cached PL, otherwise the PL is reloaded from cache
        getServices().getPersonenWsService().getOpslag().clear();
        // Reload PL
        String number = currentPL.getPersoon().getNummer().getVal();
        personenWsService.getPersoonslijst(true, currentPL.getDatasource(), number);
        String fragment = plWindow.getFragmentUtility().getFragment();
        // Reset the navigation layout
        plWindow.resetNavigatie(emp(fragment) ? "pl.overzicht" : fragment);
        new Message(currentWindow, "", "De persoonslijst is herladen ...", Message.TYPE_INFO);
      }
    }
  }

  public void goToPl(Window currentWindow, String fragment, PLEDatasource databron, String... nummers) {

    try {
      BasePLExt pl = getServices().getPersonenWsService().getPersoonslijst(true, databron, nummers);

      if (pl.getCats().isEmpty()) {
        throw new ProException(NO_RESULTS, WARNING,
            "De geselecteerde persoon is niet gevonden, ook niet in de verwijsgegevens");
      }

      if (pl.isToonBeperking()) {
        throw new ProException(NO_RESULTS, WARNING,
            "De persoon wordt niet getoond vanwege een verstrekkingsbeperking");
      }

      getServices().getPersoonHistorieService().change(pl,
          getServices().getGebruiker(),
          PersoonHistorieType.HISTORIE,
          true);

      if (currentWindow instanceof PersoonslijstWindow) {
        PersoonslijstWindow plWindow = (PersoonslijstWindow) currentWindow;
        plWindow.resetNavigatie(emp(fragment) ? "pl.overzicht" : fragment);

      } else {
        removeWindowIfNotCurrent(currentWindow, VerwijzingWindow.NAME);
        removeWindowIfNotCurrent(currentWindow, PersoonslijstWindow.NAME);

        if (pl.heeftVerwijzing()) {
          openWindow(currentWindow, new VerwijzingWindow(), "");

        } else {
          openWindow(currentWindow, new PersoonslijstWindow(), fragment);
        }
      }
    } catch (Exception e) {
      handleException(currentWindow, e);
    }
  }

  public void handleException(Throwable e) {
    handleException(getCurrentWindow(), e);
  }

  public void handleException(Window window, Throwable e) {
    ExceptionHandler.handle(window, e);
  }

  @Override
  public boolean isDynamicHeight() {
    String type = getServices().getParameterService().getParm(SCHERMOPBOUWTYPE);
    return !SchermopbouwtypeContainer.VAST.equals(type);
  }

  /**
   * Mag de huidige gebruiker deze actie doen?
   */
  public boolean isProfielActie(ProfielActie profielActie) {
    return getServices().getGebruiker().getProfielen().isProfielActie(profielActie);
  }

  public boolean isProfielActie(ProfielActieType type, ProfielActie profielActie) {
    return getServices().getGebruiker().getProfielen().isProfielActie(type, profielActie);
  }

  public void onHome() {
    getServices().getPersonenWsService().getOpslag().clear();
    openWindow(getCurrentWindow(), new HomeWindow(), "#reset");
  }

  @Override
  public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
    GbaApplication.setInstance(this);
    super.onRequestStart(request, response);
    new GbaApplicationRequestHandler(this);
  }

  public void openWindow(Window currentWindow, Window window) {
    openWindow(new WindowChangeListener(currentWindow, window, null, true));
  }

  public void openWindow(Window currentWindow, Window newWindow, String fragment) {
    openWindow(new WindowChangeListener(currentWindow, newWindow, fragment, true));
  }

  public void removePreviousWindow() {
    windowNames.removePrevious();
  }

  private void removeWindowIfNotCurrent(Window current, String name) {
    if (!current.getName().equals(name)) {
      removeWindow(getWindow(name));
    }
  }

  @Override
  public void removeWindow(Window window) {

    windowNames.remove(window);
    super.removeWindow(window);
  }

  /**
   * X-UA-Compatible (IE 10/11 only)
   */
  public void setLegacyMode() {
    String legacyMode = getServices().getParameterService().getSysteemParameter(X_UA_COMPATIBLE).getValue();
    getSession().setAttribute(X_UA_COMPATIBLE.getKey(), legacyMode);
  }

  @Override
  public void setMainWindow(Window window) {

    super.setMainWindow(window);
    windowNames.put(window);
  }

  @Override
  public void terminalError(Terminal.ErrorEvent event) {

    if (getCurrentWindow() != null) {
      handleException(getCurrentWindow(), event.getThrowable());
    } else {
      super.terminalError(event);
    }
  }

  public void closeAllModalWindows(Collection<Window> windows) {
    for (Window window : windows) {
      closeAllModalWindows(window.getChildWindows());
      if (window instanceof ModalWindow) {
        ((ModalWindow) window).closeWindow();
      }
    }
  }

  @Override
  protected void loadWindows() {

    getServices().init();

    setSessionTimeout();
    setLegacyMode();

    // Eerste window laden
    Bsn bsn = new Bsn(astr(getParameterMap().get("bsn")));
    String fragment = ((LoginWindow3) getLoginWindow()).getFragment();

    if (bsn.isCorrect()) {
      Window parentWindow = getWindows().isEmpty() ? getLoginWindow() : getWindows().iterator().next();
      goToPl(parentWindow, fragment, PLEDatasource.STANDAARD, bsn.getDefaultBsn());

    } else {
      // Sla zoekparameters op
      new ZakenParameters(this).storeParameters();
      Window w = new WindowHandler(this).getNextWindow();
      openWindow(getLoginWindow(), w, fragment);
    }
  }

  private boolean isRememberMe() {
    return isTru(getServices().getParameterService().getGebruikerParameters(Gebruiker.getDefault()).get(
        REMEMBER_ME).getValue());
  }

  /**
   * Opent een window
   */
  private void openWindow(WindowChangeListener wcl) {

    process.intercept(new ProcessChangeInterceptor(wcl) {

      @Override
      protected void proceed(WindowChangeListener wcl) {
        proceedOpenWindow(wcl);
      }
    });
  }

  private void proceedOpenWindow(WindowChangeListener wcl) {

    Window currentWindow = wcl.getCurrentWindow();
    Window newWindow = wcl.getNewWindow();
    boolean removeFirst = wcl.isRemoveFirst();
    String fragment = wcl.getFragment();

    if (!currentWindow.getName().equals(newWindow.getName()) && removeFirst) {
      removeAddedWindow(newWindow);
    }

    StringBuilder url = new StringBuilder(getExternalURL(newWindow.getName()));

    if (fil(fragment)) {

      if (!fragment.contains("#")) {
        url.append("#");
      }

      url.append(fragment);
    }

    wcl.windowChanged();

    boolean windowExists = getWindow(newWindow.getName()) != null;
    currentWindow.open(new ExternalResource(url.toString()));
    setMainWindow(newWindow);

    if (removeFirst || !windowExists) {
      if (newWindow instanceof GbaWindow) {
        ((GbaWindow) newWindow).event(new WindowInit());
      }
    }
  }

  /**
   * Verwijderd en voegt een nieuwe window toe.
   * Windows zijn in de GBA niet persistent.
   * Wordt steeds een nieuwe gemaakt.
   */
  private void removeAddedWindow(Window window) {

    Window w = getWindow(window.getName());

    if (w != null) {
      removeWindow(w);
    }

    addWindow(window);
  }

  /**
   * Timeout
   */
  private void setSessionTimeout() {

    int timeout = aval(getServices().getParameterService().getParm(SESSIE_TIMEOUT)) * 60;
    if (timeout >= 60) {
      getSession().setMaxInactiveInterval(timeout);
    }
  }
}
