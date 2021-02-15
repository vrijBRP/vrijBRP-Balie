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

package nl.procura.gba.web.modules.zaken.reisdocument.page24;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.zaken.reisdocumenten.LeveringType;
import nl.procura.gba.web.services.zaken.reisdocumenten.SluitingType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page24ReisdocumentBean1 implements Serializable {

  static final String AFLEVERING = "aflevering";
  static final String AFSLUITING = "afsluiting";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Aflevering",
      required = true)
  @Select(containerDataSource = LeveringContainer.class)
  @Immediate
  private LeveringType aflevering = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Afsluiting",
      required = true)
  private SluitingType afsluiting = SluitingType.DOCUMENT_UITGEREIKT;

  public LeveringType getAflevering() {
    return aflevering;
  }

  public void setAflevering(LeveringType aflevering) {
    this.aflevering = aflevering;
  }

  public SluitingType getAfsluiting() {
    return afsluiting;
  }

  public void setAfsluiting(SluitingType afsluiting) {
    this.afsluiting = afsluiting;
  }
}
