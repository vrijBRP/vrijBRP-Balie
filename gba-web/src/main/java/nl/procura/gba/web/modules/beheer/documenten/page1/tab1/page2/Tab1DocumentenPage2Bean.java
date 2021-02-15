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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.beheer.overig.MapValidator;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.documenten.DocumentVertrouwelijkheid;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Tab1DocumentenPage2Bean implements Serializable {

  public static final String CODE              = "code";
  public static final String VOLGNR            = "volgnr";
  public static final String NAAM              = "naam";
  public static final String SJABLOON          = "sjabloon";
  public static final String TYPE              = "type";
  public static final String MAP               = "map";
  public static final String VERVALDATUM       = "vervaldatum";
  public static final String OMSCHRIJVING      = "omschrijving";
  public static final String AANTAL            = "aantal";
  public static final String DMSNAAM           = "dmsNaam";
  public static final String VERTROUWELIJKHEID = "vertrouwelijkheid";

  @Field(type = FieldType.LABEL,
      caption = "Code")
  private String code = "";

  @Field(customTypeClass = NumberField.class,
      caption = "Volgnr.",
      width = "50px")
  @TextField(nullRepresentation = "",
      maxLength = 5)
  private String volgnr;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Naam",
      required = true,
      width = "350px")
  private String naam = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Sjabloon",
      width = "350px",
      required = true)
  @Immediate
  private String sjabloon = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Type",
      width = "350px",
      required = true)
  private DocumentType type = DocumentType.ONBEKEND;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Map",
      width = "350px",
      validators = MapValidator.class)
  @Immediate
  private String map = "";

  @Field(customTypeClass = DatumVeld.class,
      caption = "Einddatum",
      width = "100px")
  private DateFieldValue vervaldatum;

  @Field(type = FieldType.TEXT_AREA,
      caption = "Omschrijving",
      width = "350px")
  @TextArea(nullRepresentation = "",
      rows = 3)
  private String omschrijving = "";

  @Field(customTypeClass = NumberField.class,
      caption = "Aantal per keer",
      description = "Het aantal afdrukken van dit document per printactie.",
      width = "40px")
  @TextField(maxLength = 1)
  private String aantal = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "DMS-naam",
      width = "350px")
  @TextField(maxLength = 100)
  private String dmsNaam = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Vertrouwelijkheid",
      width = "350px")
  @Immediate
  private DocumentVertrouwelijkheid vertrouwelijkheid;
}
