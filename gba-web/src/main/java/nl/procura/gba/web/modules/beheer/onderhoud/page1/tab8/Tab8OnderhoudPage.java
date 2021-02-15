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

package nl.procura.gba.web.modules.beheer.onderhoud.page1.tab8;

import static nl.procura.gba.web.common.session.SessionSelectType.*;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import com.vaadin.Application;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.Terminal;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;

import nl.procura.gba.web.common.session.*;
import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.onderhoud.OnderhoudTabPage;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTableFilterLayout;
import nl.procura.vaadin.functies.UserAgentInfo;

public class Tab8OnderhoudPage extends OnderhoudTabPage {

  private final Table        table         = new Table();
  private final Button       buttonRefresh = new Button("Herlaad de informatie (F3)");
  private final Sessions     sessions      = new Sessions();
  private SessionSelectField sessionSelect;

  public Tab8OnderhoudPage() {
    super("Sessies");
    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      sessionSelect = new SessionSelectField() {

        @Override
        protected void onChange(SessionSelectType value) {
          reload();
        }
      };

      reload();

      buttonDel.setCaption("Sessies stoppen (F8)");

      addButton(buttonRefresh);
      addButton(buttonDel);
      addComponent(new Fieldset("Actuele sessies"));
      addExpandComponent(table);

      buttonRefresh.setWidth("170px");
      buttonDel.setWidth("170px");
      sessionSelect.setWidth("170px");

      getButtonLayout().addComponent(sessionSelect);
      getButtonLayout().addComponent(new IndexedTableFilterLayout(table));
      getButtonLayout().setComponentAlignment(sessionSelect, Alignment.MIDDLE_LEFT);
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (isKeyCode(button, keyCode, KeyCode.F3, buttonRefresh)) {
      reload();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onDelete() {

    new InvalidateProcedure(table, true);

    super.onDelete();
  }

  private void addVaadinProperties(HttpSession httpSession, Session session) {
    Enumeration<String> an = httpSession.getAttributeNames();
    while (an.hasMoreElements()) {
      String key = an.nextElement();
      Object val = httpSession.getAttribute(key);
      if (val instanceof WebApplicationContext) {
        WebApplicationContext wctx = (WebApplicationContext) val;
        for (Application application : wctx.getApplications()) {
          Terminal terminal = application.getMainWindow().getTerminal();
          if (terminal instanceof WebBrowser) {
            WebBrowser browser = (WebBrowser) terminal;
            session.setUserAddress(browser.getAddress());
            session.setUserOs(getOs(browser));
            session.setUserAgent(getUserAgent(browser));
            session.setUserAgentVersion(getUserAgentVersion(browser));

            Object user = application.getUser();
            if (user instanceof Gebruiker) {
              session.setUserName(((Gebruiker) user).getNaam());
            }
          }
        }
      }
    }
  }

  private String getOs(WebBrowser browser) {
    return (browser != null) ? new UserAgentInfo(browser.getBrowserApplication()).getOs() : "Onbekend";
  }

  private String getUserAgent(WebBrowser browser) {
    if (browser != null) {
      return new UserAgentInfo(browser.getBrowserApplication()).getUserAgent();
    }
    return "Onbekend";
  }

  private String getUserAgentVersion(WebBrowser browser) {
    if (browser != null) {
      return new UserAgentInfo(browser.getBrowserApplication()).getUserAgentVersion();
    }
    return "";
  }

  private void reload() {

    sessions.setInfo(HttpSessionStorage.getInfo());
    sessions.getActive().clear();
    for (HttpSession httpSession : HttpSessionStorage.getHttpSessions()) {
      Session session = SessionUtils.getSession(httpSession);
      sessions.getActive().add(session);
      addVaadinProperties(httpSession, session);
    }

    table.init();
  }

  private class InvalidateProcedure extends DeleteProcedure<Session> {

    public InvalidateProcedure(GbaTable table, boolean askAll) {
      super(table, askAll);
    }

    @Override
    protected void afterDelete() {
      reload();
    }

    @Override
    protected void deleteValue(Session session) {
      for (HttpSession httpSession : HttpSessionStorage.getHttpSessions()) {
        if (session.getId().equals(httpSession.getId())) {
          httpSession.invalidate();
        }
      }
    }

    @Override
    protected void updateMessages() {

      MELDING_GEEN_RECORDS_OM_TE_VERWIJDEREN = "Er zijn geen sessies om te stoppen";
      RECORD_VERWIJDEREN = "De {0} sessie stoppen?";
      RECORDS_VERWIJDEREN = "De {0} sessies stoppen?";
      RECORD_IS_VERWIJDERD = "De sessie is gestopt";
      RECORDS_ZIJN_VERWIJDERD = "De sessies zijn gestopt";
    }
  }

  private class Table extends GbaTable {

    public Table() {
      setSelectable(true);
      setMultiSelect(true);
    }

    @Override
    public void setColumns() {

      addColumn("Gebruiker", 200);
      addColumn("Duur van sessie", 200);
      addColumn("Inactief", 200);
      addColumn("IP-adres", 150);
      addColumn("Platform", 150);
      addColumn("Browser");
    }

    @Override
    public void setRecords() {

      try {
        for (Session session : sessions.getActive()) {

          boolean loggedIn = LOGGED_IN.equals(sessionSelect.getValue()) && fil(session.getUserName());
          boolean notLoggedIn = NOT_LOGGED_IN.equals(sessionSelect.getValue()) && emp(session.getUserName());
          boolean all = ALL.equals(sessionSelect.getValue());

          if (loggedIn || notLoggedIn || all) {
            Record record = addRecord(session);
            record.addValue(getUserName(session));
            record.addValue(session.getDurationFormatted());
            record.addValue(session.getLastAccessedTimeFormatted());
            record.addValue(session.getUserAddress());
            record.addValue(getOs(session));
            record.addValue(getUserAgent(session));
          }
        }
      } catch (Exception e) {
        getApplication().handleException(e);
      }

      super.setRecords();
    }

    private Object getOs(Session session) {
      if (fil(session.getUserOs())) {
        return session.getUserOs();
      }
      return "";
    }

    private String getUserAgent(Session session) {
      if (fil(session.getUserAgent())) {
        return session.getUserAgent() + " " + session.getUserAgentVersion();
      }
      return "";
    }

    private String getUserName(Session session) {
      if (fil(session.getUserName())) {
        return session.getUserName();
      }
      return "(Nog) niet ingelogd";
    }
  }
}
