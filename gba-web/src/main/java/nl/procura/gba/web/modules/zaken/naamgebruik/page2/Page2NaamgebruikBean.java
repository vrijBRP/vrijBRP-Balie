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

package nl.procura.gba.web.modules.zaken.naamgebruik.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2NaamgebruikBean implements Serializable {

  public static final String DATUMWIJZ         = "datumWijz";
  public static final String HUIDIGNAAMGEBRUIK = "huidigNaamgebruik";
  public static final String NIEUWNAAMGEBRUIK  = "nieuwNaamgebruik";

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum ingang",
      required = true)
  private Date datumWijz = null;

  @Field(type = FieldType.LABEL,
      caption = "Huidig naamgebruik")
  private FieldValue huidigNaamgebruik = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Keuze naamgebruik",
      required = true)
  private FieldValue nieuwNaamgebruik = null;
}
