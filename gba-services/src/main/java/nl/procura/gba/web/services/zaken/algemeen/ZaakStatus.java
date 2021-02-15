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

package nl.procura.gba.web.services.zaken.algemeen;

import static nl.procura.standard.Globalfunctions.*;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.jpa.personen.db.BaseEntity;
import nl.procura.gba.jpa.personen.db.IndVerwerkt;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;

public class ZaakStatus extends IndVerwerkt {

  private long duur = 0;

  public ZaakStatus() {
    setOpmerking("");
    setUsr(new Usr(BaseEntity.DEFAULT, ""));
    setDatumTijdInvoer(new DateTime());
  }

  public ZaakStatus(String zaakId, ZaakStatusType type) {
    this(type);
    setZaakId(zaakId);
  }

  public ZaakStatus(ZaakStatusType type) {
    this();
    setIndVerwerkt(type.getCode());
  }

  public Long getCode() {
    return getcIndVerwerkt();
  }

  public DateTime getDatumTijdInvoer() {
    return new DateTime(getDIn(), getTIn());
  }

  public void setDatumTijdInvoer(DateTime dt) {
    setDIn(toBigDecimal(dt.getLongDate()));
    setTIn(toBigDecimal(dt.getLongTime()));
  }

  public long getDuur() {
    return duur;
  }

  public void setDuur(long duur) {
    this.duur = duur;
  }

  public ZaakStatusType getStatus() {
    return ZaakStatusType.get(getIndVerwerkt());
  }

  public void setStatus(ZaakStatusType status) {
    setIndVerwerkt(status.getCode());
  }

  public boolean isCorrectIngevoerd() {
    return pos(getDIn()) && pos(getTIn());
  }

  public UsrFieldValue getIngevoerdDoor() {
    return new UsrFieldValue(getUsr().getCUsr(), getUsr().getUsrfullname());
  }

  public void setIngevoerdDoor(UsrFieldValue ingevoerdDoor) {
    setUsr(new Usr(along(ingevoerdDoor.getValue()), ingevoerdDoor.getDescription()));
  }

}
