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

package nl.procura.gba.web.modules.zaken.tmv.page6;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page6TmvBean2 implements Serializable {

  public static final String ACTIE                = "actie";
  public static final String VERWERKING           = "verwerking";
  public static final String DOSSIERNR            = "dossiernr";
  public static final String VERSTUURDDOOR        = "verstuurddoor";
  public static final String TOELICHTING          = "toelichting";
  public static final String DATUMAANLEG          = "datumaanleg";
  public static final String DATUMWIJZIGING       = "datumwijziging";
  public static final String GEMEENTE             = "gemeente";
  public static final String DATUMVERWAFH         = "datumVerwAfh";
  public static final String STATUSDOSSIER        = "statusdossier";
  public static final String RESULTAATCODE        = "resultaatcode";
  public static final String TOELICHTINGRESULTAAT = "toelichtingResultaat";
  public static final String TIJDSTIP             = "tijdstip";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Actie")
  private String actie                = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Verwerking")
  private String verwerking           = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Dossiernr")
  private String dossiernr            = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Verstuurd door")
  private String verstuurddoor        = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Toelichting")
  private String toelichting          = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Datum aanleg")
  private String datumaanleg          = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Datum wijziging")
  private String datumwijziging       = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gemeente")
  private String gemeente             = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Datum verwachte afhandeling")
  private String datumVerwAfh         = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Status dossier")
  private String statusdossier        = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Resultaat")
  private String resultaatcode        = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Toelichting resultaat")
  private String toelichtingResultaat = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Tijdstip")
  private String tijdstip             = "";

  public String getActie() {
    return actie;
  }

  public void setActie(String actie) {
    this.actie = actie;
  }

  public String getDatumaanleg() {
    return datumaanleg;
  }

  public void setDatumaanleg(String datumaanleg) {
    this.datumaanleg = datumaanleg;
  }

  public String getDatumVerwAfh() {
    return datumVerwAfh;
  }

  public void setDatumVerwAfh(String datumVerwAfh) {
    this.datumVerwAfh = datumVerwAfh;
  }

  public String getDatumwijziging() {
    return datumwijziging;
  }

  public void setDatumwijziging(String datumwijziging) {
    this.datumwijziging = datumwijziging;
  }

  public String getDossiernr() {
    return dossiernr;
  }

  public void setDossiernr(String dossiernr) {
    this.dossiernr = dossiernr;
  }

  public String getGemeente() {
    return gemeente;
  }

  public void setGemeente(String gemeente) {
    this.gemeente = gemeente;
  }

  public String getResultaatcode() {
    return resultaatcode;
  }

  public void setResultaatcode(String resultaatcode) {
    this.resultaatcode = resultaatcode;
  }

  public String getStatusdossier() {
    return statusdossier;
  }

  public void setStatusdossier(String statusdossier) {
    this.statusdossier = statusdossier;
  }

  public String getTijdstip() {
    return tijdstip;
  }

  public void setTijdstip(String tijdstip) {
    this.tijdstip = tijdstip;
  }

  public String getToelichting() {
    return toelichting;
  }

  public void setToelichting(String toelichting) {
    this.toelichting = toelichting;
  }

  public String getToelichtingResultaat() {
    return toelichtingResultaat;
  }

  public void setToelichtingResultaat(String toelichtingResultaat) {
    this.toelichtingResultaat = toelichtingResultaat;
  }

  public String getVerstuurddoor() {
    return verstuurddoor;
  }

  public void setVerstuurddoor(String verstuurddoor) {
    this.verstuurddoor = verstuurddoor;
  }

  public String getVerwerking() {
    return verwerking;
  }

  public void setVerwerking(String verwerking) {
    this.verwerking = verwerking;
  }
}
