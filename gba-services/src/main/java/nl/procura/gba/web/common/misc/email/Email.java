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

package nl.procura.gba.web.common.misc.email;

import static nl.procura.gba.web.common.misc.email.EmailAddressType.*;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionType;

public class Email {

  private static final String         TIMEOUT                     = "30000";
  private static final String         MAIL_SMTP_HOST              = "mail.smtp.host";
  private static final String         MAIL_SMTP_PORT              = "mail.smtp.port";
  private static final String         MAIL_SMTP_CONNECTIONTIMEOUT = "mail.smtp.connectiontimeout";
  private static final String         MAIL_SMTP_TIMEOUT           = "mail.smtp.timeout";
  private static final String         MAIL_HTML                   = "mail.content.type";
  private final List<EmailAttachment> attachments                 = new ArrayList<>();
  private String                      username                    = "";
  private String                      password                    = "";
  private String                      subject                     = "";
  private StringBuilder               body                        = new StringBuilder();
  private Properties                  properties                  = new Properties();
  private EmailAddressList            adresses                    = new EmailAddressList();

  public Email() {

    properties.put(MAIL_SMTP_CONNECTIONTIMEOUT, TIMEOUT);
    properties.put(MAIL_SMTP_TIMEOUT, TIMEOUT);
  }

  public void addAttachment(EmailAttachment attachment) {
    attachments.add(attachment);
  }

  public void addBreak() {
    add("\n");
  }

  public void addLine(String content) {
    if (fil(body.toString())) {
      body.append("\n");
    }
    add(content);
  }

  public void addProperties(String properties) {

    try {
      this.properties.load(new ByteArrayInputStream(properties.getBytes()));
    } catch (Exception e) {
      throw new ProException(WARNING, "De properties kunnen niet worden ingelezen.");
    }
  }

  /**
   * Controleer de e-mail
   */
  public void check() {

    check(getSubject(), "Geen onderwerp ingevuld");
    check(body.toString(), "Geen inhoud ingevuld");

    check(getAdresses().get(FROM), "Geen e-mailadres van de afzender ingevuld", true);
    check(getAdresses().get(TO), "Geen e-mailadres voor de ontvanger ingevuld", true);
    check(getAdresses().get(REPLY_TO), "Geen e-mailadres als beantwoorder ingevuld", true);
    check(getAdresses().get(CC), "", false);
    check(getAdresses().get(BCC), "", false);
  }

  public EmailAddressList getAdresses() {
    return adresses;
  }

  public void setAdresses(EmailAddressList adresses) {
    this.adresses = adresses;
  }

  public StringBuilder getBody() {
    return body;
  }

  public void setBody(StringBuilder body) {
    this.body = body;
  }

  public Properties getProperties() {
    return properties;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  public String getProperty(String key) {
    return astr(properties.get(key));
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public boolean isHtml() {
    return isTru(properties.getProperty(MAIL_HTML));
  }

  public void setHtml(boolean isHtml) {
    setProperty(MAIL_HTML, isHtml ? "1" : "0");
  }

  public void replaceValue(String key, String value) {

    String content = body.toString();
    content = content.replaceAll("\\$\\{" + key + "\\}", value);
    body = new StringBuilder(content);
  }

  public void send() {

    check();

    Authenticator authenticator = null;

    if (fil(username) && fil(password)) {
      authenticator = new javax.mail.Authenticator() {

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(username, password);
        }
      };
    }

    Session session = Session.getInstance(properties, authenticator);
    Message msg = new MimeMessage(session);

    try {
      msg.setFrom(new InternetAddress(getAdresses().getToString(FROM)));

      for (EmailAddress email : getAdresses().get(EmailAddressType.TO)) {
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getNameAndEmail()));
      }

      for (EmailAddress email : getAdresses().get(EmailAddressType.CC)) {
        msg.addRecipient(Message.RecipientType.CC, new InternetAddress(email.getNameAndEmail()));
      }

      for (EmailAddress email : getAdresses().get(EmailAddressType.BCC)) {
        msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(email.getNameAndEmail()));
      }

      for (EmailAddress email : getAdresses().get(EmailAddressType.REPLY_TO)) {
        msg.setReplyTo(new Address[]{ new InternetAddress(email.getNameAndEmail()) });
        break;
      }

      msg.setSubject(getSubject());
      BodyPart bodypart = new MimeBodyPart();

      if (isHtml()) {
        bodypart.setContent(body.toString(), "text/html");
      } else {
        bodypart.setText(body.toString());
      }

      Multipart mp = new MimeMultipart();
      mp.addBodyPart(bodypart);

      for (EmailAttachment attachment : attachments) {

        MimeBodyPart mailAttachment = new MimeBodyPart();
        ByteArrayDataSource ds = new ByteArrayDataSource(attachment.getContent(), attachment.getFileType());
        mailAttachment.setDataHandler(new DataHandler(ds));
        mailAttachment.setFileName(attachment.getFileName());
        mp.addBodyPart(mailAttachment);
      }

      msg.setContent(mp);
      Transport.send(msg);
    } catch (MessagingException e) {
      throw new ProException(ProExceptionType.WEBSERVICE, "Probleem bij versturen e-mail", e);
    }
  }

  public void setHost(String host) {
    setProperty(MAIL_SMTP_HOST, host);
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setPort(String port) {
    setProperty(MAIL_SMTP_PORT, port);
  }

  public void setProperty(String key, String value) {
    if (fil(value)) {
      properties.put(key, value);
    }
  }

  public void setUsername(String username) {
    this.username = username;
  }

  protected void add(String content) {
    body.append(content);
  }

  private void check(List<EmailAddress> adresses, String message, boolean required) {
    for (EmailAddress adres : adresses) {
      if (adres.isOk()) {
        return;
      }
    }
    if (required) {
      throw new ProException(WARNING, message);
    }
  }

  private void check(String value, String message) {
    if (emp(value)) {
      throw new ProException(WARNING, message);
    }
  }

}
