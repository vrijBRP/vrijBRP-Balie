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

package nl.procura.gba.web.modules.bs.overlijden.gemeente.overzicht;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class OverlijdenGemeenteOverzichtBean1 implements Serializable {

  public static final String NAAM     = "naam";
  public static final String GEBOREN  = "geboren";
  public static final String DOCUMENT = "document";
  public static final String PARTNER  = "partner";
  public static final String KINDEREN = "kinderen";

  @Field(type = FieldType.LABEL,
      caption = "Naam")
  private String naam = "";

  @Field(type = FieldType.LABEL,
      caption = "Geboren")
  private String geboren = "";

  @Field(type = FieldType.LABEL,
      caption = "Document")
  private String document = "";

  @Field(type = FieldType.LABEL,
      caption = "Partner")
  private String partner = "";

  @Field(type = FieldType.LABEL,
      caption = "Minderjarige kinderen")
  private String kinderen = "";

  public String getDocument() {
    return document;
  }

  public void setDocument(String document) {
    this.document = document;
  }

  public String getGeboren() {
    return geboren;
  }

  public void setGeboren(String geboren) {
    this.geboren = geboren;
  }

  public String getKinderen() {
    return kinderen;
  }

  public void setKinderen(String kinderen) {
    this.kinderen = kinderen;
  }

  public String getNaam() {
    return naam;
  }

  public void setNaam(String naam) {
    this.naam = naam;
  }

  public String getPartner() {
    return partner;
  }

  public void setPartner(String partner) {
    this.partner = partner;
  }
}
