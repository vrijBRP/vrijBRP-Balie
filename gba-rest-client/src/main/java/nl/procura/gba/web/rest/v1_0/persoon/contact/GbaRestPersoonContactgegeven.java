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

package nl.procura.gba.web.rest.v1_0.persoon.contact;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "contactgegeven")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "type", "waarde" })
public class GbaRestPersoonContactgegeven {

  private GbaRestContactgegevenType type;
  private String                    waarde = "";

  public GbaRestPersoonContactgegeven() {
  }

  public GbaRestPersoonContactgegeven(GbaRestContactgegevenType type, String waarde) {
    this.type = type;
    this.waarde = waarde;
  }

  public GbaRestContactgegevenType getType() {
    return type;
  }

  public void setType(GbaRestContactgegevenType type) {
    this.type = type;
  }

  public String getWaarde() {
    return waarde;
  }

  public void setWaarde(String waarde) {
    this.waarde = waarde;
  }
}
