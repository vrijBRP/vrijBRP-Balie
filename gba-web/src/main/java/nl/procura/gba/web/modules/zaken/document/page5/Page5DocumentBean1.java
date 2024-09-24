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

package nl.procura.gba.web.modules.zaken.document.page5;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaBsnField;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page5DocumentBean1 implements Serializable {

  public static final String ADRES         = "adres";
  public static final String BSN           = "bsn";
  public static final String GEBOORTEDATUM = "geboortedatum";
  public static final String GESLACHTSNAAM = "geslachtsnaam";

  @Field(type = FieldType.LABEL,
      caption = "Adres")
  private String     adres         = "";
  @Field(customTypeClass = GbaBsnField.class,
      caption = "BSN")
  private FieldValue bsn           = new FieldValue();
  @Field(customTypeClass = DatumVeld.class,
      caption = "Geboortedatum")
  private FieldValue geboortedatum = new FieldValue();
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geslachtsnaam")
  private String     geslachtsnaam = "";

  public String getAdres() {
    return adres;
  }

  public void setAdres(String adres) {
    this.adres = adres;
  }

  public FieldValue getBsn() {
    return bsn;
  }

  public void setBsn(FieldValue bsn) {
    this.bsn = bsn;
  }

  public FieldValue getGeboortedatum() {
    return geboortedatum;
  }

  public void setGeboortedatum(FieldValue geboortedatum) {
    this.geboortedatum = geboortedatum;
  }

  public String getGeslachtsnaam() {
    return geslachtsnaam;
  }

  public void setGeslachtsnaam(String geslachtsnaam) {
    this.geslachtsnaam = geslachtsnaam;
  }
}
