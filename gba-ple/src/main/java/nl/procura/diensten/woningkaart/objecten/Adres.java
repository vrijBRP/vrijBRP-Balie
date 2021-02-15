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

package nl.procura.diensten.woningkaart.objecten;

public class Adres {

  private String gemeentedeel      = "";
  private String gemeentedeel_code = "";
  private String straat            = "";
  private String straat_code       = "";
  private String huisnummer        = "";
  private String huisletter        = "";
  private String toevoeging        = "";
  private String aanduiding        = "";
  private String postcode          = "";
  private String locatie           = "";
  private String locatie_code      = "";

  private String ppd_code          = "";
  private String stemdistrict_code = "";

  private String wijk_code        = "";
  private String buurt_code       = "";
  private String sub_buurt_code   = "";
  private String adres_indicatie  = "";
  private String woning_indicatie = "";
  private String woning_code      = "";

  private String datum_ingang   = "";
  private String datum_einde    = "";
  private String code_object    = "";
  private String volgcode_einde = "";

  private String naam_openbare_ruimte               = "";
  private String woonplaatsnaam                     = "";
  private String identificatiecode_verblijfplaats   = "";
  private String identificatiecode_nummeraanduiding = "";

  private Persoon[] personen;

  public String getAanduiding() {

    return aanduiding;
  }

  public void setAanduiding(String aanduiding) {

    this.aanduiding = aanduiding;
  }

  public String getGemeentedeel() {

    return gemeentedeel;
  }

  public void setGemeentedeel(String gemeentedeel) {

    this.gemeentedeel = gemeentedeel;
  }

  public String getHuisletter() {

    return huisletter;
  }

  public void setHuisletter(String huisletter) {

    this.huisletter = huisletter;
  }

  public String getHuisnummer() {

    return huisnummer;
  }

  public void setHuisnummer(String huisnummer) {

    this.huisnummer = huisnummer;
  }

  public String getLocatie() {

    return locatie;
  }

  public void setLocatie(String locatie) {

    this.locatie = locatie;
  }

  public Persoon[] getPersonen() {

    return personen;
  }

  public void setPersonen(Persoon[] personen) {

    this.personen = personen;
  }

  public String getPostcode() {

    return postcode;
  }

  public void setPostcode(String postcode) {

    this.postcode = postcode;
  }

  public String getStraat() {

    return straat;
  }

  public void setStraat(String straat) {

    this.straat = straat;
  }

  public String getToevoeging() {

    return toevoeging;
  }

  public void setToevoeging(String toevoeging) {

    this.toevoeging = toevoeging;
  }

  public String getGemeentedeel_code() {
    return gemeentedeel_code;
  }

  public void setGemeentedeel_code(String gemeentedeel_code) {
    this.gemeentedeel_code = gemeentedeel_code;
  }

  public String getStraat_code() {
    return straat_code;
  }

  public void setStraat_code(String straat_code) {
    this.straat_code = straat_code;
  }

  public String getLocatie_code() {
    return locatie_code;
  }

  public void setLocatie_code(String locatie_code) {
    this.locatie_code = locatie_code;
  }

  public String getPpd_code() {
    return ppd_code;
  }

  public void setPpd_code(String ppd_code) {
    this.ppd_code = ppd_code;
  }

  public String getStemdistrict_code() {
    return stemdistrict_code;
  }

  public void setStemdistrict_code(String stemdistrict_code) {
    this.stemdistrict_code = stemdistrict_code;
  }

  public String getWijk_code() {
    return wijk_code;
  }

  public void setWijk_code(String wijk_code) {
    this.wijk_code = wijk_code;
  }

  public String getBuurt_code() {
    return buurt_code;
  }

  public void setBuurt_code(String buurt_code) {
    this.buurt_code = buurt_code;
  }

  public String getSub_buurt_code() {
    return sub_buurt_code;
  }

  public void setSub_buurt_code(String sub_buurt_code) {
    this.sub_buurt_code = sub_buurt_code;
  }

  public String getAdres_indicatie() {
    return adres_indicatie;
  }

  public void setAdres_indicatie(String adres_indicatie) {
    this.adres_indicatie = adres_indicatie;
  }

  public String getWoning_indicatie() {
    return woning_indicatie;
  }

  public void setWoning_indicatie(String woning_indicatie) {
    this.woning_indicatie = woning_indicatie;
  }

  public String getWoning_code() {
    return woning_code;
  }

  public void setWoning_code(String woning_code) {
    this.woning_code = woning_code;
  }

  public String getDatum_ingang() {
    return datum_ingang;
  }

  public void setDatum_ingang(String datum_ingang) {
    this.datum_ingang = datum_ingang;
  }

  public String getDatum_einde() {
    return datum_einde;
  }

  public void setDatum_einde(String datum_einde) {
    this.datum_einde = datum_einde;
  }

  /**
   * @return the code_object
   */
  public String getCode_object() {
    return code_object;
  }

  /**
   * @param code_object the code_object to set
   */
  public void setCode_object(String code_object) {
    this.code_object = code_object;
  }

  /**
   * @return the volgcode_einde
   */
  public String getVolgcode_einde() {
    return volgcode_einde;
  }

  /**
   * @param volgcode_einde the volgcode_einde to set
   */
  public void setVolgcode_einde(String volgcode_einde) {
    this.volgcode_einde = volgcode_einde;
  }

  public String getNaam_openbare_ruimte() {
    return naam_openbare_ruimte;
  }

  public void setNaam_openbare_ruimte(String naamOpenbareRuimte) {
    naam_openbare_ruimte = naamOpenbareRuimte;
  }

  public String getWoonplaatsnaam() {
    return woonplaatsnaam;
  }

  public void setWoonplaatsnaam(String woonplaatsnaam) {
    this.woonplaatsnaam = woonplaatsnaam;
  }

  public String getIdentificatiecode_verblijfplaats() {
    return identificatiecode_verblijfplaats;
  }

  public void setIdentificatiecode_verblijfplaats(String identificatiecodeVerblijfplaats) {
    identificatiecode_verblijfplaats = identificatiecodeVerblijfplaats;
  }

  public String getIdentificatiecode_nummeraanduiding() {
    return identificatiecode_nummeraanduiding;
  }

  public void setIdentificatiecode_nummeraanduiding(String identificatiecodeNummeraanduiding) {
    identificatiecode_nummeraanduiding = identificatiecodeNummeraanduiding;
  }
}
