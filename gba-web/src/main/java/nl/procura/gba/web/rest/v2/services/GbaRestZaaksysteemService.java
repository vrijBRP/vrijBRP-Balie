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

import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakId;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificatie;

public class GbaRestZaaksysteemService extends GbaRestAbstractService {

  public GbaRestZaakId genereerZaakId() {
    ZaakIdentificatie zaakId = getServices().getZaakDmsService().genereerZaakId();
    GbaRestZaakId restZaakId = new GbaRestZaakId();
    restZaakId.setId(zaakId.getExternId());
    restZaakId.setSysteem(zaakId.getType());
    return restZaakId;
  }
}
