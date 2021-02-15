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

package nl.procura.gbaws.web.vaadin.module.sources.gbav.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.jpa.personenws.db.GbavProfileType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.ProTextField;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD,
    defaultRequiredError = "Veld \"{}\" is verplicht.")
public class Page2DbGbavBean implements Serializable {

  public static final String PROFILE         = "profile";
  public static final String DESCR           = "descr";
  public static final String BLOCK           = "block";
  public static final String DATE_CHANGED    = "dateChanged";
  public static final String DATE_EXPIRATION = "dateExpiration";
  public static final String TYPE            = "type";
  public static final String URL_PW          = "urlPassword";
  public static final String URL_REQUEST     = "urlRequest";
  public static final String URL_INDICATIES  = "urlIndicaties";
  public static final String USERNAME        = "username";
  public static final String PW              = "password";

  @Field(customTypeClass = ProTextField.class,
      width = "400px",
      caption = "Naam",
      required = true)
  @TextField(nullRepresentation = "",
      maxLength = 250)
  private String profile = "";

  @Field(customTypeClass = ProTextField.class,
      width = "400px",
      caption = "Omschrijving",
      required = true)
  @TextField(nullRepresentation = "",
      maxLength = 250)
  private String descr = "";

  @Field(type = FieldType.LABEL,
      caption = "Geblokkeerd")
  private String block = "";

  @Field(customTypeClass = DatumVeld.class,
      caption = "Aangepast op",
      width = "120px",
      required = true)
  private DateFieldValue dateChanged = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Type",
      width = "250px",
      required = true)
  @Select(containerDataSource = GbavProfileTypeContainer.class)
  private GbavProfileType type = null;

  @Field(customTypeClass = ProTextField.class,
      width = "600px",
      caption = "Endpoint voor wachtwoord",
      required = true)
  @TextField(nullRepresentation = "",
      maxLength = 250)
  private String urlPassword = "";

  @Field(customTypeClass = ProTextField.class,
      width = "600px",
      caption = "Endpoint voor zoeken",
      required = true)
  @TextField(nullRepresentation = "",
      maxLength = 250)
  private String urlRequest = "";

  @Field(customTypeClass = ProTextField.class,
      width = "600px",
      caption = "Endpoint voor indicaties")
  @TextField(nullRepresentation = "",
      maxLength = 250)
  private String urlIndicaties = "";

  @Field(customTypeClass = ProTextField.class,
      width = "120px",
      caption = "GBA-V gebruikersnaam",
      required = true)
  @TextField(nullRepresentation = "",
      maxLength = 250)
  private String username = "";

  @Field(customTypeClass = ProTextField.class,
      width = "120px",
      caption = "GBA-V wachtwoord",
      required = true)
  @TextField(nullRepresentation = "",
      maxLength = 250,
      secret = true)
  private String password = "";

  public String getProfile() {
    return profile;
  }

  public void setProfile(String profile) {
    this.profile = profile;
  }

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  public String getBlock() {
    return block;
  }

  public void setBlock(String block) {
    this.block = block;
  }

  public DateFieldValue getDateChanged() {
    return dateChanged;
  }

  public void setDateChanged(DateFieldValue dateChanged) {
    this.dateChanged = dateChanged;
  }

  public GbavProfileType getType() {
    return type;
  }

  public void setType(GbavProfileType type) {
    this.type = type;
  }

  public String getUrlPassword() {
    return urlPassword;
  }

  public void setUrlPassword(String urlPassword) {
    this.urlPassword = urlPassword;
  }

  public String getUrlRequest() {
    return urlRequest;
  }

  public void setUrlRequest(String urlRequest) {
    this.urlRequest = urlRequest;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUrlIndicaties() {
    return urlIndicaties;
  }

  public void setUrlIndicaties(String urlIndicaties) {
    this.urlIndicaties = urlIndicaties;
  }
}
