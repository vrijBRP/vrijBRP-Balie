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

package nl.procura.gba.web.modules.bs.registration.person.modules.module3;

import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.DeclarationTypeContainer;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.bs.registration.DeclarationType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class DeclarationBean {

  public static final String F_DECLARATION_DONE_BY = "declarationDoneBy";
  public static final String F_EXPLANATION         = "explanation";

  @Field(customTypeClass = GbaNativeSelect.class, caption = "Aangifte door", width = "260px", required = true)
  @Select(containerDataSource = DeclarationTypeContainer.class)
  @Immediate
  private DeclarationType declarationDoneBy;

  @Field(type = FieldType.TEXT_AREA,
      caption = "Toelichting")
  @TextArea(rows = 5,
      columns = 50,
      nullRepresentation = "",
      maxLength = 200)
  private String explanation = "";
}
