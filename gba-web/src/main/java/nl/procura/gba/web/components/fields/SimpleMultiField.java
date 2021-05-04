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

package nl.procura.gba.web.components.fields;

import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Container;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.ui.TextField;

import nl.procura.gba.web.components.fields.values.MultiFieldValue;

public class SimpleMultiField extends TextField implements FocusListener {

  private String    newCaption = "";
  private Container container;
  private List<?>   values     = new ArrayList<>();

  public SimpleMultiField() {
    addStyleName("multifield");
    addListener((FocusListener) this);
  }

  @Override
  public void focus(FocusEvent event) {
    getWindow().addWindow(new SimpleMultiFieldWindow(newCaption, getContainer(), this));
  }

  public Container getContainer() {
    return container;
  }

  public void setContainer(Container container) {
    this.container = container;
  }

  @Override
  public Class<?> getType() {
    return SimpleMultiField.class;
  }

  public List<?> getValues() {
    return values;
  }

  public void setValues(List<?> values) {
    setValue(new MultiFieldValue<>(values));
    this.values = values;
  }

  @Override
  public void setCaption(String caption) {

    if (fil(caption)) {
      newCaption = caption;
    }

    super.setCaption(caption);
  }

}
