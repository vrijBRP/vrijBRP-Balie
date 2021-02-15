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

package nl.procura.gba.web.modules.bs.ontbinding.page20;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.containers.PlaatsContainer;
import nl.procura.gba.web.components.containers.SoortVerbintenisContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page20OntbindingBean1 implements Serializable {

  public static final String SOORT     = "soort";
  public static final String DATUM     = "datum";
  public static final String LAND      = "land";
  public static final String PLAATS_NL = "plaatsNL";
  public static final String PLAATS_BL = "plaatsBuitenland";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Soort",
      required = true)
  @Select(containerDataSource = SoortVerbintenisContainer.class)
  private SoortVerbintenis soort = null;

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum sluiting",
      required = true,
      width = "97px")
  private Date datum = null;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Land sluiting",
      required = true,
      width = "250px")
  @Select(containerDataSource = LandContainer.class)
  private FieldValue land = new FieldValue("6030");

  @Field(customTypeClass = GbaTextField.class,
      caption = "Plaats sluiting",
      width = "250px")
  @TextField(nullRepresentation = "")
  private String plaatsBuitenland = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Plaats sluiting",
      width = "250px")
  @Select(containerDataSource = PlaatsContainer.class)
  private FieldValue plaatsNL = null;

  public Date getDatum() {
    return datum;
  }

  public void setDatum(Date datum) {
    this.datum = datum;
  }

  public FieldValue getLand() {
    return land;
  }

  public void setLand(FieldValue land) {
    this.land = land;
  }

  public String getPlaatsBuitenland() {
    return plaatsBuitenland;
  }

  public void setPlaatsBuitenland(String plaatsBuitenland) {
    this.plaatsBuitenland = plaatsBuitenland;
  }

  public FieldValue getPlaatsNL() {
    return plaatsNL;
  }

  public void setPlaatsNL(FieldValue plaatsNL) {
    this.plaatsNL = plaatsNL;
  }

  public SoortVerbintenis getSoort() {
    return soort;
  }

  public void setSoort(SoortVerbintenis soort) {
    this.soort = soort;
  }
}
