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

package nl.procura.gba.web.modules.zaken.verkiezing.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.beheer.verkiezing.KiezersregisterActieType;
import nl.procura.gba.web.services.beheer.verkiezing.Stempas;
import nl.procura.gba.web.services.beheer.verkiezing.Verkiezing;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.component.field.ProTextArea;
import nl.procura.vaadin.component.field.ProTextField;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2VerkiezingBean implements Serializable {

  public static final String F_VERKIEZING = "verkiezing";
  public static final String F_STEMPAS    = "stempas";
  public static final String F_AAND       = "aand";
  public static final String F_ACTIE      = "actie";
  public static final String F_MACHT_NAAM = "naam";
  public static final String F_OPM        = "opmerking";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Verkiezing",
      width = "300px",
      required = true)
  @Select(nullSelectionAllowed = false)
  private Verkiezing verkiezing;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Stempas",
      width = "300px")
  @Select(nullSelectionAllowed = false)
  private Stempas stempas;

  @Field(customTypeClass = ProTextField.class,
      caption = "Aanduiding",
      width = "450px",
      readOnly = true)
  private String aanduiding;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Aanduiding in kiezersregister",
      required = true,
      width = "450px")
  private KiezersregisterActieType actie = null;

  @Field(customTypeClass = ProTextField.class,
      caption = "Naam",
      width = "450px",
      readOnly = true)
  private String naam;

  @Field(customTypeClass = ProTextArea.class,
      caption = "Opmerking",
      width = "450px")
  @TextArea(nullRepresentation = "",
      rows = 4)
  private String opmerking;
}
