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

package nl.procura.gba.web.services.bs.lv;

import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class LvRechtsfeitAkte {

  private String     nummer    = "";
  private String     brpNummer = "";
  private String     jaar      = "";
  private FieldValue plaats    = new FieldValue();

  public String getJaar() {
    return jaar;
  }

  public void setJaar(String jaar) {
    this.jaar = jaar;
  }

  public String getNummer() {
    return nummer;
  }

  public void setNummer(String nummer) {
    this.nummer = nummer;
  }

  public String getBrpNummer() {
    return brpNummer;
  }

  public void setBrpNummer(String brpNummer) {
    this.brpNummer = brpNummer;
  }

  public FieldValue getPlaats() {
    return plaats;
  }

  public void setPlaats(FieldValue plaats) {
    this.plaats = FieldValue.from(plaats);
  }
}
