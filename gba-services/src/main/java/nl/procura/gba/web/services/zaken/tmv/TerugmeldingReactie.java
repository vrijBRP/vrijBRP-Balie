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

package nl.procura.gba.web.services.zaken.tmv;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import com.google.common.collect.ComparisonChain;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.TerugmReactie;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.interfaces.DatabaseTable;

public class TerugmeldingReactie extends TerugmReactie implements Comparable<TerugmeldingReactie>, DatabaseTable {

  private static final long serialVersionUID = -2246208387145045629L;

  @Override
  public int compareTo(TerugmeldingReactie t) {

    return ComparisonChain.start().compare(t.getDatumTijdInvoer().getLongDate(),
        getDatumTijdInvoer().getLongDate()).compare(
            t.getDatumTijdInvoer().getLongTime(), getDatumTijdInvoer().getLongTime())
        .result();
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
}
