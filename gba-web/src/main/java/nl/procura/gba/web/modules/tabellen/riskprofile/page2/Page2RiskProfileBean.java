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

package nl.procura.gba.web.modules.tabellen.riskprofile.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.math.BigDecimal;
import java.util.Set;

import nl.procura.gba.jpa.personen.types.RiskProfileRelatedCaseType;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.PosNumberField;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2RiskProfileBean implements Serializable {

  public static final String F_NAME            = "name";
  public static final String F_RELATEDCASETYPE = "relatedCaseTypes";
  public static final String F_THRESHOLD       = "threshold";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Naam",
      required = true,
      width = "400px")
  @TextField(maxLength = 255, nullRepresentation = "")
  private String name = "";

  @Field(type = Field.FieldType.OPTION_GROUP,
      caption = "Toepassen op",
      required = true,
      width = "400px")
  @Select(containerDataSource = RiskProfileRelatedCaseContainer.class, multiSelect = true)
  private Set<RiskProfileRelatedCaseType> relatedCaseTypes;

  @Field(customTypeClass = PosNumberField.class,
      caption = "Drempel",
      required = true,
      width = "100px")
  @TextField(nullRepresentation = "")
  private BigDecimal threshold = null;
}
