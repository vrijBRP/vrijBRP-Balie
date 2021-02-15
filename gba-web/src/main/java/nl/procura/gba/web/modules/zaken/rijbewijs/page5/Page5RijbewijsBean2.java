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

package nl.procura.gba.web.modules.zaken.rijbewijs.page5;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page5RijbewijsBean2 implements Serializable {

  public static final String IDBEWIJS        = "idBewijs";
  public static final String VERBLIJFSTATUS  = "verblijfstatus";
  public static final String NATIONALITEITEN = "nationaliteiten";
  public static final String DATUMINNL       = "datumInNl";
  public static final String DATUMUITNL      = "datumUitNl";
  public static final String UITLAND         = "uitLand";
  public static final String NAARLAND        = "naarLand";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Soort identiteitsbewijs",
      required = true,
      width = "300px")
  @TextField(maxLength = 80)
  private String         idBewijs        = "";
  @Field(type = FieldType.LABEL,
      caption = "Code verblijfstatus")
  private String         verblijfstatus  = "";
  @Field(type = FieldType.LABEL,
      caption = "Nationaliteit(en)",
      width = "500px")
  private String         nationaliteiten = "";
  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum inschr. in NL")
  private DateFieldValue datumInNl       = null;
  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum uitschr. uit NL")
  private DateFieldValue datumUitNl      = null;
  @Field(customTypeClass = GbaComboBox.class,
      caption = "Inschrijving uit land")
  @Select(containerDataSource = LandContainer.class)
  private FieldValue     uitLand         = null;
  @Field(customTypeClass = GbaComboBox.class,
      caption = "Uitschrijving naar land")
  @Select(containerDataSource = LandContainer.class)
  private FieldValue     naarLand        = null;

  public DateFieldValue getDatumInNl() {
    return datumInNl;
  }

  public void setDatumInNl(DateFieldValue datumInNl) {
    this.datumInNl = datumInNl;
  }

  public DateFieldValue getDatumUitNl() {
    return datumUitNl;
  }

  public void setDatumUitNl(DateFieldValue datumUitNl) {
    this.datumUitNl = datumUitNl;
  }

  public String getIdBewijs() {
    return idBewijs;
  }

  public void setIdBewijs(String idBewijs) {
    this.idBewijs = idBewijs;
  }

  public FieldValue getNaarLand() {
    return naarLand;
  }

  public void setNaarLand(FieldValue naarLand) {
    this.naarLand = naarLand;
  }

  public String getNationaliteiten() {
    return nationaliteiten;
  }

  public void setNationaliteiten(String nationaliteiten) {
    this.nationaliteiten = nationaliteiten;
  }

  public FieldValue getUitLand() {
    return uitLand;
  }

  public void setUitLand(FieldValue uitLand) {
    this.uitLand = uitLand;
  }

  public String getVerblijfstatus() {
    return verblijfstatus;
  }

  public void setVerblijfstatus(String verblijfstatus) {
    this.verblijfstatus = verblijfstatus;
  }
}
