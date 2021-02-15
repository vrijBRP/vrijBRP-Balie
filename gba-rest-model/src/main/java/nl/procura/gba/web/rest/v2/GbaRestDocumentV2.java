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

package nl.procura.gba.web.rest.v2;

import nl.procura.gba.web.rest.v2.model.base.GbaRestAntwoord;
import nl.procura.gba.web.rest.v2.model.zaken.GbaRestZaakDocumentToevoegenVraag;
import nl.procura.gba.web.rest.v2.model.zaken.GbaRestZaakDocumentenZoekenAntwoord;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakDocument;

public interface GbaRestDocumentV2 {

  String BASE_DOCUMENTS_URI           = "/{zaakId}/documenten";
  String GET_DOCUMENTS_BY_ZAAK_ID_URI = "/{zaakId}/documenten";
  String GET_DOCUMENT_BY_ID_URI       = "/{zaakId}/documenten/{id}";

  GbaRestAntwoord<GbaRestZaakDocumentenZoekenAntwoord> getDocumentsByZaakId(String zaakId);

  Object getDocumentById(String zaakId, String documentId);

  GbaRestAntwoord<GbaRestZaakDocument> addDocument(String zaakId, GbaRestZaakDocumentToevoegenVraag request);
}
