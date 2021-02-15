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

package nl.procura.gba.web.modules.zaken.tmv.page9;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.zaken.tmv.page9.componenten.CategorieContainer;
import nl.procura.gba.web.modules.zaken.tmv.page9.componenten.SetContainer;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page9TmvBean implements Serializable {

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Categorie")
  @Select(itemCaptionPropertyId = CategorieContainer.OMSCHRIJVING,
      nullSelectionAllowed = false)
  @Immediate()
  private String categorie = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Set")
  @Immediate()
  @Select(itemCaptionPropertyId = SetContainer.OMSCHRIJVING,
      nullSelectionAllowed = false)
  private String set = "";
}
