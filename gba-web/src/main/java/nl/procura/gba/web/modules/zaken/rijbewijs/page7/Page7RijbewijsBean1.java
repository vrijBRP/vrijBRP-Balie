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

package nl.procura.gba.web.modules.zaken.rijbewijs.page7;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page7RijbewijsBean1 implements Serializable {

  public static final String STATUS            = "status";
  public static final String DATUMTIJDSTATUS   = "datumTijdStatus";
  public static final String GEMREF            = "gemRef";
  public static final String RDWNR             = "rdwNr";
  public static final String RIJBEWIJSNUMMER   = "rijbewijsnummer";
  public static final String DEND              = "dend";
  public static final String AFGIFTE           = "afgifte";
  public static final String VERLIESDIEFSTAL   = "verliesDiefstal";
  public static final String CRBSLEUTEL        = "crbSleutel";
  public static final String BSN               = "bsn";
  public static final String ANR               = "anr";
  public static final String NAAM              = "naam";
  public static final String VOORNAMEN         = "voornamen";
  public static final String VOORVOEGSEL       = "voorvoegsel";
  public static final String GEBOREN           = "geboren";
  public static final String GESLACHT          = "geslacht";
  public static final String NAAMGEBRUIK       = "naamgebruik";
  public static final String BURGSTAAT         = "burgStaat";
  public static final String PARTNER           = "partner";
  public static final String ADRES             = "adres";
  public static final String PCWOONPLAATS      = "pcWoonplaats";
  public static final String AANVRAAGNR        = "aanvraagnr";
  public static final String DATUMTIJDAANVRAAG = "datumTijdAanvraag";
  public static final String SOORT             = "soort";
  public static final String REDEN             = "reden";
  public static final String SPOED             = "spoed";
  public static final String BESTENDIG         = "bestendig";
  public static final String VERVANGT          = "vervangt";
  public static final String AUTORITEIT        = "autoriteit";
  public static final String LOC               = "loc";
  public static final String COLLO             = "collo";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Status")
  private String status            = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Datum/tijd")
  private String datumTijdStatus   = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gemeente")
  private String gemRef            = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Rijbewijsnummer")
  private String rdwNr             = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Rijbewijsnummer")
  private String rijbewijsnummer   = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Einde geldigheid")
  private String dend              = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Afgifte")
  private String afgifte           = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Verlies/diefstal")
  private String verliesDiefstal   = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "CRB-sleutel")
  private String crbSleutel        = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "BSN")
  private String bsn               = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "A-nummer")
  private String anr               = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Naam")
  private String naam              = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Voornamen")
  private String voornamen         = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Voorvoegsel")
  private String voorvoegsel       = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geboren")
  private String geboren           = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Naamgebruik")
  private String naamgebruik       = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Burg. staat")
  private String burgStaat         = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geslacht")
  private String geslacht          = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Partner")
  private String partner           = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Adres")
  private String adres             = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Postcode / woonplaats")
  private String pcWoonplaats      = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Aanvraag-nr")
  private String aanvraagnr        = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Datum/tijd")
  private String datumTijdAanvraag = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Soort")
  private String soort             = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Reden")
  private String reden             = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Spoed")
  private String spoed             = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Bestendig")
  private String bestendig         = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Vervangt rijbewijs")
  private String vervangt          = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Autoriteit")
  private String autoriteit        = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Locatie")
  private String loc               = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Collonummer")
  private String collo             = "";

  public String getAanvraagnr() {
    return aanvraagnr;
  }

  public void setAanvraagnr(String aanvraagnr) {
    this.aanvraagnr = aanvraagnr;
  }

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

  public String getAnr() {
    return anr;
  }

  public void setAnr(String anr) {
    this.anr = anr;
  }

  public String getAutoriteit() {
    return autoriteit;
  }

  public void setAutoriteit(String autoriteit) {
    this.autoriteit = autoriteit;
  }

  public String getBestendig() {
    return bestendig;
  }

  public void setBestendig(String bestendig) {
    this.bestendig = bestendig;
  }

  public String getBsn() {
    return bsn;
  }

  public void setBsn(String bsn) {
    this.bsn = bsn;
  }

  public String getBurgStaat() {
    return burgStaat;
  }

  public void setBurgStaat(String burgStaat) {
    this.burgStaat = burgStaat;
  }

  public String getGeslacht() {
    return geslacht;
  }

  public void setGeslacht(String geslacht) {
    this.geslacht = geslacht;
  }

  public String getCollo() {
    return collo;
  }

  public void setCollo(String collo) {
    this.collo = collo;
  }

  public String getCrbSleutel() {
    return crbSleutel;
  }

  public void setCrbSleutel(String crbSleutel) {
    this.crbSleutel = crbSleutel;
  }

  public String getDatumTijdAanvraag() {
    return datumTijdAanvraag;
  }

  public void setDatumTijdAanvraag(String datumTijdAanvraag) {
    this.datumTijdAanvraag = datumTijdAanvraag;
  }

  public String getDatumTijdStatus() {
    return datumTijdStatus;
  }

  public void setDatumTijdStatus(String datumTijdStatus) {
    this.datumTijdStatus = datumTijdStatus;
  }

  public String getDend() {
    return dend;
  }

  public void setDend(String dend) {
    this.dend = dend;
  }

  public String getGeboren() {
    return geboren;
  }

  public void setGeboren(String geboren) {
    this.geboren = geboren;
  }

  public String getGemRef() {
    return gemRef;
  }

  public void setGemRef(String gemRef) {
    this.gemRef = gemRef;
  }

  public String getLoc() {
    return loc;
  }

  public void setLoc(String loc) {
    this.loc = loc;
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

  public String getPartner() {
    return partner;
  }

  public void setPartner(String partner) {
    this.partner = partner;
  }

  public String getPcWoonplaats() {
    return pcWoonplaats;
  }

  public void setPcWoonplaats(String pcWoonplaats) {
    this.pcWoonplaats = pcWoonplaats;
  }

  public String getRdwNr() {
    return rdwNr;
  }

  public void setRdwNr(String rdwNr) {
    this.rdwNr = rdwNr;
  }

  public String getReden() {
    return reden;
  }

  public void setReden(String reden) {
    this.reden = reden;
  }

  public String getRijbewijsnummer() {
    return rijbewijsnummer;
  }

  public void setRijbewijsnummer(String rijbewijsnummer) {
    this.rijbewijsnummer = rijbewijsnummer;
  }

  public String getSoort() {
    return soort;
  }

  public void setSoort(String soort) {
    this.soort = soort;
  }

  public String getSpoed() {
    return spoed;
  }

  public void setSpoed(String spoed) {
    this.spoed = spoed;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getVerliesDiefstal() {
    return verliesDiefstal;
  }

  public void setVerliesDiefstal(String verliesDiefstal) {
    this.verliesDiefstal = verliesDiefstal;
  }

  public String getVervangt() {
    return vervangt;
  }

  public void setVervangt(String vervangt) {
    this.vervangt = vervangt;
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

}
