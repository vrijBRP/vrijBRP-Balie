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

package nl.procura.gba.web.modules.bs.common.pages.residentpage;

import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.component.field.BsnField;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class ResidentPageBean {

  public static final String F_BSN               = "bsn";
  public static final String F_GEBOORTEDATUM     = "geboortedatum";
  public static final String F_GESLACHTSNAAM     = "geslachtsnaam";
  public static final String F_NIEUW_ADRES       = "nieuwAdres";
  public static final String F_TERUGMELDING      = "terugmelding";
  public static final String F_TOESTEMMING_GEVER = "toestemmingGever";

  @Field(customTypeClass = BsnField.class, caption = "BSN")
  private FieldValue bsn           = new FieldValue();
  @Field(customTypeClass = DatumVeld.class, caption = "Geboortedatum")
  private FieldValue geboortedatum = new FieldValue();
  @Field(type = FieldType.TEXT_FIELD, caption = "Geslachtsnaam", width = "230px")
  private String     geslachtsnaam = "";

  @Field(type = FieldType.LABEL, caption = "Nieuw adres")
  private String nieuwAdres   = "";
  @Field(type = FieldType.TEXT_AREA, caption = "Terugmelding", width = "400px", required = true)
  @TextArea(rows = 3)
  private String terugmelding = "";

  @Field(type = FieldType.LABEL, caption = "Toestemminggever")
  private String toestemmingGever = "";
}
