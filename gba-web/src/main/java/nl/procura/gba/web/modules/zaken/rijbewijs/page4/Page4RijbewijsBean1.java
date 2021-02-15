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

package nl.procura.gba.web.modules.zaken.rijbewijs.page4;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page4RijbewijsBean1 implements Serializable {

  public static final String NAAM          = "naam";
  public static final String GEBOREN       = "geboren";
  public static final String CRBSLEUTEL    = "crbsleutel";
  public static final String MAATREGEL     = "maatregel";
  public static final String BSN           = "bsn";
  public static final String NATIONALITEIT = "nationaliteit";
  public static final String ANUMMER       = "anummer";
  public static final String TITEL         = "titel";
  public static final String NAAMGEBRUIK   = "naamgebruik";
  public static final String BURGSTAAT     = "burgstaat";
  public static final String PARTNER       = "partner";
  public static final String ADRES         = "adres";
  public static final String AFGIFTE       = "afgifte";
  public static final String NUMMER        = "nummer";
  public static final String AFGIFTE2      = "afgifte2";
  public static final String VERVANGT      = "vervangt";
  public static final String VERLIES       = "verlies";
  public static final String VERLIES2      = "verlies2";
  public static final String ERKEND        = "erkend";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Naam")
  private String naam          = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geboren")
  private String geboren       = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "CRB-sleutel")
  private String crbsleutel    = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Maatregel")
  private String maatregel     = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "BSN")
  private String bsn           = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Nationaliteit")
  private String nationaliteit = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "A-nummer")
  private String anummer       = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Titel / predikaat")
  private String titel         = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Naamgebruik")
  private String naamgebruik   = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Burg. staat")
  private String burgstaat     = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Partner")
  private String partner       = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Adres")
  private String adres         = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Afgifte")
  private String afgifte       = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Nummer")
  private String nummer        = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "&nbsp;")
  private String afgifte2      = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Vervangt nummer")
  private String vervangt      = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Verlies")
  private String verlies       = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "&nbsp;")
  private String verlies2      = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Erkend RDW nr")
  private String erkend        = "";

  public String getAdres() {
    return adres;
  }

  public void setAdres(String adres) {
    this.adres = adres;
  }

  public String getAfgifte() {
    return afgifte;
  }

  public void setAfgifte(String afgifte) {
    this.afgifte = afgifte;
  }

  public String getAfgifte2() {
    return afgifte2;
  }

  public void setAfgifte2(String afgifte2) {
    this.afgifte2 = afgifte2;
  }

  public String getAnummer() {
    return anummer;
  }

  public void setAnummer(String anummer) {
    this.anummer = anummer;
  }

  public String getBsn() {
    return bsn;
  }

  public void setBsn(String bsn) {
    this.bsn = bsn;
  }

  public String getBurgstaat() {
    return burgstaat;
  }

  public void setBurgstaat(String burgstaat) {
    this.burgstaat = burgstaat;
  }

  public String getCrbsleutel() {
    return crbsleutel;
  }

  public void setCrbsleutel(String crbsleutel) {
    this.crbsleutel = crbsleutel;
  }

  public String getErkend() {
    return erkend;
  }

  public void setErkend(String erkend) {
    this.erkend = erkend;
  }

  public String getGeboren() {
    return geboren;
  }

  public void setGeboren(String geboren) {
    this.geboren = geboren;
  }

  public String getMaatregel() {
    return maatregel;
  }

  public void setMaatregel(String maatregel) {
    this.maatregel = maatregel;
  }

  public String getNaam() {
    return naam;
  }

  public void setNaam(String naam) {
    this.naam = naam;
  }

  public String getNaamgebruik() {
    return naamgebruik;
  }

  public void setNaamgebruik(String naamgebruik) {
    this.naamgebruik = naamgebruik;
  }

  public String getNationaliteit() {
    return nationaliteit;
  }

  public void setNationaliteit(String nationaliteit) {
    this.nationaliteit = nationaliteit;
  }

  public String getNummer() {
    return nummer;
  }

  public void setNummer(String nummer) {
    this.nummer = nummer;
  }

  public String getPartner() {
    return partner;
  }

  public void setPartner(String partner) {
    this.partner = partner;
  }

  public String getTitel() {
    return titel;
  }

  public void setTitel(String titel) {
    this.titel = titel;
  }

  public String getVerlies() {
    return verlies;
  }

  public void setVerlies(String verlies) {
    this.verlies = verlies;
  }

  public String getVerlies2() {
    return verlies2;
  }

  public void setVerlies2(String verlies2) {
    this.verlies2 = verlies2;
  }

  public String getVervangt() {
    return vervangt;
  }

  public void setVervangt(String vervangt) {
    this.vervangt = vervangt;
  }

}
