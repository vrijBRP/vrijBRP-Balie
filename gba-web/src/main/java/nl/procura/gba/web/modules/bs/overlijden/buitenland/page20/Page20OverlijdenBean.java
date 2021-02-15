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

package nl.procura.gba.web.modules.bs.overlijden.buitenland.page20;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.services.bs.algemeen.enums.TypeAfgever;
import nl.procura.gba.web.services.bs.algemeen.enums.TypeBronDocument;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page20OverlijdenBean implements Serializable {

  public static final String DATUMONTVANGST   = "datumOntvangst";
  public static final String ONTVANGSTTYPE    = "ontvangstType";
  public static final String DATUMOVERLIJDEN  = "datumOverlijden";
  public static final String PLAATSOVERLIJDEN = "plaatsOverlijden";
  public static final String LANDOVERLIJDEN   = "landOverlijden";
  public static final String BRONDOCUMENT     = "bronDocument";
  public static final String LANDAFGIFTE      = "landAfgifte";
  public static final String VOLDOETAANEISEN  = "voldoetAanEisen";
  public static final String GERELATEERDEN    = "gerelateerden";

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum ontvangst",
      required = true)
  private FieldValue datumOntvangst = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "van",
      required = true)
  @Select(containerDataSource = TypeAfgeverContainer.class)
  @Immediate
  private TypeAfgever ontvangstType = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum overlijden")
  private FieldValue datumOverlijden = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Plaats overlijden",
      required = true)
  private String plaatsOverlijden = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Land overlijden",
      required = true)
  @Select(containerDataSource = LandContainer.class)
  private FieldValue landOverlijden = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Type brondocument")
  @Select(containerDataSource = TypeBrondocumentContainer.class)
  @Immediate
  private TypeBronDocument bronDocument = null;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Land afgifte",
      required = true)
  @Select(containerDataSource = LandContainer.class)
  @Immediate
  private FieldValue landAfgifte = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Voldoet aan eisen",
      required = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private boolean voldoetAanEisen = false;

  @Field(caption = "Gerelateerde als nabestaande",
      customTypeClass = GbaNativeSelect.class)
  @Immediate
  private FieldValue gerelateerden = new FieldValue("0", "Geen");
}
