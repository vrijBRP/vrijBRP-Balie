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

public class Diversengegevens {

  private String gezinsverhouding_omschrijving = "";
  private String datum_pl_verstrekt            = "";
  private String anummer                       = "";
  private String datum_geldigheid              = "";
  private String kenmerk                       = "";
  private String kenmerk_omschrijving          = "";
  private String vuurwapen                     = "";
  private String ots                           = "";
  private String signaal                       = "";
  private String correspondentie_adres_1       = "";
  private String correspondentie_adres_2       = "";
  private String gezinsverhouding              = "";
  private String indicatie_pl_verstrekt        = "";

  public Diversengegevens() {
  }

  public String getAnummer() {
    return anummer;
  }

  public void setAnummer(String anummer) {
    this.anummer = anummer;
  }

  public String getCorrespondentie_adres_1() {
    return correspondentie_adres_1;
  }

  public void setCorrespondentie_adres_1(String correspondentie_adres_1) {
    this.correspondentie_adres_1 = correspondentie_adres_1;
  }

  public String getCorrespondentie_adres_2() {
    return correspondentie_adres_2;
  }

  public void setCorrespondentie_adres_2(String correspondentie_adres_2) {
    this.correspondentie_adres_2 = correspondentie_adres_2;
  }

  public String getDatum_geldigheid() {
    return datum_geldigheid;
  }

  public void setDatum_geldigheid(String datum_geldigheid) {
    this.datum_geldigheid = datum_geldigheid;
  }

  public String getDatum_pl_verstrekt() {
    return datum_pl_verstrekt;
  }

  public void setDatum_pl_verstrekt(String datum_pl_verstrekt) {
    this.datum_pl_verstrekt = datum_pl_verstrekt;
  }

  public String getGezinsverhouding() {
    return gezinsverhouding;
  }

  public void setGezinsverhouding(String gezinsverhouding) {
    this.gezinsverhouding = gezinsverhouding;
  }

  public String getGezinsverhouding_omschrijving() {
    return gezinsverhouding_omschrijving;
  }

  public void setGezinsverhouding_omschrijving(String gezinsverhouding_omschrijving) {
    this.gezinsverhouding_omschrijving = gezinsverhouding_omschrijving;
  }

  public String getIndicatie_pl_verstrekt() {
    return indicatie_pl_verstrekt;
  }

  public void setIndicatie_pl_verstrekt(String indicatie_pl_verstrekt) {
    this.indicatie_pl_verstrekt = indicatie_pl_verstrekt;
  }

  public String getKenmerk() {
    return kenmerk;
  }

  public void setKenmerk(String kenmerk) {
    this.kenmerk = kenmerk;
  }

  public String getKenmerk_omschrijving() {
    return kenmerk_omschrijving;
  }

  public void setKenmerk_omschrijving(String kenmerk_omschrijving) {
    this.kenmerk_omschrijving = kenmerk_omschrijving;
  }

  public String getOts() {
    return ots;
  }

  public void setOts(String ots) {
    this.ots = ots;
  }

  public String getSignaal() {
    return signaal;
  }

  public void setSignaal(String signaal) {
    this.signaal = signaal;
  }

  public String getVuurwapen() {
    return vuurwapen;
  }

  public void setVuurwapen(String vuurwapen) {
    this.vuurwapen = vuurwapen;
  }
}
