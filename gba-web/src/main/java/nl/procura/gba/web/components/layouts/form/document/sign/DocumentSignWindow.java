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

import java.util.List;
import java.util.function.Consumer;

import nl.procura.gba.web.components.layouts.form.document.PrintRecord;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.beheer.zynyo.SignedDocument;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;

public class DocumentSignWindow extends GbaModalWindow {

  private final List<SignedDocument> signedDocuments;
  private final InitiateDocumentSignPage page;

  public DocumentSignWindow(Zaak zaak, PrintRecord record, List<SignedDocument> signedDocuments) {
    super("Ondertekenen", "800px");

    this.signedDocuments = signedDocuments;
    this.page = new InitiateDocumentSignPage(zaak, record, signedDocuments, request -> {
      SignedDocument signedDocument = new SignedDocument();
      signedDocument.name(record.getDocument().getDocument());
      signedDocument.documentUUID(request.documentUUID());
      signedDocument.documentContent("");
      this.signedDocuments.add(signedDocument);

      updateSignedDocuments(this.signedDocuments);
    });
  }

  public void setOnClose(Consumer<List<SignedDocument>> consumer) {
    this.page.setOnClose(consumer);
  }

  public void updateSignedDocuments(List<SignedDocument> signedDocuments) {
    this.page.reloadTable(signedDocuments);
  }

  @Override
  public void attach() {
    super.attach();

    addComponent(this.page);
  }
}
