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

package nl.procura.gba.web.modules.zaken.verhuizing.page20;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.zaken.verhuizing.HervestigingDuur;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page20VerhuizingBean1 implements Serializable {

  public static final String LAND = "land";
  public static final String DUUR = "duur";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Land van herkomst",
      required = true,
      width = "400px")
  @Select(containerDataSource = LandContainer.class)
  private FieldValue       land = null;
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Vermoedelijke verblijfsduur",
      required = true,
      width = "400px")
  @Select(containerDataSource = HervestigingDuurContainer.class,
      nullSelectionAllowed = false)
  @Immediate()
  private HervestigingDuur duur = HervestigingDuur.LANGER;

  public HervestigingDuur getDuur() {
    return duur;
  }

  public void setDuur(HervestigingDuur duur) {
    this.duur = duur;
  }

  public FieldValue getLand() {
    return land;
  }

  public void setLand(FieldValue land) {
    this.land = land;
  }
}
