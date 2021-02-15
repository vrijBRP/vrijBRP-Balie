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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.tab3;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.container.NLBooleanContainer;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page8ZakenTab3Bean implements Serializable {

  public static final String DOCUMENT      = "document";
  public static final String STATUS        = "status";
  public static final String UITVOER       = "uitvoer";
  public static final String UITVOER_LABEL = "uitvoerLabel";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Begeleidende brief")
  private DocumentRecord document = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Uitvoer naar",
      required = true,
      requiredError = "{}: verplicht veld")
  @Immediate()
  @Select(nullSelectionAllowed = false)
  private PrintOptie uitvoer = null;

  @Field(type = FieldType.LABEL,
      caption = "Uitvoer naar")
  private String uitvoerLabel = "dezelfde uitvoer als het uittreksel";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Op verwerkt na afdrukken")
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private Boolean status = true;
}
