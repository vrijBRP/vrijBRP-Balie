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

package nl.procura.gba.web.modules.zaken.rijbewijs.page3;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.field.AnrField;
import nl.procura.vaadin.component.field.BsnField;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page3RijbewijsBean1 implements Serializable {

  public static final String ANR           = "anr";
  public static final String BSN           = "bsn";
  public static final String GESLACHTSNAAM = "geslachtsnaam";
  public static final String VOORNAMEN     = "voornamen";
  public static final String GEBOORTEDATUM = "geboortedatum";

  @Field(customTypeClass = AnrField.class,
      caption = "Anr",
      required = true)
  private AnrFieldValue     anr           = new AnrFieldValue();
  @Field(customTypeClass = BsnField.class,
      caption = "BSN",
      required = true)
  private BsnFieldValue     bsn           = new BsnFieldValue();
  @Field(customTypeClass = GbaTextField.class,
      caption = "Geslachtsnaam",
      width = "300px")
  private String            geslachtsnaam = "";
  @Field(customTypeClass = GbaTextField.class,
      caption = "Voornamen",
      width = "300px")
  private String            voornamen     = "";
  @Field(customTypeClass = GbaDateField.class,
      caption = "Geboortedatum",
      required = true)
  private GbaDateFieldValue geboortedatum = new GbaDateFieldValue();

  public AnrFieldValue getAnr() {
    return anr;
  }

  public void setAnr(AnrFieldValue anr) {
    this.anr = anr;
  }

  public BsnFieldValue getBsn() {
    return bsn;
  }

  public void setBsn(BsnFieldValue bsn) {
    this.bsn = bsn;
  }

  public GbaDateFieldValue getGeboortedatum() {
    return geboortedatum;
  }

  public void setGeboortedatum(GbaDateFieldValue geboortedatum) {
    this.geboortedatum = geboortedatum;
  }

  public String getGeslachtsnaam() {
    return geslachtsnaam;
  }

  public void setGeslachtsnaam(String geslachtsnaam) {
    this.geslachtsnaam = geslachtsnaam;
  }

  public String getVoornamen() {
    return voornamen;
  }

  public void setVoornamen(String voornamen) {
    this.voornamen = voornamen;
  }
}
