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

package nl.procura.gba.web.common.login;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import javax.servlet.http.HttpServletRequest;

import com.vaadin.terminal.ErrorMessage;
import com.vaadin.terminal.UserError;
import com.vaadin.terminal.gwt.server.WebBrowser;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.account.wachtwoord.pages.passwordExpired.PasswordExpired;
import nl.procura.gba.web.modules.account.wachtwoord.pages.passwordExpired.PasswordExpiredWindow;
import nl.procura.gba.web.services.applicatie.onderhoud.OnderhoudService;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.GebruikerService;
import nl.procura.gba.web.services.beheer.link.LinkService;
import nl.procura.gba.web.services.beheer.link.PersonenLink;
import nl.procura.gba.web.services.beheer.link.PersonenLinkProperty;
import nl.procura.gba.web.services.beheer.link.PersonenLinkType;
import nl.procura.proweb.rest.guice.misc.TicketMap;
import nl.procura.proweb.rest.v1_0.gebruiker.ProRestGebruiker;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.functies.UserAgentInfo;
import nl.procura.vaadin.theme.Credentials;
import nl.procura.vaadin.theme.twee.login.CookieLoginValidator;

public class GBALoginValidator extends CookieLoginValidator {

  private ProRestGebruiker ticketGebruiker;
  private GbaApplication   application;

  public GBALoginValidator(GbaApplication gbaApplication) {
    super("wdwid9wa9d9ufdyw7guge8rhjswvljkhdwavfef", "05684tnvbi5tg43kcbbe3hg7dg37d3");
    setApplication(gbaApplication);
  }

  @Override
  public GbaApplication getApplication() {
    return application;
  }

  public void setApplication(GbaApplication application) {
    this.application = application;
  }

  @Override
  public Credentials loadCredentials() {

    if (checkLink()) {
      return null;
    }

    String ticket = astr(application.getParameterMap().get(OnderhoudService.TICKET));
    if (fil(ticket)) {
      ticketGebruiker = TicketMap.getGebruikerByMd5(ticket, getApplication().getSession().getId());
      if (ticketGebruiker != null) {
        return new Credentials(ticketGebruiker.getGebruikersnaam(), "");
      }
    }

    // Normale inlogprocedure
    return super.loadCredentials();
  }

  @Override
  public ErrorMessage validate(Credentials credentials) {

    GebruikerService gebruikers = getApplication().getServices().getGebruikerService();

    try {
      HttpServletRequest request = getApplication().getHttpRequest();
      String remoteAddress = request.getRemoteAddr().trim();

      Gebruiker gebruiker;
      if (ticketGebruiker != null) {
        gebruiker = gebruikers.getGebruikerByNaam(ticketGebruiker.getGebruikersnaam(), false);
      } else {
        gebruiker = gebruikers.getGebruikerByCredentials(getBrowser(), remoteAddress, credentials, false);
      }

      if (gebruiker != null) {
        if (gebruiker.isWachtwoordVerlopen()) {
          wijzigWachtwoord(new PasswordExpired(gebruiker, null, gebruikers, null));
        } else {
          validated(gebruiker);
        }
      }
    } catch (ProException e) {
      if (e.getSeverity() != INFO) {
        e.printStackTrace();
      }
      return new UserError(e.getMessage());
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      return new UserError(e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      return new UserError("Er is een onbekende fout opgetreden.");
    }

    return null;
  }

  private boolean checkLink() {

    LinkService links = getApplication().getServices().getLinkService();
    GebruikerService gebruikers = getApplication().getServices().getGebruikerService();

    String linkId = astr(getApplication().getParameterMap().get("link"));

    if (fil(linkId)) {

      PersonenLink link = links.getById(linkId);

      if (link == null) {

        Throwable exception = new ProException(WARNING, "Deze link bestaat niet (meer).");
        getApplication().handleException(getApplication().getLoginWindow(), exception);
      } else if (link.isVerlopen()) {

        Throwable exception = new ProException(WARNING, "Deze link is verlopen.");
        getApplication().handleException(getApplication().getLoginWindow(), exception);
      } else if (link.getLinkType() == PersonenLinkType.WACHTWOORD_RESET) {
        Gebruiker gebruiker = gebruikers.getGebruikerByNaam(
            link.getProperty(PersonenLinkProperty.GEBRUIKER.getCode()), false);

        if (gebruiker != null) {
          wijzigWachtwoord(new PasswordExpired(gebruiker, link, gebruikers, links));
        }
      }

      return true;
    }

    return false;
  }

  private String getBrowser() {
    WebBrowser browser = (WebBrowser) getApplication().getCurrentWindow().getTerminal();
    if (browser != null) {
      UserAgentInfo info = new UserAgentInfo(browser.getBrowserApplication());
      return info.getUserAgent() + " " + info.getUserAgentVersion();
    }
    return "Onbekend";
  }

  private void wijzigWachtwoord(PasswordExpired passwordExpired) {
    getApplication().getLoginWindow().addWindow(new PasswordExpiredWindow(passwordExpired));
  }
}
