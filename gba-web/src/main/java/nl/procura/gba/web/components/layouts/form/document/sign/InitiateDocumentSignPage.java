/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.components.layouts.form.document.sign;

import static nl.procura.standard.Globalfunctions.isTru;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.vaadin.ui.Button;

import nl.procura.burgerzaken.zynyo.api.SignDocumentRequestBody;
import nl.procura.burgerzaken.zynyo.api.model.AuthenticationMethod;
import nl.procura.burgerzaken.zynyo.api.model.DocumentInfo;
import nl.procura.burgerzaken.zynyo.api.model.Signatory;
import nl.procura.burgerzaken.zynyo.api.model.ZynyoSignDocumentRequest;
import nl.procura.burgerzaken.zynyo.api.model.ZynyoSignedDocument;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.form.document.PrintRecord;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.zynyo.SignedDocument;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.contact.PlContactgegeven;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.standard.security.Base64;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.window.Message;
import nl.procura.vaadin.functies.VaadinUtils;

public class InitiateDocumentSignPage extends NormalPageTemplate {

  private final Zaak                               zaak;
  private final PrintRecord                        record;
  private final SignedDocumentsTable               signedDocumentsTable;
  private final Consumer<ZynyoSignDocumentRequest> onDocumentSignRequested;
  private InitiateDocumentSignForm                 form;
  private Consumer<List<SignedDocument>>           onClose;

  private final Button buttonStartSigning            = new Button("Start ondertekenen");
  private final Button buttonRetrieveSignedDocuments = new Button("Ondertekende documenten ophalen");

  public InitiateDocumentSignPage(
      Zaak zaak,
      PrintRecord record,
      List<SignedDocument> signedDocuments,
      Consumer<ZynyoSignDocumentRequest> onDocumentSignRequested) {
    this.zaak = zaak;
    this.record = record;
    this.signedDocumentsTable = new SignedDocumentsTable(signedDocuments);
    this.onDocumentSignRequested = onDocumentSignRequested;
  }

  public void reloadTable(List<SignedDocument> signedDocuments) {
    signedDocumentsTable.setSignedDocuments(signedDocuments);
    signedDocumentsTable.clear();
    signedDocumentsTable.setRecords();
    signedDocumentsTable.reloadRecords();
  }

  public void setOnClose(Consumer<List<SignedDocument>> consumer) {
    this.onClose = consumer;
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      addButton(buttonStartSigning);
      addButton(buttonRetrieveSignedDocuments);
      addButton(buttonClose);

      setInfo("Document ondertekenen",
          "Na ondertekening wordt het ondertekende document op het ingevulde e-mailadres afgeleverd en als bijlage aan de zaak toegevoegd.");
      setSpacing(true);

      Optional<PlContactgegeven> contactgegevenEmail = Services.getInstance().getContactgegevensService()
          .getContactgegevens(Services.getInstance().getContactgegevensService().getCurrentContact())
          .stream()
          .filter((PlContactgegeven plContactgegeven) -> plContactgegeven.getContactgegeven()
              .isGegeven(ContactgegevensService.EMAIL))
          .findFirst();
      String defaultEmail = contactgegevenEmail.isPresent() ? contactgegevenEmail.get().getAant() : "";
      String personName = Services.getInstance().getPersonenWsService().getHuidige().getPersoon().getNaam()
          .getNaamNaamgebruikGeslachtsnaamVoornaamAanschrijf();
      String document = record.getDocument().getDocument();
      if (isTru(Services.getInstance().getParameterService().getParm(ParameterConstant.DOC_TOON_BESTAND))) {
        document += String.format(" (%d - %s)", record.getDocument().getCDocument(), record.getDocument().getBestand());
      }

      form = new InitiateDocumentSignForm(personName, document, defaultEmail);
      addComponent(form);

      addComponent(new Fieldset("Ondertekende documenten"));
      addComponent(signedDocumentsTable);
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonStartSigning) {
      try {
        initiateSignRequest();
      } finally {
        VaadinUtils.resetHeight(getWindow());
      }
    } else if (button == buttonRetrieveSignedDocuments) {
      retrieveSignedDocuments();
    }

