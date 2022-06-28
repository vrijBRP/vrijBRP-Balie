/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.beheer.verkiezing.page6;

import static nl.procura.gba.web.modules.beheer.verkiezing.page6.Page6VerkiezingBean.F_INHOUD;
import static nl.procura.gba.web.modules.beheer.verkiezing.page6.Page6VerkiezingBean.F_NAAM;

import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

import nl.procura.gba.jpa.personen.db.KiesrVerkInfo;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class Page6VerkiezingForm extends GbaForm<Page6VerkiezingBean> {

  public Page6VerkiezingForm(KiesrVerkInfo verkInfo) {
    setOrder(F_NAAM, F_INHOUD);
    setColumnWidths(WIDTH_130, "");

    Page6VerkiezingBean bean = new Page6VerkiezingBean();
    bean.setNaam(verkInfo.getNaam());
    bean.setInhoud(verkInfo.getInhoud());
    setBean(bean);
  }

  @Override
  public void afterSetColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(F_INHOUD)) {
      column.addComponent(new Label(" (Gebruik shift + enter om meerdere regels in te voeren.)"));
    }
    super.afterSetColumn(column, field, property);
  }
}
