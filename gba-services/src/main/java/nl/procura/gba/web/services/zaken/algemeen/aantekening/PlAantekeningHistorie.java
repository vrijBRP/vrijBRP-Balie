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

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.AantekeningHist;
import nl.procura.gba.jpa.personen.db.AantekeningInd;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.java.reflection.ReflectionUtil;

public class PlAantekeningHistorie extends AantekeningHist implements Comparable<PlAantekeningHistorie> {

  private static final long serialVersionUID = -7767198675133171846L;

  private DateTime               tijdstip  = null;
  private UsrFieldValue          gebruiker = new UsrFieldValue();
  private PlAantekeningIndicatie indicatie = null;

  public PlAantekeningHistorie() {
    setInhoud("");
    setOnderwerp("");
  }

  @Override
  public int compareTo(PlAantekeningHistorie o) {
    return along(getCode()) > along(o.getCode()) ? -1 : 1;
  }

  public Long getCode() {
    return getCAantekeningHist();
  }

  public UsrFieldValue getGebruiker() {
    return this.gebruiker;
  }

  public void setGebruiker(UsrFieldValue gebruiker) {
    this.gebruiker = gebruiker;
  }

  public PlAantekeningStatus getHistorieStatus() {
    return PlAantekeningStatus.get(super.getStatus());
  }

  public void setHistorieStatus(PlAantekeningStatus status) {
    super.setStatus(status.getCode());
  }

  public PlAantekeningIndicatie getIndicatie() {
    if (this.indicatie != null) {
      return indicatie;
    }

    return ReflectionUtil.deepCopyBean(PlAantekeningIndicatie.class, getAantekeningInd());
  }

  public void setIndicatie(PlAantekeningIndicatie indicatie) {
    this.indicatie = indicatie;
    setAantekeningInd(ReflectionUtil.deepCopyBean(AantekeningInd.class, indicatie));
  }

  public DateTime getTijdstip() {
    return tijdstip;
  }

  public void setTijdstip(DateTime tijdstip) {
    this.tijdstip = tijdstip;
    setDIn(toBigDecimal(tijdstip.getLongDate()));
    setTIn(toBigDecimal(tijdstip.getLongTime()));
  }
}
