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

package nl.procura.gba.web.rest.v1_0.document.genereren;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "persoon")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "bsn", "gegevens" })
public class GbaRestDocumentPersoon {

  private String                       bsn      = "";
  private List<GbaRestDocumentGegeven> gegevens = new ArrayList<>();

  public GbaRestDocumentPersoon() {
  }

  public GbaRestDocumentPersoon(String bsn) {
    this.bsn = bsn;
  }

  public String getBsn() {
    return bsn;
  }

  public void setBsn(String bsn) {
    this.bsn = bsn;
  }

  public List<GbaRestDocumentGegeven> getGegevens() {
    return gegevens;
  }

  public void setGegevens(List<GbaRestDocumentGegeven> gegevens) {
    this.gegevens = gegevens;
  }

  @Transient
  public GbaRestDocumentPersoon addGegeven(String naam, String waarde) {
    getGegevens().add(new GbaRestDocumentGegeven(naam, waarde));
    return this;
  }
}
