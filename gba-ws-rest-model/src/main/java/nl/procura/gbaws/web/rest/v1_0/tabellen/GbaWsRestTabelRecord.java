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

package nl.procura.gbaws.web.rest.v1_0.tabellen;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "record")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "code", "omschrijving", "datumIngang", "datumEinde", "attributen" })
public class GbaWsRestTabelRecord {

  private String code         = "";
  private String omschrijving = "";
  private int    datumIngang  = -1;
  private int    datumEinde   = -1;
  private String attributen   = "";

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getOmschrijving() {
    return omschrijving;
  }

  public void setOmschrijving(String omschrijving) {
    this.omschrijving = omschrijving;
  }

  public int getDatumIngang() {
    return datumIngang;
  }

  public void setDatumIngang(int datumIngang) {
    this.datumIngang = datumIngang;
  }

  public int getDatumEinde() {
    return datumEinde;
  }

  public void setDatumEinde(int datumEinde) {
    this.datumEinde = datumEinde;
  }

  public String getAttributen() {
    return attributen;
  }

  public void setAttributen(String attributen) {
    this.attributen = attributen;
  }
}
