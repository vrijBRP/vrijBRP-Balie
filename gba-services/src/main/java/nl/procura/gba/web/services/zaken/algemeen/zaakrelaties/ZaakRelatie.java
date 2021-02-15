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

package nl.procura.gba.web.services.zaken.algemeen.zaakrelaties;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.ZaakRel;
import nl.procura.gba.jpa.personen.db.ZaakRelPK;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;

public class ZaakRelatie extends ZaakRel {

  public ZaakRelatie() {
    setId(new ZaakRelPK());
  }

  public String getGerelateerdZaakId() {
    return getId().getZaakIdRel();
  }

  public void setGerelateerdZaakId(String zaakId) {
    getId().setZaakIdRel(zaakId);
  }

  public ZaakType getGerelateerdZaakType() {
    return ZaakType.get(along(getzTypeRel()));
  }

  public void setGerelateerdZaakType(ZaakType zaakType) {
    setzTypeRel(toBigDecimal(zaakType.getCode()));
  }

  public String getZaakId() {
    return getId().getZaakId();
  }

  public void setZaakId(String zaakId) {
    getId().setZaakId(zaakId);
  }

  public ZaakType getZaakType() {
    return ZaakType.get(along(getzType()));
  }

  public void setZaakType(ZaakType zaakType) {
    setzType(toBigDecimal(zaakType.getCode()));
  }

  public boolean isVanToepassingOp(Zaak zaak) {
    String zaakId = zaak.getZaakId();
    boolean isZaak = zaakId.equals(getId().getZaakId());
    boolean isRelZaak = zaakId.equals(getId().getZaakIdRel());
    return isZaak || isRelZaak;
  }
}
