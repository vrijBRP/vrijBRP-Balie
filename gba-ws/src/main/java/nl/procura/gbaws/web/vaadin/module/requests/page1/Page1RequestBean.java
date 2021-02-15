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

package nl.procura.gbaws.web.vaadin.module.requests.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gbaws.db.wrappers.UsrWrapper;
import nl.procura.gbaws.web.vaadin.module.requests.page1.periodes.Periode;
import nl.procura.gbaws.web.vaadin.module.requests.page1.periodes.PeriodesContainer;
import nl.procura.gbaws.web.vaadin.module.requests.page1.periodes.Vandaag;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.ProTextField;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD,
    defaultRequiredError = "Veld \"{}\" is verplicht.")
public class Page1RequestBean implements Serializable {

  public static final String PERIODE   = "periode";
  public static final String GEBRUIKER = "gebruiker";
  public static final String VAN       = "van";
  public static final String TM        = "tm";
  public static final String ZOEKEN    = "zoeken";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Periode",
      width = "270px",
      required = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = PeriodesContainer.class)
  @Immediate()
  private Periode periode = new Vandaag();

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Gebruiker",
      width = "270px")
  @Select(nullSelectionAllowed = true)
  @Immediate()
  private UsrWrapper gebruiker = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "Van",
      width = "80px",
      required = true,
      visible = false)
  private DateFieldValue van = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "t/m",
      width = "80px",
      required = true,
      visible = false)
  private DateFieldValue tm = null;

  @Field(customTypeClass = ProTextField.class,
      caption = "Zoekterm",
      width = "270px")
  private String zoeken = "";

  public Periode getPeriode() {
    return periode;
  }

  public void setPeriode(Periode periode) {
    this.periode = periode;
  }

  public DateFieldValue getVan() {
    return van;
  }

  public void setVan(DateFieldValue van) {
    this.van = van;
  }

  public DateFieldValue getTm() {
    return tm;
  }

  public void setTm(DateFieldValue tm) {
    this.tm = tm;
  }

  public String getZoeken() {
    return zoeken;
  }

  public void setZoeken(String zoeken) {
    this.zoeken = zoeken;
  }

  public UsrWrapper getGebruiker() {
    return gebruiker;
  }

  public void setGebruiker(UsrWrapper gebruiker) {
    this.gebruiker = gebruiker;
  }
}
