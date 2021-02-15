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

package nl.procura.gba.web.modules.tabellen.kennisbank.page1;

import static nl.procura.gba.web.modules.tabellen.kennisbank.page1.Page1KennisbankBean.BESTAND;
import static nl.procura.gba.web.modules.tabellen.kennisbank.page1.Page1KennisbankBean.UITGAVE;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public abstract class Page1KennisbankForm extends ReadOnlyForm {

  private boolean isGeupload = false;

  public Page1KennisbankForm() {
    setCaption("Kennisbank CSV bestand");
    setOrder(BESTAND, UITGAVE);
    setReadThrough(true);
    setReadonlyAsText(false);
    setColumnWidths(WIDTH_130, "");
  }

  public Page1KennisbankForm set(String bestand, String uitgave, boolean isGeupload) {

    this.isGeupload = isGeupload;
    Page1KennisbankBean bean = new Page1KennisbankBean();
    bean.setBestand(bestand);
    bean.setUitgave(uitgave);
    setBean(bean);

    return this;
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {

    if (isGeupload && property.is(BESTAND)) {
      ClickListener listener = (ClickListener) event -> onDelete();

      column.addComponent(new Button("Verwijderen", listener));
    }

    super.afterSetColumn(column, field, property);
  }

  public abstract void onDelete();

  @Override
  public Page1KennisbankBean getBean() {
    return (Page1KennisbankBean) super.getBean();
  }
}
