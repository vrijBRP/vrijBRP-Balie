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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page5.tab2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page5.RijbewijsStatusTypeContainer2;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsStatusType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.field.ProNativeSelect;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page5ZakenTab2Bean1 implements Serializable {

  public static final String STATUS   = "status";
  public static final String MAX      = "max";
  public static final String AANGEVER = "aangever";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Status",
      required = true)
  @Select(containerDataSource = RijbewijsStatusTypeContainer2.class)
  private RijbewijsStatusType status = RijbewijsStatusType.RIJBEWIJS_ONTVANGEN_DOOR_GEMEENTE_OK;

  @Field(customTypeClass = NumberField.class,
      caption = "Max. aantal",
      required = true,
      width = "60px")
  @TextField(maxLength = 4)
  private String max = "500";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Zoek de aangever erbij",
      required = true,
      width = "60px")
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  private Boolean aangever = false;

  public RijbewijsStatusType getStatus() {
    return status;
  }

  public void setStatus(RijbewijsStatusType status) {
    this.status = status;
  }

  public String getMax() {
    return max;
  }

  public void setMax(String max) {
    this.max = max;
  }

  public Boolean getAangever() {
    return aangever;
  }

  public void setAangever(Boolean aangever) {
    this.aangever = aangever;
  }
}
