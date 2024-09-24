/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.behandelaar.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.InputPrompt;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextArea;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2ZaakBehandelaarBean implements Serializable {

  public static final String DATUM_TIJD          = "datumTijd";
  public static final String BEHANDELAAR         = "behandelaar";
  public static final String BEHANDELAAR_LABEL   = "behandelaarLabel";
  public static final String TOEWEZEN_DOOR_LABEL = "toegewezenDoorLabel";
  public static final String OPMERKING           = "opmerking";

  @Field(type = FieldType.LABEL,
      caption = "Datum / tijd")
  private DateTime datumTijd = null;

  @Field(type = FieldType.LABEL,
      caption = "Behandelaar")
  private UsrFieldValue behandelaarLabel;

  @Field(type = FieldType.LABEL,
      caption = "Toegewezen door")
  private UsrFieldValue toegewezenDoorLabel;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Behandelaar",
      width = "300px",
      required = true)
  @Select(nullSelectionAllowed = false)
  private UsrFieldValue behandelaar;

  @Field(type = FieldType.TEXT_AREA,
      caption = "Opmerking",
      width = "300px")
  @TextArea(rows = 3,
      maxLength = 250,
      nullRepresentation = "")
  @InputPrompt(text = "Opmerking (optioneel)")
  private String opmerking;
}
