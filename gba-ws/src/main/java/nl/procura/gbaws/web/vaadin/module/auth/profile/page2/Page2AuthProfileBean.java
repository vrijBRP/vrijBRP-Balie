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

package nl.procura.gbaws.web.vaadin.module.auth.profile.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gbaws.db.enums.SearchOrderType;
import nl.procura.gbaws.db.wrappers.GbavProfileWrapper;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.ProTextField;

@FormFieldFactoryBean(accessType = ElementType.FIELD,
    defaultRequiredError = "Veld \"{}\" is verplicht.")
public class Page2AuthProfileBean implements Serializable {

  public static final String PROFILE        = "profile";
  public static final String DESCR          = "descr";
  public static final String CONFIG_GBAV    = "configGbav";
  public static final String SEARCH_ORDER   = "searchOrder";
  public static final String SEARCH_PROCURA = "searchProcura";
  public static final String SEARCH_GBAV    = "searchGbav";

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

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "GBA-V",
      width = "250px")
  @Select(containerDataSource = GbavProfileContainer.class)
  private GbavProfileWrapper configGbav = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Volgorde zoeken",
      required = true,
      width = "250px")
  @Select(nullSelectionAllowed = false,
      containerDataSource = SearchOrderContainer.class)
  private SearchOrderType searchOrder = SearchOrderType.DEFAULT;

  @Field(type = FieldType.CHECK_BOX,
      caption = "Procura database")
  private boolean searchProcura = false;

  @Field(type = FieldType.CHECK_BOX,
      caption = "GBA-V")
  private boolean searchGbav = false;

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

  public SearchOrderType getSearchOrder() {
    return searchOrder;
  }

  public void setSearchOrder(SearchOrderType searchOrder) {
    this.searchOrder = searchOrder;
  }

  public GbavProfileWrapper getConfigGbav() {
    return configGbav;
  }

  public void setConfigGbav(GbavProfileWrapper configGbav) {
    this.configGbav = configGbav;
  }

  public boolean isSearchProcura() {
    return searchProcura;
  }

  public void setSearchProcura(boolean searchProcura) {
    this.searchProcura = searchProcura;
  }

  public boolean isSearchGbav() {
    return searchGbav;
  }

  public void setSearchGbav(boolean searchGbav) {
    this.searchGbav = searchGbav;
  }
}
