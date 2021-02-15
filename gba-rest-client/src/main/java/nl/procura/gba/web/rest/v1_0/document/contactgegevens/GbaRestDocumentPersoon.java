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

@XmlRootElement(name = "persoon")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "geslachtsnaam", "voornamen", "voorvoegsel", "naamgebruik", "voorvoegselPartner",
    "geslachtsnaamPartner", "contactgegevens" })
public class GbaRestDocumentPersoon {

  private String                 geslachtsnaam        = "";
  private String                 voornamen            = "";
  private String                 voorvoegsel          = "";
  private String                 naamgebruik          = "";
  private String                 voorvoegselPartner   = "";
  private String                 geslachtsnaamPartner = "";
  private GbaRestDocumentContact contactgegevens      = new GbaRestDocumentContact();

  public String getGeslachtsnaam() {
    return geslachtsnaam;
  }

  public void setGeslachtsnaam(String geslachtsnaam) {
    this.geslachtsnaam = geslachtsnaam;
  }

  public String getVoornamen() {
    return voornamen;
  }

  public void setVoornamen(String voornamen) {
    this.voornamen = voornamen;
  }

  public String getVoorvoegsel() {
    return voorvoegsel;
  }

  public void setVoorvoegsel(String voorvoegsel) {
    this.voorvoegsel = voorvoegsel;
  }

  public GbaRestDocumentContact getContactgegevens() {
    return contactgegevens;
  }

  public void setContactgegevens(GbaRestDocumentContact contactgegevens) {
    this.contactgegevens = contactgegevens;
  }

  public String getNaamgebruik() {
    return naamgebruik;
  }

  public void setNaamgebruik(String naamgebruik) {
    this.naamgebruik = naamgebruik;
  }

  public String getVoorvoegselPartner() {
    return voorvoegselPartner;
  }

  public void setVoorvoegselPartner(String voorvoegselPartner) {
    this.voorvoegselPartner = voorvoegselPartner;
  }

  public String getGeslachtsnaamPartner() {
    return geslachtsnaamPartner;
  }

  public void setGeslachtsnaamPartner(String geslachtsnaamPartner) {
    this.geslachtsnaamPartner = geslachtsnaamPartner;
  }
}
