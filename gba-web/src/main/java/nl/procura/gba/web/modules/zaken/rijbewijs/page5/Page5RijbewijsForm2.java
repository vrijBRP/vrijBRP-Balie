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

package nl.procura.gba.web.modules.zaken.rijbewijs.page5;

import static nl.procura.gba.web.modules.zaken.rijbewijs.page5.Page5RijbewijsBean2.NATIONALITEITEN;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page5RijbewijsForm2 extends GbaForm<Page5RijbewijsBean2> {

  public Page5RijbewijsForm2(Page5RijbewijsBean2 bean) {

    initForm();
    setReadThrough(true);
    setReadonlyAsText(false);
    setBean(bean);
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(NATIONALITEITEN)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }

  protected void initForm() {
  }
}
