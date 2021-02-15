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

package nl.procura.gba.web.modules.bs.registration.page10;

import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.InterpreterTypeContainer;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.services.bs.registration.InterpreterType;
import nl.procura.vaadin.annotation.field.*;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class InterpreterBean {

  public static final String F_INTERPRETER = "interpreter";
  public static final String F_NAME        = "name";
  public static final String F_LANGUAGE    = "language";

  @Field(customTypeClass = GbaNativeSelect.class, caption = "Aanwezigheid tolk", width = "100px")
  @Select(containerDataSource = InterpreterTypeContainer.class, nullSelectionAllowed = false)
  @Immediate
  private InterpreterType interpreter = InterpreterType.NONE;

  @Field(customTypeClass = GbaTextField.class, caption = "Naam tolk", width = "350px", required = true)
  @TextField(nullRepresentation = "")
  private String name;

  @Field(customTypeClass = GbaTextField.class, caption = "Taal", width = "200px", required = true)
  @TextField(nullRepresentation = "")
  private String language;
}
