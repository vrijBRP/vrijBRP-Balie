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

package nl.procura.gba.web.modules.zaken.gpk.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.zaken.gpk.GpkKaartType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.ProDateField;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2GpkBean implements Serializable {

  public static final String NUMMER      = "nummer";
  public static final String KAART       = "kaart";
  public static final String VERVALDATUM = "vervalDatum";
  public static final String AFGEVER     = "afgever";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Nummer",
      required = true,
      width = "180px")
  private String nummer = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Kaarttype",
      required = true,
      width = "180px")
  @Select(nullSelectionAllowed = false,
      containerDataSource = GpkKaartTypeContainer.class)
  private GpkKaartType kaart = null;

  @Field(customTypeClass = ProDateField.class,
      caption = "Vervaldatum",
      required = true,
      width = "180px")
  private Date vervalDatum = null;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Afgegeven door",
      readOnly = true,
      width = "300px")
  private String afgever = "";
}
