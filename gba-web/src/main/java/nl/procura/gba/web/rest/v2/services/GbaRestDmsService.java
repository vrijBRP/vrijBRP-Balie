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
import java.io.IOException;
import java.nio.file.Files;
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
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSContent;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSDocument;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSFileContent;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.documenten.DocumentVertrouwelijkheid;
import nl.procura.gba.web.services.zaken.documenten.printen.DocumentenPrintenService;
import nl.procura.standard.ProcuraDate;

public class GbaRestDmsService extends GbaRestAbstractService {

  private static final BiMap<GbaRestZaakDocumentVertrouwelijkheid, DocumentVertrouwelijkheid> VERTROUWELIJKHEID_MAP = HashBiMap
      .create();

  static {
    // this fails when both enums are not the same intentionally, so missing matches are noticed 
    for (DocumentVertrouwelijkheid value : DocumentVertrouwelijkheid.values()) {
      VERTROUWELIJKHEID_MAP.put(GbaRestZaakDocumentVertrouwelijkheid.valueOf(value.name()), value);
    }
  }

  public GbaRestZaakDocumentenZoekenAntwoord getDocumentsByZaakId(String zaakId) {
    GbaRestZaakDocumentenZoekenAntwoord antwoord = new GbaRestZaakDocumentenZoekenAntwoord();
    Zaak zaak = getRestServices().getZaakService().getZaakByZaakId(zaakId);
    List<DMSDocument> docs = getServices().getDmsService().getDocumentsByZaak(zaak).getDocuments();
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
    Zaak zaak = getRestServices().getZaakService().getZaakByZaakId(zaakId);
    List<DMSDocument> docs = getServices().getDmsService().getDocumentsByZaak(zaak).getDocuments();
    for (DMSDocument doc : docs) {
      if (Objects.equals(id, documentId(doc))) {
        return doc.getContent();
      }
    }
    return null;
  }

  public GbaRestZaakDocument addDocument(String folderId, GbaRestZaakDocument document, byte[] content)
      throws IOException {
    File bestand = DocumentenPrintenService.newTijdelijkBestand(document.getBestandsnaam());
    IOUtils.write(content, Files.newOutputStream(bestand.toPath()));
    DMSDocument dmsDocument = toDmsDocument(document, bestand);
    dmsDocument.setZaakId(folderId);
    return toGbaRestZaakDocument(getServices().getDmsService().save(dmsDocument));
  }

  public GbaRestZaakDocument addZaakDocument(String zaakId, GbaRestZaakDocumentToevoegenVraag request)
      throws IOException {
    Zaak zaak = getRestServices().getZaakService().getZaakByZaakId(zaakId);
    File bestand = DocumentenPrintenService.newTijdelijkBestand(request.getDocument().getBestandsnaam());
    IOUtils.write(request.getInhoud(), Files.newOutputStream(bestand.toPath()));
    DMSDocument dmsDocument = toDmsDocument(request.getDocument(), bestand);
    return toGbaRestZaakDocument(getServices().getDmsService().save(zaak, dmsDocument));
  }

  private DMSDocument toDmsDocument(GbaRestZaakDocument document, File bestand) {
    if (document == null) {
      throw new IllegalArgumentException("document is verplicht");
    }
    String bestandsnaam = document.getBestandsnaam();
    if (bestandsnaam == null) {
      throw new IllegalArgumentException("document bestandsnaam is verplicht");
    }

    DocumentVertrouwelijkheid vertrouwelijkheid = getServices().getDocumentService()
        .getStandaardVertrouwelijkheid(VERTROUWELIJKHEID_MAP.get(document.getVertrouwelijkheid()), ONBEKEND);

    ProcuraDate date = new ProcuraDate();
    return DMSDocument.builder()
        .content(DMSFileContent.from(bestand))
        .title(defaultIfBlank(document.getTitel(), bestandsnaam))
        .confidentiality(vertrouwelijkheid.getNaam())
        .user(getServices().getGebruiker().getNaam())
        .date(Long.parseLong(date.getSystemDate()))
        .time(Long.parseLong(date.getSystemTime()))
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
