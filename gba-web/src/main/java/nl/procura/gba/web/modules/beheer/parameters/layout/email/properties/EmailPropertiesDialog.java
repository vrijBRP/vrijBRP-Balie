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

package nl.procura.gba.web.modules.beheer.parameters.layout.email.properties;

import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.zaken.ZakenModuleTemplate;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class EmailPropertiesDialog extends GbaModalWindow {

  public EmailPropertiesDialog() {
    super("E-mail eigenschappen (Escape om te sluiten)", "800px");
  }

  @Override
  public void attach() {

    super.attach();

    MainModuleContainer mainModule = new MainModuleContainer();

    addComponent(mainModule);

    Module module = new Module();

    mainModule.getNavigation().addPage(module);
  }

  @SuppressWarnings("unused")
  public void onSelect(String name) {
  }

  public class Module extends ZakenModuleTemplate {

    public Module() {
      setMargin(false);
    }

    @Override
    public void event(PageEvent event) {

      super.event(event);

      if (event.isEvent(InitPage.class)) {

        getPages().getNavigation().goToPage(new Page1EmailProperties());
      }
    }
  }

  public class Page1EmailProperties extends ButtonPageTemplate {

    private final GbaTable table;

    public Page1EmailProperties() {

      setInfo("Mogelijke extra eigenschappen e-mail");

      table = new GbaTable() {

        @Override
        public void onClick(Record record) {
          onSelect(record.getObject(String.class));
          super.onClick(record);
        }

        @Override
        public void setColumns() {

          setSelectable(true);

          addColumn("Naam", 200);
          addColumn("Type", 60);
          addColumn("Beschrijving");
          addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

          super.setColumns();
        }

        @Override
        public void setRecords() {

          try {

            addRecord("mail.smtp.user", "String", "Default user name for SMTP");
            addRecord("mail.smtp.port", "int",
                "The SMTP server port to connect to, if the connect() method doesn't explicitly specify one. Defaults to 25.");
            addRecord("mail.smtp.from", "String",
                "Email address to use for SMTP MAIL command. This sets the envelope return address. Defaults to msg.getFrom() or "
                    + "InternetAddress.getLocalAddress(). NOTE: mail.smtp.user was previously used for this.");

            addRecord("mail.smtp.localhost", "String",
                "Local host name used in the SMTP HELO or EHLO command. Defaults to InetAddress.getLocalHost().getHostName(). "
                    + "Should not normally need to be set if your JDK and your name service are configured properly.");

            addRecord("mail.smtp.localaddress", "String",
                "Local address (host name) to bind to when creating the SMTP socket. Defaults to the address picked by the Socket class. "
                    + "Should not normally need to be set, but useful with multi-homed hosts where it's important to pick a particular "
                    + "local address to bind to.");

            addRecord("mail.smtp.localport", "int",
                "Local port number to bind to when creating the SMTP socket. Defaults to the port number picked by the Socket class.");

            addRecord("mail.smtp.ehlo", "true/false",
                "If false, do not attempt to sign on with the EHLO command. Defaults to true. Normally failure of the EHLO command will "
                    + "fallback to the HELO command; this property exists only for servers that don't fail EHLO properly or "
                    + "don't implement EHLO properly");

            addRecord("mail.smtp.auth", "true/false",
                "If true, attempt to authenticate the user using the AUTH command. Defaults to false");

            addRecord("mail.smtp.auth.mechanisms", "String",
                "If set, lists the authentication mechanisms to consider, and the order in which to consider them. "
                    + "Only mechanisms supported by the server and supported by the current implementation will be used. "
                    + "The default is \"LOGIN PLAIN DIGEST-MD5 NTLM\", which includes all the authentication mechanisms "
                    + "supported by the current implementation");

            addRecord("mail.smtp.auth.ntlm.domain", "String", "The NTLM authentication domain.");
            addRecord("mail.smtp.auth.ntlm.flags", "int",
                "NTLM protocol-specific flags. See http://curl.haxx.se/rfc/ntlm.html#theNtlmFlags for details.");
            addRecord("mail.smtp.submitter", "String",
                "The submitter to use in the AUTH tag in the MAIL FROM command. "
                    + "Typically used by a mail relay to pass along information about the original submitter of the message. "
                    + "See also the setSubmitter method of SMTPMessage. Mail clients typically do not use this");

            addRecord("mail.smtp.dsn.notify", "String",
                "The NOTIFY option to the RCPT command. Either NEVER, or some "
                    + "combination of SUCCESS, FAILURE, and DELAY (separated by commas)");

            addRecord("mail.smtp.dsn.ret", "String",
                "The RET option to the MAIL command. Either FULL or HDRS");
            addRecord("mail.smtp.allow8bitmime", "true/false",
                "If set to true, and the server supports the 8BITMIME extension, text parts of messages "
                    + "that use the \"quoted-printable\" or \"base64\" encodings are converted to use \"8bit\" "
                    + "encoding if they follow the RFC2045 rules for 8bit text.");

            addRecord("mail.smtp.sendpartial", "true/false",
                "If set to true, and a message has some valid and some invalid addresses, send the message anyway, "
                    + "reporting the partial failure with a SendFailedException. If set to false (the default), the message "
                    + "is not sent to any of the recipients if there is an invalid recipient address.");

            addRecord("mail.smtp.sasl.realm", "String", "The realm to use with DIGEST-MD5 authentication");
            addRecord("mail.smtp.quitwait", "true/false",
                "If set to false, the QUIT command is sent and the connection is immediately closed. "
                    + "If set to true (the default), causes the transport to wait for the response to the QUIT command");

            addRecord("mail.smtp.reportsuccess", "true/false",
                "If set to true, causes the transport to include an SMTPAddressSucceededException for each address "
                    + "that is successful. Note also that this will cause a SendFailedException to be thrown from the "
                    + "sendMessage method of SMTPTransport even if all addresses were correct and the message was sent "
                    + "successfully.");

            addRecord("mail.smtp.socketFactory", "SocketFactory",
                "If set to a class that implements the javax.net.SocketFactory interface, this class will be "
                    + "used to create SMTP sockets. Note that this is an instance of a class, not a name, and must "
                    + "be set using the put method, not the setProperty method.");

            addRecord("mail.smtp.socketFactory.class", "String",
                "If set, specifies the name of a class that implements the javax.net.SocketFactory interface. "
                    + "This class will be used to create SMTP sockets");

            addRecord("mail.smtp.socketFactory.fallback", "String",
                "If set to true, failure to create a socket using the specified socket "
                    + "factory class will cause the socket to be created using the java.net.Socket class. Defaults to true.");

            addRecord("mail.smtp.socketFactory.port", "int",
                "Specifies the port to connect to when using the specified socket factory. "
                    + "If not set, the default port will be used");

            addRecord("mail.smtp.ssl.enable", "true/false",
                "If set to true, use SSL to connect and use the SSL port by default. "
                    + "Defaults to false for the \"smtp\" protocol and true for the \"smtps\" protocol");

            addRecord("mail.smtp.ssl.checkserveridentity", "true/false",
                "If set to true, check the server identity as specified by RFC 2595. "
                    + "These additional checks based on the content of the server's certificate are intended "
                    + "to prevent man-in-the-middle attacks. " + "Defaults to false.");

            addRecord("mail.smtp.ssl.trust", "String",
                "If set, and a socket factory hasn't been specified, enables use of a MailSSLSocketFactory. "
                    + "If set to \" * \", all hosts are trusted. If set to a whitespace separated list of hosts, "
                    + "those hosts are trusted. Otherwise, trust depends on the certificate the server presents.");

            addRecord("mail.smtp.ssl.socketFactory", "SSLSocketFactory",
                "If set to a class that extends the javax.net.ssl.SSLSocketFactory class, "
                    + "this class will be used to create SMTP SSL sockets. Note that this is an instance of a class, not a name, "
                    + "and must be set using the put method, not the setProperty method.");

            addRecord("mail.smtp.ssl.socketFactory.class", "String",
                "If set, specifies the name of a class that extends the javax.net.ssl.SSLSocketFactory class. "
                    + "This class will be used to create SMTP SSL sockets.");

            addRecord("mail.smtp.ssl.socketFactory.port", "int",
                "Specifies the port to connect to when using the specified socket factory. "
                    + "If not set, the default port will be used.");

            addRecord("mail.smtp.ssl.protocols", "String",
                "Specifies the SSL protocols that will be enabled for SSL connections. "
                    + "The property value is a whitespace separated list of tokens acceptable to the "
                    + "javax.net.ssl.SSLSocket.setEnabledProtocols method.");

            addRecord("mail.smtp.ssl.ciphersuites", "String",
                "Specifies the SSL cipher suites that will be enabled for SSL connections. "
                    + "The property value is a whitespace separated list of tokens acceptable to the "
                    + "javax.net.ssl.SSLSocket.setEnabledCipherSuites method.");

            addRecord("mail.smtp.mailextension", "String",
                "Extension string to append to the MAIL command. "
                    + "The extension string can be used to specify standard SMTP service extensions as well as vendor-specific extensions. "
                    + "Typically the application should use the SMTPTransport method supportsExtension to verify that the server "
                    + "supports the desired service extension. See RFC 1869 and other RFCs that define specific extensions.");

            addRecord("mail.smtp.starttls.enable", "true/false",
                "If true, enables the use of the STARTTLS command (if supported by the server) "
                    + "to switch the connection to a TLS-protected connection before issuing any login commands. "
                    + "Note that an appropriate trust store must configured so that the client will trust the server's "
                    + "certificate. Defaults to false.");

            addRecord("mail.smtp.starttls.required", "true/false",
                "If true, requires the use of the STARTTLS command. "
                    + "If the server doesn't support the STARTTLS command, or the command fails, the connect method will fail. "
                    + "Defaults to false.");

            addRecord("mail.smtp.userset", "true/false",
                "If set to true, use the RSET command instead of the NOOP command in the isConnected method. "
                    + "In some cases sendmail will respond slowly after many NOOP commands; use of RSET avoids this sendmail issue. "
                    + "Defaults to false.");

            addRecord("mail.smtp.noop.strict", "true/false",
                "If set to true (the default), insist on a 250 response code from the NOOP command to indicate success. "
                    + "The NOOP command is used by the isConnected method to determine if the connection is still alive. "
                    + "Some older servers return the wrong response code on success, some servers don't implement the NOOP command "
                    + "at all and so always return a failure code. Set this property to false to handle servers that are broken "
                    + "in this way. Normally, when a server times out a connection, it will send a 421 response code, "
                    + "which the client will see as the response to the next command it issues. "
                    + "Some servers send the wrong failure response code when timing out a connection. Do not set this property to "
                    + "false when dealing with servers that are broken in this way.");
          } catch (Exception e) {
            getApplication().handleException(getWindow(), e);
          }
        }

        private void addRecord(String naam, String type, String beschrijving) {

          Record r = addRecord(naam);
          r.addValue(naam);
          r.addValue(type);
          r.addValue(beschrijving);
        }
      };

      addComponent(table);
    }
  }
}
