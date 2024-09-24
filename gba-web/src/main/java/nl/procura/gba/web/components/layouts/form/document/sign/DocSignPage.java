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

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;

import nl.procura.burgerzaken.zynyo.api.model.AuthenticationMethod;
import nl.procura.burgerzaken.zynyo.api.model.DocumentInfo;
import nl.procura.burgerzaken.zynyo.api.model.SignDocumentResponse;
import nl.procura.burgerzaken.zynyo.api.model.SignSingleDocumentRequest;
import nl.procura.burgerzaken.zynyo.api.model.Signatory;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.form.document.PrintRecord;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.zynyo.SignedDocument;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.standard.security.Base64;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.window.Message;
import nl.procura.vaadin.theme.twee.Icons;

import lombok.Setter;

public class DocSignPage extends NormalPageTemplate {

  private final DocSignParameters    parameters;
  private final SignedDocumentsTable table;
  private DocSignForm                form;

  private final Button buttonStart    = new Button("Start ondertekenen");
  private final Button buttonRetrieve = new Button("Ondertekende documenten ophalen");

  public DocSignPage(DocSignParameters parameters) {
    this.parameters = parameters;
    this.table = new SignedDocumentsTable();
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      addButton(buttonStart);
      addButton(buttonRetrieve, 1F);
      addButton(buttonClose);

      setInfo("Document ondertekenen",
          "Na ondertekening wordt het ondertekende document op het ingevulde e-mailadres afgeleverd "
              + "<br/>en als bijlage aan de zaak toegevoegd.");
      setSpacing(true);

      if (parameters.getPrintRecords().isEmpty()) {
        throw new ProException(ERROR,
            "Er zijn geen documenten om te ondertekenen");
      }

      if (parameters.getPersons().isEmpty()) {
        throw new ProException(ERROR,
            "Er zijn geen ondertekenaars geselecteerd");
      }

      PrintRecord record = parameters.getPrintRecords().get(0);
      String document = record.getDocument().getDocument();
      int size = parameters.getPrintRecords().size();
      if (size > 1) {
        document = size + " documenten bij "
            + parameters.getPrintRecords().get(0).getZaak().getType().getOms().toLowerCase();
      }

      form = new DocSignForm(document, parameters.getPersons());
      addComponent(form);
      addComponent(new Fieldset("Ondertekende documenten"));
      addComponent(table);
    }

    super.event(event);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonStart) {
      initiateSignRequest();

    } else if (button == buttonRetrieve) {
      retrieveSignedDocuments();
    }

    super.handleEvent(button, keyCode);
  }

  public void initiateSignRequest() {
    form.commit();

    Services services = getApplication().getServices();
    if (services.getGebruiker().getLocatie().getZynyoDeviceId().isEmpty()) {
      new Message(getWindow(), "Locatie heeft geen Zynyo device ID gekoppeld", Message.TYPE_ERROR_MESSAGE);
      return;
    }

    if (form.getBean().getEmail()) {
      getApplication().getParentWindow().addWindow(new ConfirmDialog("Bevestiging kopie via e-mail",
          String.format("Het document wordt naar <b>%s</b> gestuurd. "
              + "<br/>Weet u zeker dat u door wilt gaan?",
              form.getBean().getPerson().getEmail()),
          "400px") {

        @Override
        public void buttonYes() {
          sendRequest(services);
          super.buttonYes();
        }
      });
    } else {
      sendRequest(services);
    }
  }

  private void sendRequest(Services services) {
    SignSingleDocumentRequest request = new SignSingleDocumentRequest();
    DocumentInfo documentInfo = new DocumentInfo();
    documentInfo.setName(form.getBean().getDocument());

    Signatory signatory = new Signatory();
    signatory.setName(form.getBean().getPerson().getName());
    signatory.setEmail(form.getBean().getPerson().getEmail());
    signatory.setLocale(form.getBean().getLanguage().getValue());

    List<AuthenticationMethod> authenticationMethods = new ArrayList<>();
    AuthenticationMethod authenticationMethod = new AuthenticationMethod();
    authenticationMethod.setType("documentanchor");
    authenticationMethod.setOrdernumber(0);
    authenticationMethods.add(authenticationMethod);
    signatory.setAuthenticationMethods(authenticationMethods);

    signatory.setPriority("DEFAULT");
    signatory.setSignatoryRole("SIGN");
    signatory.setDisableEmail(form.getBean().getEmail());
    signatory.setDisableInvitation(true);
    signatory.setDisableStatusChange(true);

    List<Signatory> signatories = new ArrayList<>();
    signatories.add(signatory);

    request.documentInfo(documentInfo);
    request.signatories(signatories);
    request.reference(Integer.parseInt(services
        .getGebruiker().getLocatie().getZynyoDeviceId()));

    List<PrintRecord> records = parameters.getPrintRecords();
    request.content(Base64.encodeBytes(services
        .getDocumentService()
        .generateSignDocument(records.stream()
            .map(PrintRecord::getPreviewArray)
            .collect(Collectors.toList()))));

    try {
      SignDocumentResponse response = services.getZynyoService()
          .getSigningApi()
          .postSignDocumentRequest(request);

      parameters.getSignedDocuments().add(new SignedDocument()
          .name(form.getBean().getDocument())
          .documentUUID(response.documentUUID()));

    } catch (RuntimeException exception) {
      throw new ProException(
          ERROR,
          "Het ondertekenen kon niet worden gestart door een foutmelding in de communicatie met Zynyo");
    }
    new Message(getParentWindow(), "Ondertekenen gestart", Message.TYPE_SUCCESS);
    table.init();
  }

  public void retrieveSignedDocuments() {
    for (SignedDocument signedDocument : parameters.getSignedDocuments()) {
      if (isNotBlank(signedDocument.documentContent())) {
        continue;
      }

      try {
        signedDocument.documentContent(getApplication().getServices().getZynyoService()
            .getSigningApi()
            .getSignedDocument(signedDocument.documentUUID())
            .documentContent());

        Zaak zaak = parameters.getPrintRecords().get(0).getZaak();
        getApplication().getServices().getDmsService().saveSignedDocumentWithZaak(zaak, signedDocument);
        new Message(getParentWindow(), "Ondertekende documenten succesvol opgehaald", Message.TYPE_SUCCESS);

      } catch (RuntimeException exception) {
        new Message(getParentWindow(), "Ophalen van ondertekende document mislukt", Message.TYPE_WARNING_MESSAGE);
      }
    }
    table.init();
  }

  public boolean hasUnretrievedDocuments() {
    return table.getAllValues(SignedDocument.class)
        .stream()
        .anyMatch(document -> document.documentContent()
            .isEmpty());
  }

  @Setter
  public class SignedDocumentsTable extends GbaTable {

    @Override
    public void setColumns() {
      setSelectable(false);
      addColumn("Document", 300);
      addColumn("&nbsp;", 20).setClassType(Embedded.class);
      addColumn("Ondertekend en opgehaald");
    }

    @Override
    public void setRecords() {
      for (SignedDocument documentRecord : parameters.getSignedDocuments()) {
        Record record = addRecord(documentRecord);
        boolean contentEmpty = documentRecord.documentContent().isEmpty();
        record.addValue(documentRecord.name());
        record.addValue(new TableImage(contentEmpty
            ? Icons.getIcon(Icons.ICON_WARN)
            : Icons.getIcon(Icons.ICON_OK)));
        record.addValue(contentEmpty
            ? "Ondertekening nog niet opgehaald"
            : "Ondertekend en opgeslagen");
      }
    }
  }
}
