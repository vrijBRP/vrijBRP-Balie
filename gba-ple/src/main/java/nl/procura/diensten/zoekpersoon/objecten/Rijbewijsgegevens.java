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

package nl.procura.diensten.zoekpersoon.objecten;

public class Rijbewijsgegevens {

  private String datum_geldigheid       = "";
  private String nummer                 = "";
  private String categorie              = "";
  private String datum_einde_geldigheid = "";
  private String datum_verfrissing      = "";
  private String belemmering            = "";
  private String beperking              = "";

  public Rijbewijsgegevens() {
  }

  public String getNummer() {
    return nummer;
  }

  public void setNummer(String nummer) {
    this.nummer = nummer;
  }

  public String getCategorie() {
    return categorie;
  }

  public void setCategorie(String categorie) {
    this.categorie = categorie;
  }

  public String getDatum_einde_geldigheid() {
    return datum_einde_geldigheid;
  }

  public void setDatum_einde_geldigheid(String datum_einde_geldigheid) {
    this.datum_einde_geldigheid = datum_einde_geldigheid;
  }

  public String getDatum_geldigheid() {
    return datum_geldigheid;
  }

  public void setDatum_geldigheid(String datum_geldigheid) {
    this.datum_geldigheid = datum_geldigheid;
  }

  public String getBelemmering() {
    return belemmering;
  }

  public void setBelemmering(String belemmering) {
    this.belemmering = belemmering;
  }

  public String getBeperking() {
    return beperking;
  }

  public void setBeperking(String beperking) {
    this.beperking = beperking;
  }

  public String getDatum_verfrissing() {
    return datum_verfrissing;
  }

  public void setDatum_verfrissing(String datum_verfrissing) {
    this.datum_verfrissing = datum_verfrissing;
  }
}
