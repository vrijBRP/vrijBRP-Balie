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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.zkndms.page4;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.annotation.field.TextField;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page4ZknDmsStatusBean implements Serializable {

  public static final String VOLGNR         = "volgnr";
  public static final String STATUS         = "status";
  public static final String STATUS_ACTUEEL = "statusActueel";
  public static final String TIJDSTIP       = "tijdstip";
  public static final String IS_GEZET_DOOR  = "isGezetDoor";
  public static final String TOELICHTING    = "toelichting";

  @Field(type = FieldType.LABEL,
      caption = "Volgnummer",
      width = "600px")
  @TextField(nullRepresentation = "")
  private String volgnr = null;

  @Field(type = FieldType.LABEL,
      caption = "Status",
      width = "600px")
  @TextField(nullRepresentation = "")
  private String status = null;

  @Field(type = FieldType.LABEL,
      caption = "Momenteel actueel",
      width = "600px")
  @TextField(nullRepresentation = "")
  private String statusActueel = null;

  @Field(type = FieldType.LABEL,
      caption = "Tijdstip",
      width = "600px")
  @TextField(nullRepresentation = "")
  private String tijdstip = null;

  @Field(type = FieldType.LABEL,
      caption = "Is gezet door",
      width = "600px")
  @TextField(nullRepresentation = "")
  private String isGezetDoor = null;

  @Field(type = FieldType.TEXT_AREA,
      caption = "Toelichting",
      width = "600px",
      readOnly = true)
  @TextArea(nullRepresentation = "")
  private String toelichting = null;
}
