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

package nl.procura.gba.web.modules.bs.erkenning.overzicht.buiten;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class ErkenningOverzichtBean1 implements Serializable {

  public static final String AKTENR                = "aktenr";
  public static final String DATUM                 = "datum";
  public static final String TOESTEMMINGGEVER_TYPE = "toestemminggeverType";
  public static final String NAAMSKEUZE            = "naamskeuze";
  public static final String NAAMSAANDUIDING_TYPE  = "naamsAanduidingType";
  public static final String AFSTAMMINGSRECHT      = "afstammingsrecht";
  public static final String GEMEENTE              = "gemeente";
  public static final String LAND                  = "land";
  public static final String PLAATS                = "plaats";

  @Field(type = FieldType.LABEL,
      caption = "Akte")
  private String aktenr = "";

  @Field(type = FieldType.LABEL,
      caption = "Datum")
  private String datum = "";

  @Field(type = FieldType.LABEL,
      caption = "Toestemminggever")
  private String toestemminggeverType = "";

  @Field(type = FieldType.LABEL,
      caption = "Naamskeuze")
  private String naamskeuze = "";

  @Field(type = FieldType.LABEL,
      caption = "Geslachtsnaam")
  private String naamsAanduidingType = "";

  @Field(type = FieldType.LABEL,
      caption = "Toegepast recht van")
  private String afstammingsrecht = "";

  @Field(type = FieldType.LABEL,
      caption = "Plaats")
  private String plaats = "";

  public String getAfstammingsrecht() {
    return afstammingsrecht;
  }

  public void setAfstammingsrecht(String afstammingsrecht) {
    this.afstammingsrecht = afstammingsrecht;
  }

  public String getAktenr() {
    return aktenr;
  }

  public void setAktenr(String aktenr) {
    this.aktenr = aktenr;
  }

  public String getDatum() {
    return datum;
  }

  public void setDatum(String datum) {
    this.datum = datum;
  }

  public String getNaamsAanduidingType() {
    return naamsAanduidingType;
  }

  public void setNaamsAanduidingType(String naamsAanduidingType) {
    this.naamsAanduidingType = naamsAanduidingType;
  }

  public String getNaamskeuze() {
    return naamskeuze;
  }

  public void setNaamskeuze(String naamskeuze) {
    this.naamskeuze = naamskeuze;
  }

  public String getPlaats() {
    return plaats;
  }

  public void setPlaats(String plaats) {
    this.plaats = plaats;
  }

  public String getToestemminggeverType() {
    return toestemminggeverType;
  }

  public void setToestemminggeverType(String toestemminggeverType) {
    this.toestemminggeverType = toestemminggeverType;
  }
}
