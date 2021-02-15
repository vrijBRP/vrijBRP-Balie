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

package nl.procura.gba.web.services.zaken.vog;

import static nl.procura.standard.Globalfunctions.*;

import java.text.MessageFormat;

import nl.procura.gba.jpa.personen.db.VogBelang;
import nl.procura.gba.web.services.interfaces.DatabaseTable;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class VogBelanghebbende extends VogBelang implements DatabaseTable {

  private static final long serialVersionUID = 5429531516535758928L;

  private FieldValue land = new FieldValue();
  private FieldValue pc   = new FieldValue();

  public String getAdres() {
    return trim(MessageFormat.format("{0} {1} {2}", getStraat(), getHnr(), getHnrT()));
  }

  public long getHnr() {
    return along(getHnrB());
  }

  public void setHnr(long hnr) {
    setHnrB(toBigDecimal(hnr));
  }

  public String getHnrT() {
    return getHnrTB();
  }

  public void setHnrT(String hnrT) {
    setHnrTB(hnrT);
  }

  public FieldValue getLand() {
    return land;
  }

  public void setLand(FieldValue land) {
    this.land = FieldValue.from(land);
    setCLandB(this.land.getBigDecimalValue());
  }

  public String getNaam() {
    return getInstellingB();
  }

  public void setNaam(String naam) {
    setInstellingB(naam);
  }

  public FieldValue getPc() {
    return this.pc;
  }

  public void setPc(FieldValue pc) {

    this.pc = pc;
    setPcB(astr(pc.getValue()));
  }

  public String getPlaats() {
    return getPlaatsB();
  }

  public void setPlaats(String plaats) {
    setPlaatsB(plaats);
  }

  public String getStraat() {
    return getStraatB();
  }

  public void setStraat(String straat) {
    setStraatB(straat);
  }

  public String getTel() {
    return getTelB();
  }

  public void setTel(String tel) {
    setTelB(tel);
  }

  public String getVertegenwoordiger() {
    return getNaamB();
  }

  public void setVertegenwoordiger(String vertegenwoordiger) {
    setNaamB(vertegenwoordiger);
  }

  public String toString() {
    return getNaam() + ", " + getVertegenwoordiger();
  }
}
