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

package nl.procura.gba.web.modules.beheer.email.page2;

import static nl.procura.gba.web.modules.beheer.email.page2.Page2EmailBean.*;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.beheer.email.*;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page2EmailForm extends GbaForm<Page2EmailBean> {

  public Page2EmailForm(EmailTemplate emailTemplate) {

    setOrder(TYPE, ACTIVEREN, ONDERWERP, VAN, ANTWOORD_NAAR, BCC, VARIABELEN, GELDIG, TYPE_CONTENT);
    setColumnWidths(WIDTH_130, "");

    Page2EmailBean bean = new Page2EmailBean();

    bean.setType(emailTemplate.getType());
    bean.setOnderwerp(emailTemplate.getOnderwerp());
    bean.setVan(emailTemplate.getVan());
    bean.setBcc(emailTemplate.getBcc());
    bean.setAntwoordNaar(emailTemplate.getAntwoordNaar());
    bean.setTypeContent(emailTemplate.getTypeContent());
    bean.setActiveren(emailTemplate.isGeactiveerd());
    bean.setGeldig(astr(emailTemplate.getGeldigheid()));

    setBean(bean);

    getField(TYPE, ProNativeSelect.class).setValue(emailTemplate.getType());
  }

  @Override
  public void afterSetColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (property.is(GELDIG)) {
      Label label = new Label("(in dagen)");
      label.setWidth("150px");
      column.addComponent(label);
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public void setBean(Object bean) {

    super.setBean(bean);

    getField(TYPE, ProNativeSelect.class).setContainerDataSource(new EmailTypeContainer(getEmailTypes()));

    getField(TYPE).addListener((ValueChangeListener) event -> onChangeType((EmailType) event.getProperty().getValue()));

    getField(TYPE_CONTENT).addListener(
        (ValueChangeListener) event -> onChangeTypeContent((EmailTypeContent) event.getProperty().getValue()));
  }

  protected List<EmailType> getEmailTypes() {
    return new ArrayList<>();
  }

  protected void onChangeType(EmailType type) {
    Field field = getField(VARIABELEN);
    field.setReadOnly(false);
    String variabeles = getVariableMessage(EmailVariables.getVariables(type));
    field.setValue(fil(variabeles) ? variabeles : "Niet van toepassing");
    field.setReadOnly(true);
    repaint();
  } // Override

  @SuppressWarnings("unused")
  protected void onChangeTypeContent(EmailTypeContent type) {
  }

  private String getVariableMessage(List<EmailVariable> variables) {

    StringBuilder msg = new StringBuilder();

    for (EmailVariable variable : variables) {

      msg.append("<b>" + variable.getCode() + "</b> (" + variable.getOms() + "), ");
    }

    return trim(msg.toString());
  }
}
