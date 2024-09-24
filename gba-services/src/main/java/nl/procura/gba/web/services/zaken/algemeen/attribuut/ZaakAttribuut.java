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

package nl.procura.gba.web.services.zaken.algemeen.attribuut;

import nl.procura.gba.jpa.personen.db.ZaakAttr;
import nl.procura.gba.jpa.personen.db.ZaakAttrPK;

public class ZaakAttribuut extends ZaakAttr {

  public ZaakAttribuut() {
    ZaakAttrPK id = new ZaakAttrPK();
    id.setcUsr(0L);
    setId(id);
  }

  public ZaakAttribuut(String zaakId, ZaakAttribuutType type) {
    ZaakAttrPK id = new ZaakAttrPK();
    id.setZaakId(zaakId);
    id.setZaakAttr(type.getCode());
    id.setcUsr(0L);
    setId(id);
    setOms(type.getOms());
  }

  public ZaakAttribuut(String zaakId, ZaakAttribuutType type, String waarde) {
    this(zaakId, type);
    setWaarde(waarde);
  }

  public ZaakAttribuut(String zaakId, ZaakAttribuutType type, String attribuut, String waarde) {
    this(zaakId, type);
    setAttribuut(attribuut);
    setWaarde(waarde);
  }

  public String getAttribuut() {
    return getId().getZaakAttr();
  }

  public void setAttribuut(String attribuut) {
    getId().setZaakAttr(attribuut);
  }

  public void setCodeGebruiker(long codeGebruiker) {
    getId().setcUsr(codeGebruiker);
  }

  public String getZaakId() {
    return getId().getZaakId();
  }

  public void setZaakId(String zaakId) {
    getId().setZaakId(zaakId);
  }

  public String toString() {
    return getAttribuut();
  }
}
