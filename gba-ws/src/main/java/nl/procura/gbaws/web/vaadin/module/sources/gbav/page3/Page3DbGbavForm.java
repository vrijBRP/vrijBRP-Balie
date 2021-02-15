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

package nl.procura.gbaws.web.vaadin.module.sources.gbav.page3;

import static nl.procura.gbaws.web.vaadin.module.sources.gbav.page3.Page3DbGbavBean.PW;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;

import nl.procura.gba.common.BrpPasswordGenerator;
import nl.procura.gbaws.web.vaadin.layouts.forms.DefaultForm;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public abstract class Page3DbGbavForm extends DefaultForm {

  public Page3DbGbavForm() {
    setColumnWidths("150px", "");
    init();
  }

  protected abstract void init();

  @Override
  public Object getNewBean() {
    return new Page3DbGbavBean();
  }

  @Override
  public Page3DbGbavBean getBean() {
    return (Page3DbGbavBean) super.getBean();
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {

    if (property.is(PW)) {

      Button genereerButton = new Button("Genereer een wachtwoord");
      genereerButton
          .addListener((ClickListener) event -> getField(PW).setValue(BrpPasswordGenerator.newPassword()));

      column.addComponent(genereerButton);
    }

    super.afterSetColumn(column, field, property);
  }
}
