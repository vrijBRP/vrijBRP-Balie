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

package nl.procura.gba.web.modules.zaken.vog.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2VogBean3 implements Serializable {

  public static final String DOEL         = "doel";
  public static final String FUNCTIE      = "functie";
  public static final String OMSCHRIJVING = "omschrijving";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Doel")
  private String doel         = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Functie")
  private String functie      = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Omschrijving")
  private String omschrijving = "";

  public String getDoel() {
    return doel;
  }

  public void setDoel(String doel) {
    this.doel = doel;
  }

  public String getFunctie() {
    return functie;
  }

  public void setFunctie(String functie) {
    this.functie = functie;
  }

  public String getOmschrijving() {
    return omschrijving;
  }

  public void setOmschrijving(String omschrijving) {
    this.omschrijving = omschrijving;
  }
}
