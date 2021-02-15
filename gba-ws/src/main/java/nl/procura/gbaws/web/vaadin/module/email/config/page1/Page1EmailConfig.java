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

package nl.procura.gbaws.web.vaadin.module.email.config.page1;

import static nl.procura.gbaws.web.vaadin.module.email.config.page1.Page1EmailConfigBean.*;
import static nl.procura.standard.Globalfunctions.fil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

import nl.procura.gbaws.db.handlers.EmailConfigDao;
import nl.procura.gbaws.db.wrappers.EmailConfigWrapper;
import nl.procura.gbaws.mail.Email;
import nl.procura.gbaws.web.vaadin.module.auth.ModuleAuthPage;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1EmailConfig extends ModuleAuthPage {

  private Page1EmailConfigForm form1 = null;
  private Page1EmailConfigForm form2 = null;

  public Page1EmailConfig() {
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonSave);

      form1 = new Page1EmailConfigForm() {

        @Override
        protected void init() {

          setCaption("Wanneer moet er een mail gestuurd worden?");
          setOrder(CHECK_WRONG_PW, CHECK_NEW_PW);
          setColumnWidths("200px", "");
        }
      };

      form2 = new Page1EmailConfigForm() {

        @Override
        protected void init() {

          setCaption("E-mail configuratie");
          setOrder(PROPERTIES, EMAIL_FROM, EMAIL_REPLYTO, EMAIL_RECIPIENTS);
          setColumnWidths("200px", "");
        }
      };

      Page1EmailConfigBean bean = new Page1EmailConfigBean();

      EmailConfigWrapper config = EmailConfigDao.getConfig();

      bean.setCheckWrongPassword(config.isTypeFoutWW());
      bean.setCheckNewPassword(config.isTypeNieuwWW());

      bean.setProperties(config.getTable().getProperties());
      bean.setEmailFrom(config.getTable().getEmailFrom());
      bean.setEmailReplyTo(config.getTable().getEmailReply());
      bean.setEmailRecipients(config.getTable().getEmailRecipients());

      form1.setBean(bean);
      form2.setBean(bean);

      addComponent(form1);
      addComponent(form2);
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().removeOtherPages();
    getNavigation().goToPage(Page1EmailConfig.class);
  }

  @Override
  public void onSave() {

    form1.commit();
    form2.commit();

    Page1EmailConfigBean bean = form1.getBean();

    EmailConfigWrapper config = EmailConfigDao.getConfig();

    config.getTable().setCEmailConfig(1);
    config.getTable().setEmailFrom(bean.getEmailFrom());
    config.getTable().setEmailReply(bean.getEmailReplyTo());
    config.getTable().setEmailRecipients(bean.getEmailRecipients());

    String types = "";

    if (bean.isCheckWrongPassword()) {
      types = " " + Email.TYPE_FOUT_WW;
    }
    if (bean.isCheckNewPassword()) {
      types += " " + Email.TYPE_NIEUW_WW;
    }

    config.getTable().setTypes(types.trim());

    if (isPropertiesCorrect(bean)) {
      config.getTable().setProperties(bean.getProperties());
    }

    config.mergeAndCommit();

    successMessage("Gegevens zijn opgeslagen");

    super.onSave();
  }

  private boolean isPropertiesCorrect(Page1EmailConfigBean bean) {

    Properties properties = new Properties();
    if (fil(bean.getProperties())) {
      try {
        properties.load(new ByteArrayInputStream(bean.getProperties().getBytes()));
        return true;
      } catch (final IOException e) {
        throw new RuntimeException("De andere eigenschappen zijn incorrect geformatteerd");
      }
    }

    return true;
  }
}
