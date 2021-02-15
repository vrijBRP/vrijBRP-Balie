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

package nl.procura.gba.web.services.zaken.contact;

import java.io.Serializable;

public class Contactgegeven implements Serializable {

  private static final long serialVersionUID = 7719982110878403191L;
  private long              code             = -1;
  private String            gegeven          = "";
  private String            oms              = "";

  public Contactgegeven() {
  }

  public Contactgegeven(long code, String contactgegeven, String oms) {
    setCode(code);
    setGegeven(contactgegeven);
    setOms(oms);
  }

  public Contactgegeven(String contactgegeven) {
    this(-1, contactgegeven, "");
  }

  public long getCode() {
    return code;
  }

  public void setCode(long code) {
    this.code = code;
  }

  public String getGegeven() {
    return gegeven;
  }

  public void setGegeven(String aantek3) {
    this.gegeven = aantek3;
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public boolean isGegeven(String... gegevens) {
    for (String g : gegevens) {
      if (g.equalsIgnoreCase(getGegeven())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return "Aantek3 [aantek3=" + gegeven + ", code=" + code + ", oms=" + oms + "]";
  }
}
