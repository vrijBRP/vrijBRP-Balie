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

package nl.procura.gba.web.modules.beheer.parameters.layout;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.trim;

import java.io.InputStream;

import com.vaadin.ui.Button;
import com.vaadin.ui.TextArea;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.common.misc.email.EmailAddressType;
import nl.procura.gba.web.common.misc.email.EmailAttachment;
import nl.procura.gba.web.modules.beheer.parameters.layout.email.properties.EmailPropertiesDialog;
import nl.procura.gba.web.modules.beheer.parameters.layout.email.properties.EmailTest;
import nl.procura.gba.web.modules.beheer.parameters.layout.email.properties.EmailTestDialog;

public class EmailParameterLayout extends DatabaseParameterLayout {

  private final Button buttonSend         = new Button("Test e-mail verzenden");
  private final Button buttonProperties   = new Button("Toon mogelijke eigenschappen");
  private final String emailEigenschappen = "emailEigenschappen";

  public EmailParameterLayout(GbaApplication application, String naam, String category) {
    super(application, naam, category);
    addButton(buttonSend);
    addButton(buttonProperties);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonProperties) {

      getWindow().addWindow(new EmailPropertiesDialog() {

        @Override
        public void onSelect(String name) {

          TextArea field = getForm().getField(emailEigenschappen, TextArea.class);

          StringBuilder sb = new StringBuilder(astr(field.getValue()));
          sb.append("\n");
          sb.append(name);
          sb.append("=");

          field.setValue(trim(sb.toString()));

          successMessage(name + " is toegevoegd");

          closeWindow();
        }
      });
    }

    if (button == buttonSend) {

      getForm().commit();

      getWindow().addWindow(new EmailTestDialog() {

        @Override
        public void onSend(String ontvanger, String afzender, String beantwoorder) {

          EmailTest e = new EmailTest();

          String host = astr(getForm().getValue("emailServer"));
          String port = astr(getForm().getValue("emailPort"));
          String username = astr(getForm().getValue("emailGebruikersnaam"));
          String password = astr(getForm().getValue("emailWachtwoord"));
          String emailProperties = astr(getForm().getValue(emailEigenschappen));

          e.setHost(host);
          e.setPort(port);
          e.setUsername(username);
          e.setPassword(password);
          e.setSubject("Proweb personen test e-mail");
          e.addProperties(emailProperties);

          e.getAdresses().add(EmailAddressType.FROM, "", afzender);
          e.getAdresses().add(EmailAddressType.TO, "", ontvanger);
          e.getAdresses().add(EmailAddressType.REPLY_TO, "", beantwoorder);

          String path = "VAADIN/themes/gba-web/layouts/img/tested.jpg";
          InputStream is = EmailTestDialog.class.getClassLoader().getResourceAsStream(path);
          e.addAttachment(new EmailAttachment("getest.jpg", is));

          e.check();

          e.send();

          successMessage("E-mail is verstuurd naar " + ontvanger);

          closeWindow();
        }
      });
    }

    super.handleEvent(button, keyCode);
  }
}
