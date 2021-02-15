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

package nl.procura.gba.web.modules.bs.overlijden.lijkvinding.page20;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.TimeField;
import nl.procura.gba.web.services.bs.algemeen.enums.OntvangenDocument;
import nl.procura.gba.web.services.bs.overlijden.lijkvinding.SchriftelijkeAangever;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.field.fieldvalues.TimeFieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page20LijkvindingBean implements Serializable {

  public static final String SCHRIFTELIJKE_AANGEVER = "schriftelijkeAangever";
  public static final String PLAATS_LIJKVINDING     = "plaatsLijkvinding";
  public static final String PLAATS_TOEVOEGING      = "plaatsToevoeging";
  public static final String ONTVANGEN_DOCUMENT     = "ontvangenDocument";
  public static final String DATUM_LIJKVINDING      = "datumLijkvinding";
  public static final String TIJD_LIJKVINDING       = "tijdLijkvinding";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Schriftelijke aangifte door",
      required = true,
      invalidAllowed = false)
  @Select(containerDataSource = AangifteContainer.class,
      nullSelectionAllowed = false)
  private SchriftelijkeAangever schriftelijkeAangever = SchriftelijkeAangever.HULPOFFICIER;

  @Field(type = FieldType.LABEL,
      caption = "Plaats lijkvinding")
  private FieldValue plaatsLijkvinding = null;

  @Field(type = FieldType.TEXT_AREA,
      caption = "Toevoeging",
      required = true)
  @TextArea(rows = 5,
      columns = 50,
      nullRepresentation = "")
  private String plaatsToevoeging = "";

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum Lijkvinding",
      required = true,
      width = "100px")
  private Date datumLijkvinding = null;

  @Field(customTypeClass = TimeField.class,
      caption = "Tijdstip lijkvinding",
      required = true,
      width = "76px")
  @TextField(maxLength = 5)
  @Immediate
  private TimeFieldValue tijdLijkvinding = new TimeFieldValue();

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Ontvangen document",
      required = true)
  @Select(containerDataSource = OntvangenDocumentContainer.class)
  private OntvangenDocument ontvangenDocument = null;
}
