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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.task.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.zaken.algemeen.tasks.TaskExecutionType;
import nl.procura.gba.web.services.zaken.algemeen.tasks.TaskStatusType;
import nl.procura.gba.web.services.zaken.algemeen.tasks.TaskType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.annotation.field.TextField;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2TaskBean implements Serializable {

  public static final String EVENT           = "event";
  public static final String ZAAK            = "zaak";
  public static final String TASK_TYPE       = "taskType";
  public static final String OMS             = "oms";
  public static final String UITVOERDER_TYPE = "uitvoerderType";
  public static final String UITVOERDER      = "uitvoerder";
  public static final String STATUS          = "status";
  public static final String OPMERKINGEN     = "opmerkingen";

  @Field(type = FieldType.LABEL,
      caption = "Aanleiding",
      width = "500px")
  private Object event;

  @Field(type = FieldType.LABEL,
      caption = "Zaak",
      width = "500px")
  private Object zaak;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Taak",
      width = "500px")
  @Select(nullSelectionAllowed = false,
      containerDataSource = TaskTypeContainer.class)
  private TaskType taskType;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Omschrijving",
      required = true,
      width = "500px")
  @TextField(nullRepresentation = "",
      maxLength = 255)
  private String oms = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Uitvoerder",
      width = "150px",
      required = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = TaskUitvoerderContainer.class)
  private TaskExecutionType uitvoerderType;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Gebruiker",
      width = "334px",
      required = true)
  private UsrFieldValue uitvoerder;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Status taak",
      width = "100px",
      required = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = TaskStatusContainer.class)
  private TaskStatusType status;

  @Field(type = FieldType.TEXT_AREA,
      width = "500px",
      caption = "Opmerkingen")
  @TextArea(nullRepresentation = "",
      rows = 10)
  private String opmerkingen;
}
