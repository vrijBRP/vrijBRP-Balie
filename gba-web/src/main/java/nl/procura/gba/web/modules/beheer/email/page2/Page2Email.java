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

package nl.procura.gba.web.modules.beheer.email.page2;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.List;

import org.vaadin.openesignforms.ckeditor.CKEditorConfig;
import org.vaadin.openesignforms.ckeditor.CKEditorTextField;

import com.vaadin.ui.TextArea;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.beheer.email.EmailTemplate;
import nl.procura.gba.web.services.beheer.email.EmailType;
import nl.procura.gba.web.services.beheer.email.EmailTypeContent;
import nl.procura.gba.web.services.beheer.email.EmailVariables;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2Email extends NormalPageTemplate {

  private CKEditorTextField htmlTextArea  = null;
  private TextArea          plainTextArea = null;
  private Page2EmailForm    form          = null;
  private EmailTemplate     emailTemplate;

  public Page2Email(EmailTemplate emailTemplate) {

    super("Toevoegen / muteren e-mail sjablonen");

    this.emailTemplate = emailTemplate;

    addButton(buttonPrev, buttonNew, buttonSave);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form = new Page2EmailForm(emailTemplate) {

        @Override
        protected List<EmailType> getEmailTypes() {
          return getServices().getEmailService().getEmailTypes(emailTemplate);
        }

        @Override
        protected void onChangeTypeContent(EmailTypeContent type) {
          setTextArea(type);
        }
      };

      addComponent(form);

      CKEditorConfig config = new CKEditorConfig();
      config.setWriterIndentationChars("");
      config.setEnterMode(3);
      config.useCompactTags();
      config.disableElementsPath();
      config.setResizeDir(CKEditorConfig.RESIZE_DIR.HORIZONTAL);
      config.disableSpellChecker();
      config.setToolbarCanCollapse(false);
      config.setWidth("100%");

      plainTextArea = new TextArea();
      plainTextArea.setWidth("100%");
      plainTextArea.setHeight("300px");

      htmlTextArea = new CKEditorTextField(config);
      htmlTextArea.setHeight("400px");

      plainTextArea.setValue(astr(emailTemplate.getInhoud()));
      htmlTextArea.setValue(astr(emailTemplate.getInhoud()));

      setTextArea(emailTemplate.getTypeContent());
    }

    super.event(event);
  }

  @Override
  public void onNew() {

    this.emailTemplate = new EmailTemplate();

    form.reset();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    doSave();

    successMessage("De gegevens zijn opgeslagen.");
  }

  private void doSave() {

    form.commit();

    Page2EmailBean b = form.getBean();

    emailTemplate.setType(b.getType());
    emailTemplate.setTypeContent(b.getTypeContent());
    emailTemplate.setOnderwerp(b.getOnderwerp());
    emailTemplate.setVan(b.getVan());
    emailTemplate.setAntwoordNaar(b.getAntwoordNaar());
    emailTemplate.setBcc(b.getBcc());
    emailTemplate.setInhoud(getContent());
    emailTemplate.setGeactiveerd(b.getActiveren());
    emailTemplate.setGeldigheid(aval(b.getGeldig()));

    for (String wrongVariable : EmailVariables.getWrongVariables(b.getType(), getContent())) {
      throw new ProException(WARNING, "<b>" + wrongVariable + "</b> is geen geldige variabele");
    }

    getServices().getEmailService().save(emailTemplate);
  }

  private String getContent() {

    form.commit();

    if (form.getBean().getTypeContent() == EmailTypeContent.HTML) {
      return astr(htmlTextArea.getValue());
    }

    return astr(plainTextArea.getValue());
  }

  private void setTextArea(EmailTypeContent typeContent) {

    removeComponent(htmlTextArea);
    removeComponent(plainTextArea);

    if (typeContent == EmailTypeContent.HTML) {
      addComponent(htmlTextArea);
    } else {
      addComponent(plainTextArea);
    }
  }
}
