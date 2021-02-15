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

public class Persoon {

  private String anummer             = "";
  private String burgerservicenummer = "";
  private String datum_ingang        = "";
  private String datum_vertrek       = "";
  private String gezin_code          = "";
  private String volg_code           = "";
  private String datum_geboren       = "";

  public String getAnummer() {

    return anummer;
  }

  public void setAnummer(String anummer) {

    this.anummer = anummer;
  }

  public String getDatum_ingang() {

    return datum_ingang;
  }

  public void setDatum_ingang(String datum_ingang) {

    this.datum_ingang = datum_ingang;
  }

  public String getDatum_vertrek() {

    return datum_vertrek;
  }

  public void setDatum_vertrek(String datum_vertrek) {

    this.datum_vertrek = datum_vertrek;
  }

  public String getBurgerservicenummer() {
    return burgerservicenummer;
  }

  public void setBurgerservicenummer(String burgerservicenummer) {
    this.burgerservicenummer = burgerservicenummer;
  }

  public String getGezin_code() {
    return gezin_code;
  }

  public void setGezin_code(String gezin_code) {
    this.gezin_code = gezin_code;
  }

  public String getVolg_code() {
    return volg_code;
  }

  public void setVolg_code(String volg_code) {
    this.volg_code = volg_code;
  }

  public String getDatum_geboren() {
    return datum_geboren;
  }

  public void setDatum_geboren(String datum_geboren) {
    this.datum_geboren = datum_geboren;
  }
}
