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

package nl.procura.gbaws.web.vaadin.module.auth.usr.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gbaws.db.wrappers.ProfileWrapper;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.ProTextField;

@FormFieldFactoryBean(accessType = ElementType.FIELD,
    defaultRequiredError = "Veld \"{}\" is verplicht.")
public class Page2AuthUsrBean implements Serializable {

  public static final String NAME    = "name";
  public static final String DISPLAY = "display";
  public static final String PW      = "password";
  public static final String ADMIN   = "admin";
  public static final String PROFILE = "profile";

  @Field(customTypeClass = ProTextField.class,
      width = "400px",
      caption = "Gebruikersnaam",
      required = true)
  @TextField(nullRepresentation = "",
      maxLength = 250)
  private String name = "";

  @Field(customTypeClass = ProTextField.class,
      width = "400px",
      caption = "Volledige naam",
      required = true)
  @TextField(nullRepresentation = "",
      maxLength = 250)
  private String display = "";

  @Field(customTypeClass = ProTextField.class,
      width = "400px",
      caption = "Wachtwoord")
  @TextField(nullRepresentation = "",
      maxLength = 250,
      secret = true)
  private String password = "";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Beheerder",
      required = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  private Boolean admin = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Profiel")
  @Select(containerDataSource = ProfileContainer.class)
  private ProfileWrapper profile = null;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDisplay() {
    return display;
  }

  public void setDisplay(String display) {
    this.display = display;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Boolean getAdmin() {
    return admin;
  }

  public void setAdmin(Boolean admin) {
    this.admin = admin;
  }

  public ProfileWrapper getProfile() {
    return profile;
  }

  public void setProfile(ProfileWrapper profile) {
    this.profile = profile;
  }
}
