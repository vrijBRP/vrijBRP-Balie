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

package nl.procura.gba.web.services.zaken.documenten.aanvragen;

import static java.util.Arrays.asList;

import java.util.HashSet;
import java.util.Set;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;

/**
 * Extensie van Zaakargumenten
 */
public class DocumentZaakArgumenten extends ZaakArgumenten {

  private Set<DocumentType> documentTypes = new HashSet<>();

  public DocumentZaakArgumenten() {
  }

  public DocumentZaakArgumenten(BasePLExt pl, DocumentType... documentTypes) {
    super(pl, ZaakType.UITTREKSEL);
    setDocumentTypes(documentTypes);
  }

  public Set<DocumentType> getDocumentTypes() {
    return documentTypes;
  }

  public void setDocumentTypes(DocumentType... documentTypes) {
    getDocumentTypes().clear();
    getDocumentTypes().addAll(asList(documentTypes));
  }

  public void setDocumentTypes(Set<DocumentType> documentTypes) {
    this.documentTypes = documentTypes;
  }
}