    super.handleEvent(button, keyCode);
  }

  public void initiateSignRequest() {
    form.commit();

    if (Services.getInstance().getGebruiker().getLocatie().getZynyoDeviceId().isEmpty()) {
      new Message(getWindow(), "Locatie heeft geen Zynyo device ID gekoppeld", Message.TYPE_ERROR_MESSAGE);
      return;
    }

    SignDocumentRequestBody body = new SignDocumentRequestBody();
    DocumentInfo documentInfo = new DocumentInfo();
    documentInfo.setName(form.getBean().getDocument());

    Signatory signatory = new Signatory();
    signatory.setName(form.getBean().getPersonName());
    signatory.setEmail(form.getBean().getEmail());
    signatory.setLocale(form.getBean().getLanguage().getValue());
    AuthenticationMethod authenticationMethod = new AuthenticationMethod();
    authenticationMethod.setType("documentanchor");
    authenticationMethod.setOrdernumber(0);
    List<AuthenticationMethod> authenticationMethods = new ArrayList<>();
    authenticationMethods.add(authenticationMethod);
    signatory.setAuthenticationMethods(authenticationMethods);
    signatory.setPriority("DEFAULT");
    signatory.setSignatoryRole("SIGN");
    signatory.setDisableEmail(false);
    signatory.setDisableInvitation(true);
    signatory.setDisableStatusChange(true);
    List<Signatory> signatories = new ArrayList<>();
    signatories.add(signatory);

    body.documentInfo(documentInfo);
    body.signatories(signatories);
    body.reference(Integer.parseInt(Services.getInstance().getGebruiker().getLocatie().getZynyoDeviceId()));

    body.content(Base64.encodeBytes(Services.getInstance()
        .getDocumentService()
        .generateSignDocument(record.getModel(), record.getZaak(), record.getDocument())));

    try {
      ZynyoSignDocumentRequest signDocumentRequest = Services.getInstance().getZynyoService()
          .getSigningApi()
          .postSignDocumentRequest(body);
      onDocumentSignRequested.accept(signDocumentRequest);
    } catch (RuntimeException exception) {
      new Message(getWindow(),
          "Het ondertekenen kon niet worden gestart door een foutmelding in de communicatie met Zynyo",
          Message.TYPE_ERROR_MESSAGE);
      return;
    }

    new Message(getParentWindow(), "Ondertekenen gestart", Message.TYPE_SUCCESS);
  }

  public void retrieveSignedDocuments() {
    int index = 0;
    for (SignedDocument signedDocument : signedDocumentsTable.signedDocuments) {
      ++index;
      if (!signedDocument.documentContent().equals("")) {
        continue;
      }

      try {
        ZynyoSignedDocument zynyoSignedDocument = getApplication().getServices().getZynyoService().getSigningApi()
            .getSignedDocument(signedDocument.documentUUID());
        signedDocument.documentContent(zynyoSignedDocument.documentContent());
        Services.getInstance().getDmsService().saveSignedDocumentWithZaak(zaak, signedDocument, index);
        new Message(getParentWindow(), "Ondertekende documenten succesvol opgehaald", Message.TYPE_SUCCESS);
      } catch (RuntimeException exception) {
        new Message(getParentWindow(), "Ophalen van ondertekende document mislukt", Message.TYPE_ERROR_MESSAGE);
      }
    }

    signedDocumentsTable.clear();
    signedDocumentsTable.setRecords();
    signedDocumentsTable.reloadRecords();
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
    if (onClose != null) {
      onClose.accept(signedDocumentsTable.signedDocuments);
    }

    super.onClose();
  }

  public class SignedDocumentsTable extends GbaTable {

    public List<SignedDocument> signedDocuments;

    public SignedDocumentsTable(List<SignedDocument> records) {
      this.signedDocuments = records;
    }

    public void addSignedDocument(SignedDocument documentRecord) {
      Record r = addRecord(documentRecord);

      r.addValue(documentRecord.name());
      r.addValue(documentRecord.documentContent().isEmpty() ? "Wacht op ondertekening"
          : new TableImage(GbaWebTheme.ICOON_24.OK));
    }

    public void setSignedDocuments(List<SignedDocument> signedDocuments) {
      this.signedDocuments = signedDocuments;
    }

    @Override
    public void setColumns() {
      setSelectable(false);

      addColumn("Document");
      addColumn("Ondertekend en opgehaald");
    }

    @Override
    public void setRecords() {
      for (SignedDocument signedDocument : signedDocuments) {
        addSignedDocument(signedDocument);
      }
    }
  }
}
