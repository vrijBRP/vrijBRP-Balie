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

package nl.procura.gba.web.services.zaken.algemeen.aantekening;

import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.jpa.personen.db.Aantekening;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class PlAantekening extends Aantekening implements Comparable<PlAantekening> {

  private static final long                 serialVersionUID = 402801531184013852L;
  private final List<PlAantekeningHistorie> historie         = new ArrayList<>();

  @Override
  public int compareTo(PlAantekening o) {
    Long ts1 = o.getLaatsteHistorie().getCode();
    Long ts2 = getLaatsteHistorie().getCode();
    return new CompareToBuilder().append(ts1, ts2).build();
  }

  public BsnFieldValue getBurgerServiceNummer() {
    return new BsnFieldValue(astr(getBsn()));
  }

  public List<PlAantekeningHistorie> getHistorie() {
    return historie;
  }

  public PlAantekeningHistorie getLaatsteHistorie() {

    if (getHistorie() != null && getHistorie().size() > 0) {
      return getHistorie().get(0);
    }

    return null;
  }

  public String getLaatsteHistorieKorteInhoud() {
    return MiscUtils.summarize(getLaatsteHistorie().getInhoud(), 1);
  }

  public boolean isVerwijderbaar() {
    return pos(getBsn()) || fil(getZaakId());
  }

  public void setBurgerserviceNummer(BsnFieldValue bsn) {
    setBsn(FieldValue.from(bsn).getBigDecimalValue());
  }

  public String toString() {

    PlAantekeningHistorie laatste = getLaatsteHistorie();
    String onderwerp = laatste.getOnderwerp();
    return (fil(onderwerp) ? (onderwerp + "\n") : "") + getLaatsteHistorieKorteInhoud();
  }
}
