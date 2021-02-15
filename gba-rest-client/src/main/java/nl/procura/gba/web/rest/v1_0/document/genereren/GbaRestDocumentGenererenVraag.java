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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "vraag")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "documentCode", "documentDmsNaam", "documentType", "persoon", "zaak" })
public class GbaRestDocumentGenererenVraag {

  private long                   documentCode    = 0;
  private String                 documentDmsNaam = "";
  private String                 documentType    = "";
  private GbaRestDocumentPersoon persoon         = null;
  private GbaRestDocumentZaak    zaak            = null;

  public GbaRestDocumentGenererenVraag() {
  }

  public String getDocumentType() {
    return documentType;
  }

  public void setDocumentType(String documentType) {
    this.documentType = documentType;
  }

  public long getDocumentCode() {
    return documentCode;
  }

  public void setDocumentCode(long documentCode) {
    this.documentCode = documentCode;
  }

  public GbaRestDocumentPersoon getPersoon() {
    return persoon;
  }

  public void setPersoon(GbaRestDocumentPersoon persoon) {
    this.persoon = persoon;
  }

  public GbaRestDocumentZaak getZaak() {
    return zaak;
  }

  public void setZaak(GbaRestDocumentZaak zaak) {
    this.zaak = zaak;
  }

  @Override
  public String toString() {
    return "GbaRestDocumentGenererenVraag [documentCode=" + documentCode + ", documentType=" + documentType + "]";
  }

  public String getDocumentDmsNaam() {
    return documentDmsNaam;
  }

  public void setDocumentDmsNaam(String documentDmsNaam) {
    this.documentDmsNaam = documentDmsNaam;
  }
}
