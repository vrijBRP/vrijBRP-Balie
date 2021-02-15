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

package nl.procura.gbaws.mail;

import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.fil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

import org.apache.log4j.Logger;

public class Email {

  public static final String TYPE_TEST     = "test";
  public static final String TYPE_FOUT_WW  = "fout_ww";
  public static final String TYPE_NIEUW_WW = "nieuw_ww";

  public static final String  MAIL_SMTP_HOST              = "mail.smtp.host";
  public static final String  MAIL_SMTP_PORT              = "mail.smtp.port";
  public static final String  MAIL_SMTP_USER              = "mail.smtp.user";
  public static final String  MAIL_SMTP_CONNECTIONTIMEOUT = "mail.smtp.connectiontimeout";
  public static final String  MAIL_SMTP_TIMEOUT           = "mail.smtp.timeout";
  public static final String  MAIL_SMTP_AUTH              = "mail.smtp.auth";
  public static final String  MAIL_SMTP_AUTH_MECHANISMS   = "mail.smtp.auth.mechanisms";
  private static final Logger logger                      = Logger.getLogger(Email.class);

  private String     type       = "";
  private String     from       = "";
  private String     replyTo    = "";
  private String     recipients = "";
  private String     subject    = "";
  private String     body       = "";
  private Properties properties = new Properties();

  public Email() {

    properties.put("mail.smtp.connectiontimeout", "30000");
    properties.put("mail.smtp.timeout", "30000");
  }

  protected void addLine(String line) {
    setBody(getBody() + line + "\n");
  }

  protected void addNewLine() {
    setBody(getBody() + "\n");
  }

  public void setProperty(String key, String value) {
    getProperties().put(key, value);
  }

  public void send() {

    check();

    try {

      final Session session = Session.getDefaultInstance(getProperties());
      final Message msg = new MimeMessage(session);

      msg.setFrom(new InternetAddress(getFrom()));
      msg.setReplyTo(new Address[]{ new InternetAddress(getReplyTo()) });

      String[] recipients = getRecipients().split(",|\r|\n");
      for (String recipient : recipients) {
        if (fil(recipient)) {
          if (msg.getAllRecipients() == null) {
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.trim()));
          } else {
            msg.addRecipient(Message.RecipientType.CC, new InternetAddress(recipient.trim()));
          }
        }
      }

      msg.setSubject(getSubject());

      final BodyPart bodypart = new MimeBodyPart();
      bodypart.setText(getBody());

      final Multipart mp = new MimeMultipart();
      mp.addBodyPart(bodypart);
      msg.setContent(mp);

      Transport.send(msg);

      logger.info("E-mail sent: " + this);
    } catch (final MessagingException e) {
      throw new RuntimeException(e);
    }
  }

  private void checkEmail(String s) {

    try {
      new InternetAddress(s).validate();
    } catch (final AddressException e) {
      throw new RuntimeException("E-mail adres " + s + " is incorrect.");
    }
  }

  public void check() {

    if (emp(getFrom())) {
      throw new RuntimeException("Geen afzender e-mail ingevuld.");
    }

    checkEmail(getFrom());

    if (emp(getReplyTo())) {
      throw new RuntimeException("Geen e-mail voor beantwoording ingevuld.");
    }

    checkEmail(getReplyTo());

    if (emp(getSubject())) {
      throw new RuntimeException("Geen onderwerp ingevuld");
    }

    if (emp(getBody())) {
      throw new RuntimeException("Geen inhoud ingevuld");
    }

    boolean ontvanger = false;
    for (final String s : getRecipients().split(",")) {
      if (fil(s)) {
        checkEmail(s);
        ontvanger = true;
      }
    }

    if (!ontvanger) {
      throw new RuntimeException("Geen ontvanger ingevuld");
    }
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getReplyTo() {
    return replyTo;
  }

  public void setReplyTo(String replyTo) {
    this.replyTo = replyTo;
  }

  public String getRecipients() {
    return recipients;
  }

  public void setRecipients(String recipients) {
    this.recipients = recipients;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public void setProperties(String properties) {

    try {
      getProperties().load(new ByteArrayInputStream(properties.getBytes()));
    } catch (final IOException e) {
      throw new RuntimeException("De properties kunnen niet worden ingelezen.");
    }
  }

  public Properties getProperties() {
    return properties;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  @Override
  public String toString() {

    return "Email [from=" + from + ", replyTo=" + replyTo + ", recipients=" + recipients + ", subject=" + subject + "]";
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
