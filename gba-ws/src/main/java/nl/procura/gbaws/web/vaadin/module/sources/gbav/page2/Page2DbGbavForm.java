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

package nl.procura.gbaws.web.vaadin.module.sources.gbav.page2;

import static java.text.MessageFormat.format;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

import nl.procura.gbaws.web.vaadin.layouts.forms.DefaultForm;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public abstract class Page2DbGbavForm extends DefaultForm {

  private final Label dateChangedLabel = new Label("", Label.CONTENT_XHTML);

  public Page2DbGbavForm() {

    setColumnWidths("150px", "");
    init();
  }

  @Override
  public Object getNewBean() {
    return new Page2DbGbavBean();
  }

  @Override
  public Page2DbGbavBean getBean() {
    return (Page2DbGbavBean) super.getBean();
  }

  @Override
  public void afterSetBean() {

    Field dc = getField(Page2DbGbavBean.DATE_CHANGED);

    if (dc != null) {
      dc.addListener((ValueChangeListener) event -> updateChangeDateLabel(astr(event.getProperty().getValue())));

      updateChangeDateLabel(astr(dc.getValue()));
    }

    super.afterSetBean();
  }

  public void updateChangeDateLabel(String date) {

    String message = "";

    if (fil(date)) {

      ProcuraDate expirationDate = new ProcuraDate(date).addDays(90);
      String expirationDateString = expirationDate.getFormatDate();
      final int days = new ProcuraDate().diffInDays(expirationDate);

      if (days > 0) {
        if (days == 1) {
          message = setClass(true, "(Het wachtwoord verloopt morgen)");
        } else {
          message = setClass(true, format("(Het wachtwoord verloopt over {0} dagen op {1})", days,
              expirationDateString));
        }
      } else if (days == -1) {
        message = setClass(false, "(Het wachtwoord verliep gisteren)");
      } else if (days < -1) {
        message = setClass(false, format("(Het wachtwoord verliep {0} dagen geleden op {1})", days,
            expirationDateString));
      } else if (days == 0) {
        message = setClass(false, "(Het wachtwoord verloopt vandaag)");
      }
    }

    dateChangedLabel.setValue(message);
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {

    if (property.is(Page2DbGbavBean.DATE_CHANGED)) {
      column.addComponent(dateChangedLabel);
    }

    super.afterSetColumn(column, field, property);
  }

  protected abstract void init();
}
