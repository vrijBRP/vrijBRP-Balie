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

package nl.procura.gba.web.modules.hoofdmenu.raas.tab2.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Tab2RaasPage2Bean implements Serializable {

  public static final String CODE           = "code";
  public static final String AANVRAAGNUMMER = "aanvraagNummer";
  public static final String DATUM_TIJD     = "datumTijd";
  public static final String NAAM           = "naam";
  public static final String STATUS         = "status";
  public static final String RICHTING       = "richting";
  public static final String TYPE           = "type";

  @Field(type = FieldType.LABEL,
      caption = "Code")
  private Object code = "";

  @Field(type = FieldType.LABEL,
      caption = "Aanvraagnummer")
  private Object aanvraagNummer = "";

  @Field(type = FieldType.LABEL,
      caption = "Datum / tijd")
  private Object datumTijd = "";

  @Field(type = FieldType.LABEL,
      caption = "Bestandsnaam")
  private Object naam = "";

  @Field(type = FieldType.LABEL,
      caption = "Status")
  private Object status = "";

  @Field(type = FieldType.LABEL,
      caption = "Richting")
  private Object richting = "";

  @Field(type = FieldType.LABEL,
      caption = "Type")
  private Object type = "";
}
