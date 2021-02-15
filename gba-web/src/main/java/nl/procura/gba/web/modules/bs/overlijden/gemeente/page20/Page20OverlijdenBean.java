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

package nl.procura.gba.web.modules.bs.overlijden.gemeente.page20;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.TimeField;
import nl.procura.gba.web.services.bs.algemeen.enums.OntvangenDocument;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.field.fieldvalues.TimeFieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page20OverlijdenBean implements Serializable {

  public static final String PLAATS_OVERLIJDEN  = "plaatsOverlijden";
  public static final String DATUM_OVERLIJDEN   = "datumOverlijden";
  public static final String TIJD_OVERLIJDEN    = "tijdOverlijden";
  public static final String ONTVANGEN_DOCUMENT = "ontvangenDocument";

  @Field(type = FieldType.LABEL,
      caption = "Plaats overlijden")
  private FieldValue plaatsOverlijden = null;

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum overlijden",
      description = "Datum overlijden",
      required = true,
      width = "100px")
  private Date datumOverlijden = null;

  @Field(customTypeClass = TimeField.class,
      caption = "Tijdstip overlijden",
      width = "76px")
  @TextField(maxLength = 5)
  @Immediate
  private TimeFieldValue tijdOverlijden = new TimeFieldValue();

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Ontvangen document",
      required = true)
  @Select(containerDataSource = OntvangenDocumentContainer.class)
  private OntvangenDocument ontvangenDocument = null;
}
