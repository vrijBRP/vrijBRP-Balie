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

package nl.procura.gba.web.modules.bs.ontbinding.page30;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.bs.ontbinding.WijzeBeeindigingVerbintenis;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page30OntbindingBean2 implements Serializable {

  public static final String DOOR = "door";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Door",
      required = true)
  @Select(containerDataSource = WijzeBeeindigingContainer.class)
  private WijzeBeeindigingVerbintenis door = null;

  public WijzeBeeindigingVerbintenis getDoor() {
    return door;
  }

  public void setDoor(WijzeBeeindigingVerbintenis door) {
    this.door = door;
  }
}
