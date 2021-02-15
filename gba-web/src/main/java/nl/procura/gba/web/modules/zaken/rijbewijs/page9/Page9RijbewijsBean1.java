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

package nl.procura.gba.web.modules.zaken.rijbewijs.page9;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.container.NLBooleanContainer;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page9RijbewijsBean1 implements Serializable {

  public static final String STATUS = "status";

  public static final String SPOED = "spoed";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Spoed")
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE,
      nullSelectionAllowed = false)
  @Immediate()
  private boolean spoed  = false;
  @Field(type = FieldType.LABEL,
      caption = "Status",
      width = "300px")
  private String  status = "";

  public Page9RijbewijsBean1() {
  }

  public Page9RijbewijsBean1(RijbewijsAanvraag aanvraag) {

    setSpoed(aanvraag.isSpoed());
    setStatus(aanvraag.getStatussen().getStatus().getStatus().toString());
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public boolean isSpoed() {
    return spoed;
  }

  public void setSpoed(boolean spoed) {
    this.spoed = spoed;
  }
}
