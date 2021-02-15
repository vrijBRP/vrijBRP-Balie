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

package nl.procura.gba.web.modules.bs.omzetting.overzicht.form1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class OmzettingOverzichtBean1 implements Serializable {

  public static final String LOCATIE            = "locatie";
  public static final String LOCATIEDATUMTIJD   = "locatieDatumTijd";
  public static final String LOCATIESTATUS      = "locatieStatus";
  public static final String LOCATIEOPTIES      = "locatieOpties";
  public static final String LOCATIETOELICHTING = "locatieToelichting";
  public static final String GEMEENTEGETUIGEN   = "gemeenteGetuigen";
  public static final String GETUIGEN           = "getuigen";

  @Field(type = FieldType.LABEL,
      caption = "Locatie")
  private String locatie            = "";
  @Field(type = FieldType.LABEL,
      caption = "Datum / tijd")
  private String locatieDatumTijd   = "";
  @Field(type = FieldType.LABEL,
      caption = "Status")
  private String locatieStatus      = "";
  @Field(type = FieldType.LABEL,
      caption = "Opties")
  private String locatieOpties      = "";
  @Field(type = FieldType.LABEL,
      caption = "Toelichting")
  private String locatieToelichting = "";

  @Field(type = FieldType.LABEL,
      caption = "Getuigen")
  private String getuigen         = "";
  @Field(type = FieldType.LABEL,
      caption = "Gemeente getuigen")
  private String gemeenteGetuigen = "";

  public String getGemeenteGetuigen() {
    return gemeenteGetuigen;
  }

  public void setGemeenteGetuigen(String gemeenteGetuigen) {
    this.gemeenteGetuigen = gemeenteGetuigen;
  }

  public String getGetuigen() {
    return getuigen;
  }

  public void setGetuigen(String getuigen) {
    this.getuigen = getuigen;
  }

  public String getLocatie() {
    return locatie;
  }

  public void setLocatie(String locatie) {
    this.locatie = locatie;
  }

  public String getLocatieDatumTijd() {
    return locatieDatumTijd;
  }

  public void setLocatieDatumTijd(String locatieDatumTijd) {
    this.locatieDatumTijd = locatieDatumTijd;
  }

  public String getLocatieOpties() {
    return locatieOpties;
  }

  public void setLocatieOpties(String locatieOpties) {
    this.locatieOpties = locatieOpties;
  }

  public String getLocatieStatus() {
    return locatieStatus;
  }

  public void setLocatieStatus(String locatieStatus) {
    this.locatieStatus = locatieStatus;
  }

  public String getLocatieToelichting() {
    return locatieToelichting;
  }

  public void setLocatieToelichting(String locatieToelichting) {
    this.locatieToelichting = locatieToelichting;
  }
}
