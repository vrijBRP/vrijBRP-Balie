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

package nl.procura.covog.objecten.verzendAanvraagNp;

public class CovogBelanghebbende {

  private String naamBelanghebbende;
  private String naamVertegenwoordiger;
  private String straat;
  private String huisnummer;
  private String huisnummerToevoeging;
  private String postcode;
  private String plaats;
  private String landCode;
  private String telefoon;

  public CovogBelanghebbende() {
  }

  public CovogBelanghebbende(String naamBelanghebbende, String naamVertegenwoordiger, String straat,
      String huisnummer, String huisnummerToevoeging, String postcode, String plaats,
      String landCode, String telefoon) {

    this.naamBelanghebbende = naamBelanghebbende;
    this.naamVertegenwoordiger = naamVertegenwoordiger;
    this.straat = straat;
    this.huisnummer = huisnummer;
    this.huisnummerToevoeging = huisnummerToevoeging;
    this.postcode = postcode;
    this.plaats = plaats;
    this.landCode = landCode;
    this.telefoon = telefoon;
  }

  public String getNaamBelanghebbende() {
    return naamBelanghebbende;
  }

  public void setNaamBelanghebbende(String naamBelanghebbende) {
    this.naamBelanghebbende = naamBelanghebbende;
  }

  public String getNaamVertegenwoordiger() {
    return naamVertegenwoordiger;
  }

  public void setNaamVertegenwoordiger(String naamVertegenwoordiger) {
    this.naamVertegenwoordiger = naamVertegenwoordiger;
  }

  public String getStraat() {
    return straat;
  }

  public void setStraat(String straat) {
    this.straat = straat;
  }

  public String getHuisnummer() {
    return huisnummer;
  }

  public void setHuisnummer(String huisnummer) {
    this.huisnummer = huisnummer;
  }

  public String getHuisnummerToevoeging() {
    return huisnummerToevoeging;
  }

  public void setHuisnummerToevoeging(String huisnummerToevoeging) {
    this.huisnummerToevoeging = huisnummerToevoeging;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public String getPlaats() {
    return plaats;
  }

  public void setPlaats(String plaats) {
    this.plaats = plaats;
  }

  public String getLandCode() {
    return landCode;
  }

  public void setLandCode(String landCode) {
    this.landCode = landCode;
  }

  public String getTelefoon() {
    return telefoon;
  }

  public void setTelefoon(String telefoon) {
    this.telefoon = telefoon;
  }
}
