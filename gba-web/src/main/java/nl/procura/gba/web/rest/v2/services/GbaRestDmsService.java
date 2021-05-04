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

package nl.procura.gba.web.rest.v2.services;

import static nl.procura.gba.web.services.zaken.documenten.DocumentVertrouwelijkheid.ONBEKEND;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import nl.procura.gba.web.rest.v2.model.zaken.GbaRestZaakDocumentToevoegenVraag;
import nl.procura.gba.web.rest.v2.model.zaken.GbaRestZaakDocumentenZoekenAntwoord;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakDocument;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakDocumentVertrouwelijkheid;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSContent;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSDocument;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSFileContent;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSService;
import nl.procura.gba.web.services.zaken.documenten.DocumentService;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.documenten.DocumentVertrouwelijkheid;
import nl.procura.gba.web.services.zaken.documenten.printen.DocumentenPrintenService;

public class GbaRestDmsService {

  private static final BiMap<GbaRestZaakDocumentVertrouwelijkheid, DocumentVertrouwelijkheid> VERTROUWELIJKHEID_MAP = HashBiMap
      .create();

  static {
    // this fails when both enums are not the same intentionally, so missing matches are noticed 
    for (DocumentVertrouwelijkheid value : DocumentVertrouwelijkheid.values()) {
      VERTROUWELIJKHEID_MAP.put(GbaRestZaakDocumentVertrouwelijkheid.valueOf(value.name()), value);
    }
  }

  private final DMSService         dmsService;
  private final DocumentService    documentService;
  private final Services           services;
  private final GbaRestZaakService zaakService;

  public GbaRestDmsService(Services services, GbaRestZaakService zaakService) {
    this.services = services;
    this.zaakService = zaakService;
    this.dmsService = services.getDmsService();
    this.documentService = services.getDocumentService();
  }

  public GbaRestZaakDocumentenZoekenAntwoord getDocumentsByZaakId(String zaakId) {
    GbaRestZaakDocumentenZoekenAntwoord antwoord = new GbaRestZaakDocumentenZoekenAntwoord();
    Zaak zaak = zaakService.getZaakByZaakId(zaakId);
    List<DMSDocument> docs = dmsService.getDocumentsByZaak(zaak).getDocuments();
    if (docs != null && !docs.isEmpty()) {
      List<GbaRestZaakDocument> documenten = new ArrayList<>();
      for (DMSDocument doc : docs) {
        documenten.add(toGbaRestZaakDocument(doc));
      }
      antwoord.setDocumenten(documenten);
    }
    return antwoord;
  }

  public DMSContent getDocumentByZaakId(String zaakId, String id) {
    Zaak zaak = zaakService.getZaakByZaakId(zaakId);
    List<DMSDocument> docs = dmsService.getDocumentsByZaak(zaak).getDocuments();
    for (DMSDocument doc : docs) {
      if (Objects.equals(id, documentId(doc))) {
        return doc.getContent();
      }
    }
    return null;
  }

  public GbaRestZaakDocument addDocument(String zaakId, GbaRestZaakDocumentToevoegenVraag request) throws IOException {
    Zaak zaak = zaakService.getZaakByZaakId(zaakId);
    File bestand = DocumentenPrintenService.newTijdelijkBestand(request.getDocument().getBestandsnaam());
    IOUtils.write(request.getInhoud(), new FileOutputStream(bestand));
    DMSDocument dmsDocument = toDmsDocument(request.getDocument(), bestand);
    DMSDocument document = dmsService.save(zaak, dmsDocument);
    return toGbaRestZaakDocument(document);
  }

  private DMSDocument toDmsDocument(GbaRestZaakDocument document, File bestand) {
    if (document == null) {
      throw new IllegalArgumentException("document is verplicht");
    }
    String bestandsnaam = document.getBestandsnaam();
    if (bestandsnaam == null) {
      throw new IllegalArgumentException("document bestandsnaam is verplicht");
    }

    DocumentVertrouwelijkheid vertrouwelijkheid = documentService
        .getStandaardVertrouwelijkheid(VERTROUWELIJKHEID_MAP.get(document.getVertrouwelijkheid()), ONBEKEND);

    return DMSDocument.builder(DMSFileContent.from(bestand))
        .title(defaultIfBlank(document.getTitel(), bestandsnaam))
        .confidentiality(vertrouwelijkheid.getNaam())
        .user(services.getGebruiker().getNaam())
        .datatype(DocumentType.ONBEKEND.getType())
        .build();
  }

  private static GbaRestZaakDocument toGbaRestZaakDocument(DMSDocument dmsDocument) {
    GbaRestZaakDocument document = new GbaRestZaakDocument();
    document.setId(documentId(dmsDocument));
    document.setTitel(dmsDocument.getTitle());
    document.setBestandsnaam(dmsDocument.getContent().getFilename());
    DocumentVertrouwelijkheid vertrouwelijkheid = DocumentVertrouwelijkheid.get(dmsDocument.getConfidentiality());
    document.setVertrouwelijkheid(VERTROUWELIJKHEID_MAP.inverse().get(vertrouwelijkheid));
    document.setPad("");
    document.setInvoerGebruiker(dmsDocument.getUser());
    document.setInvoerDatum((int) dmsDocument.getDate());
    document.setInvoerTijd((int) dmsDocument.getTime());
    return document;
  }

  private static String documentId(DMSDocument document) {
    return Base64.getEncoder().encodeToString(document.getContent().getFilename().getBytes());
  }
}
