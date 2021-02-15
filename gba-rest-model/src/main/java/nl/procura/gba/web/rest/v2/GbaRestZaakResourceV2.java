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
import nl.procura.gba.web.rest.v2.model.zaken.*;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaak;

public interface GbaRestZaakResourceV2 {

  String BASE_ZAKEN_URI             = "/v2/zaken";
  String FIND_ZAAK_URI              = "/";
  String GET_ZAAK_BY_ZAAK_ID_URI    = "/{zaakId}";
  String DELETE_ZAAK_BY_ZAAK_ID_URI = "/{zaakId}";
  String ADD_ZAAK_URI               = "/toevoegen";
  String UPDATE_ZAAK_URI            = "/updaten";
  String UPDATE_ZAAK_STATUS_URI     = "/status";

  GbaRestAntwoord<GbaRestZaak> getZaakByZaakId(String zaakId);

  GbaRestAntwoord<GbaRestZaakZoekenAntwoord> findZaken(GbaRestZaakZoekenVraag request);

  GbaRestAntwoord<GbaRestZaak> addZaak(GbaRestZaakToevoegenVraag request);

  GbaRestAntwoord<GbaRestZaak> updateZaak(GbaRestZaakUpdateVraag request);

  GbaRestAntwoord<?> deleteZaakByZaakId(String zaakId);

  GbaRestAntwoord<?> updateZaakStatus(GbaRestZaakStatusUpdateVraag request);
}
