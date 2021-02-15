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

package nl.procura.gba.web.modules.bs.omzetting.page50;

import static nl.procura.gba.web.modules.bs.omzetting.page50.Page50OmzettingBean3.*;

import com.vaadin.ui.Label;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page50OmzettingForm3 extends GbaForm<Page50OmzettingBean3> {

  public Page50OmzettingForm3() {

    setCaption("GPS aktegegevens");
    setColumnWidths(WIDTH_130, "");
    setOrder(BS_AKTE_NUMMER, BRP_AKTE_NUMMER, PLAATS, JAAR);
  }

  @Override
  public void afterSetColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (property.is(BS_AKTE_NUMMER)) {
      column.addComponent(new Label("(max. 7 tekens lang)"));
    }

    if (property.is(BRP_AKTE_NUMMER)) {
      column.addComponent(new Label("(7 posities, 3e teken is een letter)"));
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public Page50OmzettingBean3 getNewBean() {
    return new Page50OmzettingBean3();
  }
}
