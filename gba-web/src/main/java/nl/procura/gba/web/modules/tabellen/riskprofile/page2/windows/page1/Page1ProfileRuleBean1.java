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

package nl.procura.gba.web.modules.tabellen.riskprofile.page2.windows.page1;

import static nl.procura.vaadin.component.container.ProcuraContainer.OMSCHRIJVING;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.math.BigDecimal;

import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.field.PosNumberField;
import nl.procura.vaadin.component.field.ProNativeSelect;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1ProfileRuleBean1 implements Serializable {

  public static final String F_NAME  = "name";
  public static final String F_TYPE  = "type";
  public static final String F_SCORE = "score";
  public static final String F_VNR   = "vnr";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Naam",
      required = true,
      width = "650px")
  @TextField(maxLength = 255, nullRepresentation = "")
  private String name = "";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Type",
      required = true,
      width = "650px")
  @Select(containerDataSource = RiskProfileRuleTypeContainer.class,
      itemCaptionPropertyId = OMSCHRIJVING)
  private RiskProfileRuleType type = null;

  @Field(customTypeClass = PosNumberField.class,
      caption = "Score",
      required = true,
      width = "50px")
  @TextField(nullRepresentation = "")
  private BigDecimal score = null;

  @Field(customTypeClass = NumberField.class,
      caption = "Volgorde",
      required = true,
      width = "50px")
  @TextField(nullRepresentation = "")
  private BigDecimal vnr = null;
}
