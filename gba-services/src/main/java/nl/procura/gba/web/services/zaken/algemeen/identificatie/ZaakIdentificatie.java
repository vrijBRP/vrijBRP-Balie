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

package nl.procura.gba.web.services.zaken.algemeen.identificatie;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.ZaakId;
import nl.procura.gba.jpa.personen.db.ZaakIdPK;

public class ZaakIdentificatie extends ZaakId {

  public ZaakIdentificatie() {
    setId(new ZaakIdPK());
  }

  public String getInternId() {
    return getId().getInternId();
  }

  public void setInternId(String internId) {
    getId().setInternId(internId);
  }

  public String getType() {
    return getId().getType();
  }

  public void setType(String type) {
    getId().setType(type);
  }

  public ZaakIdType getZaakIdType() {
    return ZaakIdType.get(getType());
  }

  public ZaakType getZaakType() {
    return ZaakType.get(getZaakTypeCode());
  }

  public void setZaakType(ZaakType zaakType) {
    setZaakTypeCode(zaakType.getCode());
  }
}
