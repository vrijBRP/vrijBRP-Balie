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

public class Verblijfstitelgegevens {

  private String datum_einde_onderzoek            = "";
  private String ingangsdatum_verblijfstitel      = "";
  private String datum_opneming                   = "";
  private String datum_geldigheid                 = "";
  private String indicatie_onjuist                = "";
  private String datum_ingang_onderzoek           = "";
  private String aanduiding_verblijfstitel        = "";
  private String datum_einde_verblijfstitel       = "";
  private String aanduiding_gegevens_in_onderzoek = "";
  private String verblijfstitel_omschrijving      = "";

  public Verblijfstitelgegevens() {
  }

  public String getAanduiding_gegevens_in_onderzoek() {
    return aanduiding_gegevens_in_onderzoek;
  }

  public void setAanduiding_gegevens_in_onderzoek(String aanduiding_gegevens_in_onderzoek) {
    this.aanduiding_gegevens_in_onderzoek = aanduiding_gegevens_in_onderzoek;
  }

  public String getAanduiding_verblijfstitel() {
    return aanduiding_verblijfstitel;
  }

  public void setAanduiding_verblijfstitel(String aanduiding_verblijfstitel) {
    this.aanduiding_verblijfstitel = aanduiding_verblijfstitel;
  }

  public String getDatum_einde_onderzoek() {
    return datum_einde_onderzoek;
  }

  public void setDatum_einde_onderzoek(String datum_einde_onderzoek) {
    this.datum_einde_onderzoek = datum_einde_onderzoek;
  }

  public String getDatum_einde_verblijfstitel() {
    return datum_einde_verblijfstitel;
  }

  public void setDatum_einde_verblijfstitel(String datum_einde_verblijfstitel) {
    this.datum_einde_verblijfstitel = datum_einde_verblijfstitel;
  }

  public String getDatum_geldigheid() {
    return datum_geldigheid;
  }

  public void setDatum_geldigheid(String datum_geldigheid) {
    this.datum_geldigheid = datum_geldigheid;
  }

  public String getDatum_ingang_onderzoek() {
    return datum_ingang_onderzoek;
  }

  public void setDatum_ingang_onderzoek(String datum_ingang_onderzoek) {
    this.datum_ingang_onderzoek = datum_ingang_onderzoek;
  }

  public String getDatum_opneming() {
    return datum_opneming;
  }

  public void setDatum_opneming(String datum_opneming) {
    this.datum_opneming = datum_opneming;
  }

  public String getIndicatie_onjuist() {
    return indicatie_onjuist;
  }

  public void setIndicatie_onjuist(String indicatie_onjuist) {
    this.indicatie_onjuist = indicatie_onjuist;
  }

  public String getIngangsdatum_verblijfstitel() {
    return ingangsdatum_verblijfstitel;
  }

  public void setIngangsdatum_verblijfstitel(String ingangsdatum_verblijfstitel) {
    this.ingangsdatum_verblijfstitel = ingangsdatum_verblijfstitel;
  }

  public String getVerblijfstitel_omschrijving() {
    return verblijfstitel_omschrijving;
  }

  public void setVerblijfstitel_omschrijving(String verblijfstitel_omschrijving) {
    this.verblijfstitel_omschrijving = verblijfstitel_omschrijving;
  }
}
