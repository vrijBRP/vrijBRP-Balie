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

package nl.procura.gba.web.modules.zaken.verkiezing.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.beheer.verkiezing.Stempas;
import nl.procura.gba.web.services.beheer.verkiezing.Verkiezing;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.ProTextField;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1VerkiezingBean implements Serializable {

  public static final String F_VERKIEZING           = "verkiezing";
  public static final String F_VOLMACHTEN_VERSTREKT = "volmachtenVerstrekt";
  public static final String F_VOLMACHTEN_ONTVANGEN = "volmachtenOntvangen";
  public static final String F_STEMPAS              = "stempas";
  public static final String F_TOEGEVOEGD           = "toegevoegd";
  public static final String F_AAND                 = "aanduiding";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Verkiezing",
      width = "600px",
      required = true)
  @Select(nullSelectionAllowed = false,
      itemCaptionPropertyId = VerkiezingContainer.OMSCHRIJVING)
  private Verkiezing verkiezing;

  @Field(customTypeClass = ProTextField.class,
      caption = "Verstrekt",
      width = "600px",
      readOnly = true)
  private String volmachtenVerstrekt;

  @Field(customTypeClass = ProTextField.class,
      caption = "Volmachten ontvangen",
      width = "600px",
      readOnly = true)
  private String volmachtenOntvangen;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Stempas",
      width = "230px")
  @Select(nullSelectionAllowed = false)
  private Stempas stempas;

  @Field(customTypeClass = ProTextField.class,
      caption = "Stempas handmatig toegevoegd",
      width = "600px",
      readOnly = true)
  private String toegevoegd;

  @Field(customTypeClass = ProTextField.class,
      caption = "Huidige aanduiding",
      width = "600px",
      readOnly = true)
  private String aanduiding;

}
