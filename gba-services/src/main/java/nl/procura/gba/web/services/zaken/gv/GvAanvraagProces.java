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

package nl.procura.gba.web.services.zaken.gv;

import static nl.procura.standard.Globalfunctions.*;

import com.google.common.collect.ComparisonChain;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.BaseEntity;
import nl.procura.gba.jpa.personen.db.GvProce;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.interfaces.DatabaseTable;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType;

public class GvAanvraagProces extends GvProce implements Comparable<GvAanvraagProces>, DatabaseTable {

  public GvAanvraagProces() {
    setUsr(new Usr(BaseEntity.DEFAULT));
  }

  @Override
  public int compareTo(GvAanvraagProces o) {

    long thisDate = getCGvProces();
    long thatDate = o.getCGvProces();

    return ComparisonChain.start().compare(thatDate, thisDate).result();
  }

  public DateTime getDatumTijdInvoer() {
    return new DateTime(getDIn(), getTIn());
  }

  public void setDatumTijdInvoer(DateTime dt) {

    setDIn(toBigDecimal(dt.getLongDate()));
    setTIn(toBigDecimal(dt.getLongTime()));
  }

  public UsrFieldValue getIngevoerdDoor() {
    return new UsrFieldValue(getUsr().getCUsr(), getUsr().getUsrfullname());
  }

  public void setIngevoerdDoor(UsrFieldValue ingevoerdDoor) {
    setUsr(new Usr(along(ingevoerdDoor.getValue()), ingevoerdDoor.getDescription()));
  }

  public KoppelEnumeratieType getProcesActieType() {
    return KoppelEnumeratieType.get(along(getCProcesactie()));
  }

  public void setProcesActieType(KoppelEnumeratieType procesActie) {
    setCProcesactie(toBigDecimal(procesActie == null ? null : procesActie.getCode()));
  }

  public KoppelEnumeratieType getReactieType() {
    return KoppelEnumeratieType.get(along(getCReactie()));
  }

  public void setReactieType(KoppelEnumeratieType type) {
    setCReactie(toBigDecimal(type == null ? null : type.getCode()));
  }

  public DateTime getDatumEindeTermijn() {
    return new DateTime(getDEndTermijn());
  }

  public boolean isDatumEindeTermijn() {
    return pos(getDEndTermijn());
  }

  public void setDatumEindeTermijn(DateTime dt) {
    setDEndTermijn(toBigDecimal(dt.getLongDate()));
  }
}
