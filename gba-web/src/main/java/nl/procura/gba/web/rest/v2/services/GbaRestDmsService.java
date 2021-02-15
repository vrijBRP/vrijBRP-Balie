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
import nl.procura.gba.web.services.zaken.algemeen.dms.DmsDocument;
import nl.procura.gba.web.services.zaken.algemeen.dms.DmsService;
import nl.procura.gba.web.services.zaken.algemeen.dms.DmsStream;
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

  private final DmsService         dmsService;
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
    List<DmsDocument> docs = dmsService.getDocumenten(zaak).getDocumenten();
    if (docs != null && !docs.isEmpty()) {
      List<GbaRestZaakDocument> documenten = new ArrayList<>();
      for (DmsDocument doc : docs) {
        documenten.add(toGbaRestZaakDocument(doc));
      }
      antwoord.setDocumenten(documenten);
    }
    return antwoord;
  }

  public DmsStream getDocumentByZaakId(String zaakId, String id) {
    Zaak zaak = zaakService.getZaakByZaakId(zaakId);
    List<DmsDocument> docs = dmsService.getDocumenten(zaak).getDocumenten();
    for (DmsDocument doc : docs) {
      if (Objects.equals(id, documentId(doc))) {
        return dmsService.getBestand(doc);
      }
    }
    return null;
  }

  public GbaRestZaakDocument addDocument(String zaakId, GbaRestZaakDocumentToevoegenVraag request) throws IOException {
    Zaak zaak = zaakService.getZaakByZaakId(zaakId);
    DmsDocument dmsDocument = toDmsDocument(request.getDocument());
    File bestand = DocumentenPrintenService.newTijdelijkBestand(dmsDocument.getBestandsnaam());
    IOUtils.write(request.getInhoud(), new FileOutputStream(bestand));
    DmsDocument document = dmsService.save(zaak, bestand, dmsDocument);
    return toGbaRestZaakDocument(document);
  }

  private DmsDocument toDmsDocument(GbaRestZaakDocument document) {
    if (document == null) {
      throw new IllegalArgumentException("document is verplicht");
    }
    String bestandsnaam = document.getBestandsnaam();
    if (bestandsnaam == null) {
      throw new IllegalArgumentException("document bestandsnaam is verplicht");
    }
    DmsDocument dmsDocument = new DmsDocument();
    dmsDocument.setTitel(defaultIfBlank(document.getTitel(), bestandsnaam));
    dmsDocument.setBestandsnaam(bestandsnaam);
    DocumentVertrouwelijkheid vertrouwelijkheid = documentService
        .getStandaardVertrouwelijkheid(VERTROUWELIJKHEID_MAP.get(document.getVertrouwelijkheid()), ONBEKEND);
    dmsDocument.setVertrouwelijkheid(vertrouwelijkheid.getNaam());
    dmsDocument.setAangemaaktDoor(services.getGebruiker().getNaam());
    dmsDocument.setDatatype(DocumentType.ONBEKEND.getType());
    return dmsDocument;
  }

  private static GbaRestZaakDocument toGbaRestZaakDocument(DmsDocument dmsDocument) {
    GbaRestZaakDocument document = new GbaRestZaakDocument();
    document.setId(documentId(dmsDocument));
    document.setTitel(dmsDocument.getTitel());
    document.setBestandsnaam(dmsDocument.getBestandsnaam());
    DocumentVertrouwelijkheid vertrouwelijkheid = DocumentVertrouwelijkheid.get(dmsDocument.getVertrouwelijkheid());
    document.setVertrouwelijkheid(VERTROUWELIJKHEID_MAP.inverse().get(vertrouwelijkheid));
    document.setPad(dmsDocument.getPad());
    document.setInvoerGebruiker(dmsDocument.getAangemaaktDoor());
    document.setInvoerDatum((int) dmsDocument.getDatum());
    document.setInvoerTijd((int) dmsDocument.getTijd());
    return document;
  }

  private static String documentId(DmsDocument document) {
    return Base64.getEncoder().encodeToString(document.getBestandsnaam().getBytes());
  }

}
