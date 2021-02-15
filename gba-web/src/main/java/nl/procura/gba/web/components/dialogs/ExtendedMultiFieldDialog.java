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

package nl.procura.gba.web.components.dialogs;

import java.util.Set;

import com.vaadin.data.Container;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import nl.procura.gba.web.components.fields.ExtendedMultiField;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;

public class ExtendedMultiFieldDialog extends GbaModalWindow implements ClickListener {

  private final Button       buttonToev = new Button("+");
  private final Button       buttonVerw = new Button("-");
  private final ComboBox     comboBox   = new ComboBox();
  private final ListSelect   listSelect = new ListSelect();
  private ExtendedMultiField multiField = null;

  public ExtendedMultiFieldDialog(String caption, Container container, ExtendedMultiField multiField) {

    super(caption, "300px");

    setMultiField(multiField);

    VerticalLayout v = new VerticalLayout();
    v.setMargin(true);

    comboBox.setContainerDataSource(container);
    comboBox.setWidth("100%");
    comboBox.focus();
    comboBox.setImmediate(true);

    comboBox.addListener((ValueChangeListener) event -> {

      add(event.getProperty().getValue());

      getMultiField().update();
    });

    listSelect.setWidth("100%");
    listSelect.setRows(5);
    listSelect.setNullSelectionAllowed(false);
    listSelect.setMultiSelect(true);

    for (Object item : getMultiField().getItems()) {
      listSelect.addItem(item);
    }

    HorizontalLayout h = new HorizontalLayout();
    h.setWidth("100%");

    buttonToev.setSizeFull();
    buttonToev.addListener(this);

    buttonVerw.setSizeFull();
    buttonVerw.addListener(this);

    h.addComponent(buttonToev);
    h.addComponent(buttonVerw);

    v.addComponent(comboBox);
    v.addComponent(h);
    v.addComponent(listSelect);
    v.setWidth("100%");
    v.setSpacing(true);

    addComponent(v);
  }

  @Override
  public void buttonClick(ClickEvent event) {

    if (event.getButton() == buttonToev) {

      if (comboBox.getValue() != null) {

        add(comboBox.getValue());
      }
    } else if (event.getButton() == buttonVerw) {

      if (listSelect.getValue() != null) {

        if (listSelect.getValue() instanceof Set<?>) {

          for (Object o : (Set<?>) listSelect.getValue()) {

            remove(o);
          }
        } else {

          remove(listSelect.getValue());
        }
      }
    }

    getMultiField().update();
  }

  @Override
  public void close() {
    super.close();
  }

  public ExtendedMultiField getMultiField() {
    return multiField;
  }

  public void setMultiField(ExtendedMultiField multiField) {
    this.multiField = multiField;
  }

  private void add(Object o) {
    if (o != null) {
      getMultiField().addItem(o);
      listSelect.addItem(o);
    }
  }

  private void remove(Object o) {
    getMultiField().remove(o);
    listSelect.removeItem(o);
  }
}
