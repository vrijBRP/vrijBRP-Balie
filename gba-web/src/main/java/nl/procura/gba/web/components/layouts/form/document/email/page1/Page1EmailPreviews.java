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

package nl.procura.gba.web.components.layouts.form.document.email.page1;

import static nl.procura.gba.web.common.misc.email.EmailAddressType.FROM;
import static nl.procura.gba.web.common.misc.email.EmailAddressType.REPLY_TO;
import static nl.procura.gba.web.components.layouts.form.document.email.page1.Page1EmailPreviewsBean.ONDERWERP;
import static nl.procura.gba.web.components.layouts.form.document.email.page1.Page1EmailPreviewsBean.SJABLOON;
import static nl.procura.gba.web.services.zaken.contact.ContactgegevensService.EMAIL;
import static nl.procura.gba.web.services.zaken.documenten.BestandType.PDF;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.openesignforms.ckeditor.CKEditorConfig;
import org.vaadin.openesignforms.ckeditor.CKEditorTextField;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.TextArea;

import nl.procura.gba.web.common.misc.email.*;
import nl.procura.gba.web.components.layouts.form.document.PrintRecord;
import nl.procura.gba.web.components.layouts.form.document.email.page1.keuze.PrintEmailKeuzeWindow;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.beheer.email.EmailTemplate;
import nl.procura.gba.web.services.beheer.email.EmailType;
import nl.procura.gba.web.services.beheer.email.EmailTypeContent;
import nl.procura.gba.web.services.zaken.algemeen.AbstractZaakContactService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakService;
import nl.procura.gba.web.services.zaken.algemeen.contact.ContactZaak;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoon;
import nl.procura.gba.web.services.zaken.algemeen.dms.DmsService;
import nl.procura.gba.web.services.zaken.documenten.BestandType;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintActie;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.window.Message;

/**
 * Aantekening
 */
public class Page1EmailPreviews extends NormalPageTemplate {

  private final List<PrintRecord>     records;
  private final EmailPreviewContainer container        = new EmailPreviewContainer();
  private CKEditorTextField           htmlContentArea  = null;
  private TextArea                    plainContentArea = null;
  private Page1EmailPreviewsForm      form             = null;
  private Page1EmailBijlageTable      attachmentTable  = null;
  private Page1EmailAdresTable        emailsTable      = null;

  public Page1EmailPreviews(List<PrintRecord> records) {

    this.records = records;
    setSpacing(true);
    setSizeFull();

    H2 h2 = new H2("E-mailen");
    buttonNext.setCaption("Versturen (F2)");

    addButton(buttonNext);
    addButton(buttonClose);

    getButtonLayout().setWidth("100%");
    getButtonLayout().addComponent(h2, 0);
    getButtonLayout().setExpandRatio(h2, 1f);
    getButtonLayout().setWidth("100%");
    getButtonLayout().setExpandRatio(h2, 1f);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form = new Page1EmailPreviewsForm();
      attachmentTable = new Page1EmailBijlageTable(records);
      emailsTable = new Page1EmailAdresTable(container);

      container.setTable(emailsTable);
      container.setZaakAdressen(getZaakEmailAdressen());

      buttonNew.addListener(this);
      buttonDel.addListener(this);

      HLayout buttonLayout = new HLayout(buttonNew, buttonDel);

      //J-
      VLayout emailAdressLayout = new VLayout().add(new Fieldset("E-mailadressen"))
          .add(buttonLayout)
          .add(emailsTable)
          .heightFull();

      emailAdressLayout.setExpandRatio(emailsTable, 1f);

      VLayout attachmentLayout = new VLayout().add(new Fieldset("Sjablonen en bijlagen"))
          .add(new Fieldset(form))
          .addExpandComponent(attachmentTable)
          .width("450px")
          .heightFull();

      HLayout topLayout = new HLayout().add(attachmentLayout).add(emailAdressLayout).widthFull().height("225px");

      topLayout.setExpandRatio(emailAdressLayout, 1f);
      //J+

      addComponent(topLayout);
      addComponent(new Fieldset("Inhoud"));

      CKEditorConfig config = new CKEditorConfig();
      config.setWriterIndentationChars("");
      config.setEnterMode(3);
      config.useCompactTags();
      config.disableElementsPath();
      config.setResizeDir(CKEditorConfig.RESIZE_DIR.HORIZONTAL);
      config.disableSpellChecker();
      config.setToolbarCanCollapse(true);
      config.setWidth("100%");

      plainContentArea = new TextArea();
      plainContentArea.setWidth("100%");
      plainContentArea.setHeight("100%");

      htmlContentArea = new CKEditorTextField(config);
      htmlContentArea.setWidth("100%");
      htmlContentArea.setHeight("100%");

      setContentArea(EmailTypeContent.HTML, "");

      ProNativeSelect templateField = form.getField(SJABLOON, ProNativeSelect.class);
      List<EmailTemplate> templates = getApplication().getServices()
          .getEmailService()
          .getTemplates(EmailType.DOCUMENT);

      templateField.setContainerDataSource(new EmailTemplateContainer(templates));
      templateField.addListener((ValueChangeListener) event1 -> {

        Object value = event1.getProperty().getValue();
        String van = "";
        String antwoordNaar = "";
        String onderwerp = "";
        String inhoud = "";
        EmailTypeContent typeContent = EmailTypeContent.HTML;

        if (value != null) {

          EmailTemplate template = (EmailTemplate) value;
          van = template.getVan();
          antwoordNaar = template.getAntwoordNaar();
          onderwerp = template.getOnderwerp();
          inhoud = template.getInhoud();
          typeContent = template.getTypeContent();
        }

        form.getField(ONDERWERP).setValue(onderwerp);
        setContentArea(typeContent, inhoud);

        container.getAdressen().reset();
        container.getAdressen().add(new EmailAddress(FROM, "", van));
        container.getAdressen().add(new EmailAddress(REPLY_TO, "", antwoordNaar));

        emailsTable.init();
      });
    }

