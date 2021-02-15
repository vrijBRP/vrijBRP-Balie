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

public class Inschrijvinggegevens {

  private String datum_eerste_inschrijving_gba = "";
  private String gemeente_pk                   = "";
  private String indicatie_geheim              = "";
  private String pk_conversie                  = "";
  private String datum_opschorting_pl          = "";
  private String reden_opschorting_pl          = "";
  private String naamgebruik                   = "";
  private String datum_ingang_blokkering_pl    = "";
  private String versienummer                  = "";
  private String datumtijdstempel              = "";
  private String datum_opneming                = "";
  private String datum_geldigheid              = "";
  private String gemeente_blokkering_pl        = "";

  public Inschrijvinggegevens() {
  }

  public String getDatum_eerste_inschrijving_gba() {
    return datum_eerste_inschrijving_gba;
  }

  public void setDatum_eerste_inschrijving_gba(String datum_eerste_inschrijving_gba) {
    this.datum_eerste_inschrijving_gba = datum_eerste_inschrijving_gba;
  }

  public String getDatum_ingang_blokkering_pl() {
    return datum_ingang_blokkering_pl;
  }

  public void setDatum_ingang_blokkering_pl(String datum_ingang_blokkering_pl) {
    this.datum_ingang_blokkering_pl = datum_ingang_blokkering_pl;
  }

  public String getDatum_opschorting_pl() {
    return datum_opschorting_pl;
  }

  public void setDatum_opschorting_pl(String datum_opschorting_pl) {
    this.datum_opschorting_pl = datum_opschorting_pl;
  }

  public String getDatumtijdstempel() {
    return datumtijdstempel;
  }

  public void setDatumtijdstempel(String datumtijdstempel) {
    this.datumtijdstempel = datumtijdstempel;
  }

  public String getGemeente_pk() {
    return gemeente_pk;
  }

  public void setGemeente_pk(String gemeente_pk) {
    this.gemeente_pk = gemeente_pk;
  }

  public String getIndicatie_geheim() {
    return indicatie_geheim;
  }

  public void setIndicatie_geheim(String indicatie_geheim) {
    this.indicatie_geheim = indicatie_geheim;
  }

  public String getNaamgebruik() {
    return naamgebruik;
  }

  public void setNaamgebruik(String naamgebruik) {
    this.naamgebruik = naamgebruik;
  }

  public String getPk_conversie() {
    return pk_conversie;
  }

  public void setPk_conversie(String pk_conversie) {
    this.pk_conversie = pk_conversie;
  }

  public String getReden_opschorting_pl() {
    return reden_opschorting_pl;
  }

  public void setReden_opschorting_pl(String reden_opschorting_pl) {
    this.reden_opschorting_pl = reden_opschorting_pl;
  }

  public String getVersienummer() {
    return versienummer;
  }

  public void setVersienummer(String versienummer) {
    this.versienummer = versienummer;
  }

  public String getDatum_opneming() {
    return datum_opneming;
  }

  public void setDatum_opneming(String datum_opneming) {
    this.datum_opneming = datum_opneming;
  }

  public String getDatum_geldigheid() {
    return datum_geldigheid;
  }

  public void setDatum_geldigheid(String datum_geldigheid) {
    this.datum_geldigheid = datum_geldigheid;
  }

  public String getGemeente_blokkering_pl() {
    return gemeente_blokkering_pl;
  }

  public void setGemeente_blokkering_pl(String gemeente_blokkering_pl) {
    this.gemeente_blokkering_pl = gemeente_blokkering_pl;
  }
}
