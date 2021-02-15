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

package nl.procura.gba.web.services.zaken.rijbewijs;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import com.google.common.collect.ComparisonChain;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.DateTime.TimeType;
import nl.procura.gba.jpa.personen.db.NrdStatus;
import nl.procura.gba.jpa.personen.db.NrdStatusPK;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;

public class RijbewijsAanvraagStatus extends NrdStatus implements Comparable<RijbewijsAanvraagStatus> {

  private static final long serialVersionUID = 4479474642049202627L;

  private UsrFieldValue gebruiker = new UsrFieldValue();

  public RijbewijsAanvraagStatus() {
  }

  public RijbewijsAanvraagStatus(long dIn, long tIn, RijbewijsStatusType status, Gebruiker gebruiker) {

    setDatumTijd(new DateTime());
    setDatumTijdRdw(new DateTime(dIn, tIn, TimeType.TIME_4_DIGITS));
    setStatus(status);
    setGebruiker(new UsrFieldValue(gebruiker));
  }

  @Override
  public int compareTo(RijbewijsAanvraagStatus s) {

    long thisDate = getDatumTijdRdw().getLongDate();
    long thisTime = getDatumTijdRdw().getLongTime();
    long thisEntryDate = getDatumTijd().getLongDate();
    long thisEntryTime = getDatumTijd().getLongTime();
    long thisStatus = getStatus().getCode();

    long thatDate = s.getDatumTijdRdw().getLongDate();
    long thatTime = s.getDatumTijdRdw().getLongTime();
    long thatEntryDate = s.getDatumTijd().getLongDate();
    long thatEntryTime = s.getDatumTijd().getLongTime();
    long thatStatus = s.getStatus().getCode();

    return ComparisonChain.start()
        .compare(thatDate, thisDate)
        .compare(thatTime, thisTime)
        .compare(thatEntryDate, thisEntryDate)
        .compare(thatEntryTime, thisEntryTime)
        .compare(thatStatus, thisStatus)
        .result();
  }

  public DateTime getDatumTijd() {
    return new DateTime(getId().getDStat(), getId().getTStat());
  }

  public void setDatumTijd(DateTime dateTime) {

    getId().setDStat(dateTime.getLongDate());
    getId().setTStat(dateTime.getLongTime());
  }

  public DateTime getDatumTijdRdw() {
    return new DateTime(getRdwDStat(), getRdwTStat());
  }

  public void setDatumTijdRdw(DateTime dateTime) {

    setRdwDStat(toBigDecimal(dateTime.getLongDate()));
    setRdwTStat(toBigDecimal(dateTime.getLongTime()));
  }

  public UsrFieldValue getGebruiker() {
    return gebruiker;
  }

  public void setGebruiker(UsrFieldValue gebruiker) {
    this.gebruiker = gebruiker;
    setCUsr(gebruiker.getBigDecimalValue());
  }

  @Override
  public NrdStatusPK getId() {

    if (super.getId() == null) {
      setId(new NrdStatusPK());
    }

    return super.getId();
  }

  public String getOpmerkingen() {
    return astr(getOpm());
  }

  public void setOpmerkingen(String opm) {
    setOpm(opm);
  }

  public RijbewijsStatusType getStatus() {
    return RijbewijsStatusType.get(getId().getCStat());
  }

  public void setStatus(RijbewijsStatusType status) {
    getId().setCStat(status.getCode());
  }

  public boolean isStatus(RijbewijsStatusType... statussen) {

    for (RijbewijsStatusType status : statussen) {
      if (status == getStatus()) {
        return true;
      }
    }
    return false;
  }
}
