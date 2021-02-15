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

package nl.procura.gba.web.modules.bs.overlijden.lijkvinding.overzicht;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.bs.overlijden.lijkvinding.page20.AangifteContainer;
import nl.procura.gba.web.services.bs.overlijden.lijkvinding.SchriftelijkeAangever;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class LijkvindingOverzichtBean1 implements Serializable {

  public static final String SCHRIFTELIJKEAANGEVER = "schriftelijkeAangever";
  public static final String PLAATSLIJKVINDING     = "plaatsLijkvinding";
  public static final String DOCUMENT              = "document";
  public static final String PARTNER               = "partner";
  public static final String KINDEREN              = "kinderen";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Schriftelijke aangifte door",
      required = true)
  @Select(containerDataSource = AangifteContainer.class)
  private SchriftelijkeAangever schriftelijkeAangever = null;
  @Field(type = FieldType.LABEL,
      caption = "Plaats lijkvinding")
  private String                plaatsLijkvinding     = null;
  @Field(type = FieldType.LABEL,
      caption = "Document")
  private String                document              = "";

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

  public String getKinderen() {
    return kinderen;
  }

  public void setKinderen(String kinderen) {
    this.kinderen = kinderen;
  }

  public String getPartner() {
    return partner;
  }

  public void setPartner(String partner) {
    this.partner = partner;
  }

  public String getPlaatsLijkvinding() {
    return plaatsLijkvinding;
  }

  public void setPlaatsLijkvinding(String plaatsLijkvinding) {
    this.plaatsLijkvinding = plaatsLijkvinding;
  }

  public SchriftelijkeAangever getSchriftelijkeAangever() {
    return schriftelijkeAangever;
  }

  public void setSchriftelijkeAangever(SchriftelijkeAangever schriftelijkeAangever) {
    this.schriftelijkeAangever = schriftelijkeAangever;
  }

}
