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

package nl.procura.gba.web.modules.zaken.inhouding.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.field.ProDateField;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2InhoudingBean1 implements Serializable {

  public static final String NUMMER         = "nummer";
  public static final String SOORT          = "soort";
  public static final String VANTM          = "vanTm";
  public static final String AUTORITEIT     = "autoriteit";
  public static final String INHOUDING      = "inhouding";
  public static final String DATUM          = "datum";
  public static final String DATUM_READONLY = "datumReadOnly";

  @Field(type = FieldType.LABEL,
      caption = "Nummer")
  private String nummer = "";

  @Field(type = FieldType.LABEL,
      caption = "Soort")
  private String soort = "";

  @Field(type = FieldType.LABEL,
      caption = "Van / tm")
  private String vanTm = "";

  @Field(type = FieldType.LABEL,
      caption = "Autoriteit")
  private String autoriteit = "";

  @Field(type = FieldType.LABEL,
      caption = "Type aanvraag")
  private String inhouding = "";

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum geldigheid",
      width = "97px",
      required = true)
  private Date datum = null;

  @Field(type = FieldType.LABEL,
      caption = "Datum geldigheid")
  private String datumReadOnly = "";
}
