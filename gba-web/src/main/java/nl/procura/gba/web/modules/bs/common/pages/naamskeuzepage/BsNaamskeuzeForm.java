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

package nl.procura.gba.web.modules.bs.common.pages.naamskeuzepage;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

import lombok.Data;

public class BsNaamskeuzeForm extends GbaForm<BsNaamskeuzeForm.Bean> {

  private static final String AKTE_VNR   = "akteVolgnr";
  private final Button        buttonZoek = new Button("Zoek (enter)");

  public BsNaamskeuzeForm(Runnable onClickListener) {
    setColumnWidths("120px", "");
    buttonZoek.addListener((ClickListener) event -> onClickListener.run());
    setBean(new Bean());
  }

  @Override
  public void afterSetColumn(Column column, com.vaadin.ui.Field field, Property property) {
    if (property.is(AKTE_VNR)) {
      column.addComponent(buttonZoek);
    }
    super.afterSetColumn(column, field, property);
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class Bean implements Serializable {

    @Field(customTypeClass = NumberField.class,
        caption = "Akte volgnummer",
        width = "200px",
        required = true)
    private String akteVolgnr = "";
  }
}