    super.event(event);
  }

  public Zaak getZaak(List<PrintRecord> records) {
    for (PrintRecord record : records) {
      if (record.getZaak() != null) {
        return record.getZaak();
      }
    }
    return null;
  }

  /**
   * Geef de contactpersonen terug van de zaak
   */
  private EmailAddressList getZaakEmailAdressen() {

    EmailAddressList emailAdressen = new EmailAddressList();
    Zaak zaak = getZaak(records);

    if (zaak != null) {

      ZaakService db = getServices().getZakenService().getService(zaak);
      if (db instanceof AbstractZaakContactService && zaak instanceof ContactZaak) {

        ContactZaak contactZaak = (ContactZaak) zaak;
        AbstractZaakContactService zaakContactService = (AbstractZaakContactService) db;
        ZaakContact contact = zaakContactService.getContact(contactZaak);

        for (ZaakContactpersoon persoon : contact.getPersonen()) {

          String functie = persoon.getType().getDescr();
          String naam = persoon.getNaam();
          String email = persoon.getGegeven(EMAIL);

          emailAdressen.add(new EmailAddress(EmailAddressType.TO, functie, naam, email));
        }
      }
    }
    return emailAdressen;
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
    super.onClose();
  }

  @Override
  public void onDelete() {

    List<EmailAddress> adressen = emailsTable.getSelectedValues(EmailAddress.class);

    if (adressen.size() > 0) {
      for (EmailAddress adres : adressen) {
        container.getAdressen().remove(adres);
      }

      emailsTable.init();
    } else {
      throw new ProException(ProExceptionSeverity.INFO, "Geen e-mailadressen geselecteerd");
    }

    super.onDelete();
  }

  @Override
  public void onNew() {
    getParentWindow().addWindow(new PrintEmailKeuzeWindow(container, new EmailAddress()));
    super.onNew();
  }

  @Override
  public void onNextPage() {

    form.commit();
    Page1EmailPreviewsBean bean = form.getBean();

    Email email = getServices().getEmailService().getEmailParameters(new Email());
    email.setAdresses(container.getAdressen());
    email.setSubject(bean.getOnderwerp());
    email.setHtml(isHtmlContent());
    email.addLine(astr(isHtmlContent() ? htmlContentArea.getValue() : plainContentArea.getValue()));

    ArrayList<PrintRecord> printRecords = attachmentTable.getAllValues(PrintRecord.class);

    for (PrintRecord record : printRecords) {

      String fileName = record.getDocument().getEmailDocument(PDF);
      byte[] content = record.getPreviewArray();
      email.addAttachment(new EmailAttachment(fileName, content));
    }

    email.check();
    email.send();

    storeAsAttachment(email, printRecords);

    new Message(getParentWindow(), "De e-mail is verstuurd", Message.TYPE_SUCCESS);

    onClose();

    super.onNextPage();
  }

  private void setContentArea(EmailTypeContent typeContent, String content) {

    removeComponent(htmlContentArea);
    removeComponent(plainContentArea);

    if (typeContent == EmailTypeContent.HTML) {
      htmlContentArea.setValue(content);
      addComponent(htmlContentArea);
      setExpandRatio(htmlContentArea, 1f);
    } else {
      plainContentArea.setValue(content);
      addComponent(plainContentArea);
      setExpandRatio(plainContentArea, 1f);
    }

    getWindow().setHeight("80%");
    getWindow().center();
  }

  private boolean isHtmlContent() {
    return htmlContentArea.getParent() != null;
  }

  /**
   * E-mail info opslaan als bijlage bij de zaak of persoon
   */
  private void storeAsAttachment(Email email, ArrayList<PrintRecord> printRecords) {

    String id = "";
    String zaakId = "";
    String datatype = "";
    String titel = "";
    String dmsNaam = "";

    for (PrintRecord record : printRecords) {

      PrintActie printActie = record.getPrintActie();
      Zaak zaak = printActie.getZaak();

      if (zaak != null) {
        if (zaak.isStored()) {
          id = zaak.getZaakId();
          zaakId = zaak.getZaakId();
        } else {
          id = zaak.getBurgerServiceNummer().getStringValue();
        }

        datatype = printActie.getDocument().getType();
        dmsNaam = printActie.getDocument().getDmsNaam();
        String documentType = printActie.getDocument().getDocumentType().getOms().toLowerCase();
        titel = "E-mail " + documentType;
      }
    }

    if (fil(id)) {

      DmsService dms = getServices().getDmsService();
      String gebruiker = getServices().getGebruiker().getNaam();

      byte[] bytes = new EmailSummary(email, zaakId, gebruiker, printRecords).get().getBytes();
      String ext = BestandType.TXT.getType();

      dms.save(bytes, titel, ext, gebruiker, datatype, id, zaakId, dmsNaam, "");
    }
  }
}
