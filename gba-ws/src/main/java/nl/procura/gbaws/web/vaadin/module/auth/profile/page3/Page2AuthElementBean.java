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

package nl.procura.gbaws.web.vaadin.module.auth.profile.page3;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD,
    defaultRequiredError = "Veld \"{}\" is verplicht.")
public class Page2AuthElementBean implements Serializable {

  public static final String PROFILE      = "profile";
  public static final String DATABASE     = "database";
  public static final String GBAV_ACCOUNT = "gbavAccount";
  public static final String BRP_ACCOUNT  = "brpAccount";

  @Field(type = FieldType.LABEL,
      caption = "Profiel")
  private String profile = "";

  @Field(type = FieldType.LABEL,
      caption = "Database")
  private String database = "";

  @Field(type = FieldType.LABEL,
      caption = "GBA-V account")
  private String gbavAccount = "";

  @Field(type = FieldType.LABEL,
      caption = "BRP account")
  private String brpAccount = "";

  public String getProfile() {
    return profile;
  }

  public void setProfile(String profile) {
    this.profile = profile;
  }

  public String getDatabase() {
    return database;
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  public String getGbavAccount() {
    return gbavAccount;
  }

  public void setGbavAccount(String gbavAccount) {
    this.gbavAccount = gbavAccount;
  }

  public String getBrpAccount() {
    return brpAccount;
  }

  public void setBrpAccount(String brpAccount) {
    this.brpAccount = brpAccount;
  }
}
