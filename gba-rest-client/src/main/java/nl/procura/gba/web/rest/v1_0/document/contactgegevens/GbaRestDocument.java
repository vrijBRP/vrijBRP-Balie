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

package nl.procura.gba.web.rest.v1_0.document.contactgegevens;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "document")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "documentType", "omschrijving", "datumAanvraag", "aanvraagNummer", "documentNummer", "persoon" })
public class GbaRestDocument {

  private GbaRestDocumentType    documentType   = null;
  private String                 omschrijving   = "";
  private String                 aanvraagNummer = "";
  private String                 datumAanvraag  = "";
  private String                 documentNummer = "";
  private GbaRestDocumentPersoon persoon        = new GbaRestDocumentPersoon();

  public String getAanvraagNummer() {
    return aanvraagNummer;
  }

  public void setAanvraagNummer(String aanvraagNummer) {
    this.aanvraagNummer = aanvraagNummer;
  }

  public GbaRestDocumentPersoon getPersoon() {
    return persoon;
  }

  public void setPersoon(GbaRestDocumentPersoon persoon) {
    this.persoon = persoon;
  }

  public GbaRestDocumentType getDocumentType() {
    return documentType;
  }

  public void setDocumentType(GbaRestDocumentType documentType) {
    this.documentType = documentType;
  }

  public String getDocumentNummer() {
    return documentNummer;
  }

  public void setDocumentNummer(String documentNummer) {
    this.documentNummer = documentNummer;
  }

  public String getOmschrijving() {
    return omschrijving;
  }

  public void setOmschrijving(String omschrijving) {
    this.omschrijving = omschrijving;
  }

  public String getDatumAanvraag() {
    return datumAanvraag;
  }

  public void setDatumAanvraag(String datumAanvraag) {
    this.datumAanvraag = datumAanvraag;
  }
}
