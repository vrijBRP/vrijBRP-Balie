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

package nl.procura.gbaws.requests;

import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.emp;

import org.slf4j.LoggerFactory;

import nl.procura.gbaws.db.handlers.UsrDao;
import nl.procura.gbaws.db.wrappers.RequestWrapper;
import nl.procura.gbaws.db.wrappers.UsrWrapper;
import nl.procura.gbaws.web.servlets.RequestException;
import nl.procura.standard.ProcuraDate;

public abstract class RequestHandler {

  private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(RequestHandler.class);

  private final Logger logger = new Logger();
  private String       username;
  private String       password;
  private UsrWrapper   gebruiker;

  public RequestHandler(UsrWrapper gebruiker) {
    this.gebruiker = gebruiker;
  }

  public RequestHandler(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public void execute() {

    try {
      logger.chapter("Zoekactie");
      logger.item("Gebruikersnaam", username);
      LOG.debug(username + ": " + password);

      checkLogin(username, password);
      find();

    } catch (final IllegalStateException e) {
      LOG.debug("ILLEGAL STATE" + "\n" + e.toString());

    } catch (final RuntimeException e) {
      handleException(e);

    } finally {
      try {
        sendBack();
      } catch (final RuntimeException e) {
        handleException(e);
      }

      try {
        store();
      } catch (final RuntimeException e) {
        handleException(e);
      }
    }
  }

  public Logger getLogger() {
    return logger;
  }

  public UsrWrapper getGebruiker() {
    return gebruiker;
  }

  protected void handleException(Throwable t) {
    LOG.error(t.getMessage(), t);
  }

  protected void store() {

    final StringBuilder content = new StringBuilder();
    final RequestWrapper requestWrapper = new RequestWrapper();

    for (final String s : getLogger().getLoglines()) {
      content.append(s);
      content.append("\n");
    }

    try {

      if (getGebruiker() != null) {
        requestWrapper.setGebruiker(getGebruiker());
      }

      final ProcuraDate date = new ProcuraDate();
      requestWrapper.setDatumIngang(aval(date.getSystemDate()));
      requestWrapper.setTijdIngang(aval(date.getSystemTime()));
      requestWrapper.setInhoud(content.toString());
      requestWrapper.mergeAndCommit();
    } catch (final RuntimeException e) {
      LOG.debug(e.toString());
    }
  }

  protected void sendBack() {
  }

  protected abstract void find();

  private void checkLogin(String username, String password) {

    if (gebruiker == null) {

      if (emp(username) && emp(password)) {
        throw new RequestException(1001, "Geen gebruikersnaam/wachtwoord");
      }

      gebruiker = UsrDao.getUser(username, password);

      if (getGebruiker() == null) {
        throw new RequestException(1002, "Gebruikersnaam/wachtwoord incorrect");
      }

      if (!getGebruiker().isProfiel()) {
        throw new RequestException(1003, "Gebruiker heeft geen profiel. Incomplete configuratie.");
      }
    }
  }
}
