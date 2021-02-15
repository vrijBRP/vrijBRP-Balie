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

package nl.procura.gba.web.modules.tabellen.huwelijkslocatie.page4;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatieOptieType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.NumberField;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page4HuwelijksLocatieOptiesBean implements Serializable {

  public static final String OPTIE     = "optie";
  public static final String VERPLICHT = "verplicht";
  public static final String TYPE      = "type";
  public static final String OMS       = "oms";
  public static final String VNR       = "vnr";
  public static final String MIN       = "min";
  public static final String MAX       = "max";
  public static final String ALIAS     = "alias";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Optie",
      width = "400px",
      required = true)
  @TextField(maxLength = 240)
  private String optie = "";

  @Field(customTypeClass = NumberField.class,
      caption = "Volgnummer",
      width = "50px")
  @TextField(nullRepresentation = "")
  private int vnr = 0;

  @Field(customTypeClass = NumberField.class,
      caption = "Minimaal aantal",
      width = "50px",
      required = true)
  private int min = 0;

  @Field(customTypeClass = NumberField.class,
      caption = "Maximaal aantal",
      width = "50px",
      required = true)
  private int max = 0;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Verplicht")
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private boolean verplicht = false;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Type")
  @Select(containerDataSource = Container.class,
      nullSelectionAllowed = false)
  @Immediate
  private HuwelijksLocatieOptieType type = null;

  @Field(type = FieldType.TEXT_AREA,
      caption = "Alias(sen)",
      width = "400px")
  @TextArea(rows = 2,
      maxLength = 1000)
  private String alias = "";

  public static class Container extends ArrayListContainer {

    public Container() {
      super(HuwelijksLocatieOptieType.values(), true);
    }
  }
}
