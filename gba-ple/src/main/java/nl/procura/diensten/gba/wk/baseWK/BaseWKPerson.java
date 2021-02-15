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

package nl.procura.diensten.gba.wk.baseWK;

import java.beans.Transient;
import java.io.Serializable;

import nl.procura.burgerzaken.gba.NumberUtils;

public class BaseWKPerson implements Serializable {

  private static final long serialVersionUID = 2962748643597331037L;

  private BaseWKValue anummer       = new BaseWKValue();
  private BaseWKValue bsn           = new BaseWKValue();
  private BaseWKValue datum_ingang  = new BaseWKValue();
  private BaseWKValue datum_vertrek = new BaseWKValue();
  private BaseWKValue gezin_code    = new BaseWKValue();
  private BaseWKValue volg_code     = new BaseWKValue();
  private BaseWKValue datum_geboren = new BaseWKValue();

  public BaseWKValue getAnummer() {
    return anummer;
  }

  public void setAnummer(BaseWKValue anummer) {
    this.anummer = anummer;
  }

  public BaseWKValue getBsn() {
    return bsn;
  }

  public void setBsn(BaseWKValue bsn) {
    this.bsn = bsn;
  }

  public BaseWKValue getDatum_ingang() {
    return datum_ingang;
  }

  public void setDatum_ingang(BaseWKValue datum_ingang) {
    this.datum_ingang = datum_ingang;
  }

  public BaseWKValue getDatum_vertrek() {
    return datum_vertrek;
  }

  public void setDatum_vertrek(BaseWKValue datum_vertrek) {
    this.datum_vertrek = datum_vertrek;
  }

  public BaseWKValue getGezin_code() {
    return gezin_code;
  }

  public void setGezin_code(BaseWKValue gezin_code) {
    this.gezin_code = gezin_code;
  }

  public BaseWKValue getVolg_code() {
    return volg_code;
  }

  public void setVolg_code(BaseWKValue volg_code) {
    this.volg_code = volg_code;
  }

  public BaseWKValue getDatum_geboren() {
    return datum_geboren;
  }

  public void setDatum_geboren(BaseWKValue datum_geboren) {
    this.datum_geboren = datum_geboren;
  }

  @Transient
  public boolean isCurrentResident() {
    return NumberUtils.toInt(getDatum_vertrek().getValue()) < 0;
  }
}
