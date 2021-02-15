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

package nl.procura.gba.web.modules.zaken.correspondentie.page3;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page3CorrespondentieBean implements Serializable {

  public static final String OMSCHRIJVING = "omschrijving";
  public static final String TOELICHTING  = "toelichting";
  public static final String AFSLUIT_TYPE = "afsluitType";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Omschrijving",
      width = "600px")
  private String omschrijving = "";

  @Field(type = FieldType.LABEL,
      caption = "Type van afsluiting",
      width = "600px")
  private String afsluitType = "";

  @Field(type = FieldType.TEXT_AREA,
      caption = "Toelichting",
      width = "600px")
  private String toelichting = "";
}
