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

package nl.procura.gbaws.db.wrappers;

import static nl.procura.standard.Globalfunctions.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;

import nl.procura.gba.jpa.personenws.db.EmailLog;
import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;
import nl.procura.gbaws.db.handlers.EmailConfigDao;
import nl.procura.gbaws.mail.Email;
import nl.procura.standard.exceptions.ProException;

public class EmailLogWrapper extends AbstractTableWrapper<EmailLog, Integer> {

  public EmailLogWrapper() {
    super(new EmailLog());
  }

  public EmailLogWrapper(int code) {
    this(GbaWsJpa.getManager().find(EmailLog.class, code));
  }

  public EmailLogWrapper(EmailLog profile) {
    super(profile);
  }

  @Override
  public Integer getPk() {
    return getTable().getCEmailLog();
  }

  public void delete(EntityManager m) {
    m.remove(getTable());
  }

  public void deleteAndCommit() {

    final EntityManager m = GbaWsJpa.getManager();

    m.getTransaction().begin();
    final EmailLog log = m.find(EmailLog.class, getTable().getCEmailLog());

    m.remove(log);
    m.getTransaction().commit();
  }

  public void save(Email email) {

    try {
      getTable().setSubject(email.getSubject());
      getTable().setBody(email.getBody());
      getTable().setType(email.getType());
      getTable().setDIn(new BigDecimal(new Date().getTime()));
      getTable().setSent(new BigDecimal(0));
      getTable().setType(email.getType());

      saveAndCommit();
    } catch (final RuntimeException e) {
      throw new ProException("Opslaan is mislukt: " + e.getMessage(), e);
    }
  }

  public void send() {

    final EmailConfigWrapper configDao = EmailConfigDao.getConfig();
    final Email email = new Email();

    email.setFrom(configDao.getTable().getEmailFrom());
    email.setReplyTo(configDao.getTable().getEmailReply());
    email.setRecipients(configDao.getTable().getEmailRecipients());
    email.setProperties(configDao.getTable().getProperties());
    email.setSubject(getTable().getSubject());
    email.setBody(getTable().getBody());

    boolean sendOK = false;
    String error = "";

    try {
      email.check();
      try {
        email.send();
        sendOK = true;
      } catch (final RuntimeException e) {
        error = e.getMessage();
        throw new ProException("Versturen is mislukt: " + e.getMessage(), e);
      }
    } catch (final RuntimeException e) {
      error = e.getMessage();
      throw new ProException("Aanmaken van de e-mail is mislukt: " + e.getMessage(), e);
    } finally {
      saveAndCommit(sendOK, error);
    }
  }

  private void saveAndCommit(boolean sendOK, String error) {
    try {
      getTable().setSent(new BigDecimal(sendOK ? 1 : 0));
      getTable().setError(error);
      saveAndCommit();
    } catch (final RuntimeException e) {
      throw new ProException("Opslaan is mislukt: " + e.getMessage(), e);
    }
  }

  private void saveAndCommit() {
    final EntityManager m = GbaWsJpa.getManager();
    m.getTransaction().begin();
    setTable(merge(m, getTable()));
    m.getTransaction().commit();
  }

  public String getDateTime() {
    return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date(along(getTable().getDIn())));
  }

  public String getError() {
    final String err = getTable().getError();
    if (fil(err)) {
      return "<font style='color: red;'>" + err + "</font>";
    }
    return "<font style='color: green;'>Geen</font>";
  }

  public String getSentMessage() {
    if (pos(getTable().getSent())) {
      return "<font style='color: green;'>Reeds verzonden</font>";
    }
    return "<font style='color: red;'>Nog niet verzonden</font>";
  }

  public boolean isSent() {
    return pos(getTable().getSent());
  }
}
