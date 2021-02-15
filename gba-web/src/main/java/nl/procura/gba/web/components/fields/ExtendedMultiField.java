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
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.Container;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.ui.TextField;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.dialogs.ExtendedMultiFieldDialog;
import nl.procura.standard.Globalfunctions;

public class ExtendedMultiField extends TextField implements FocusListener {

  private String       newCaption = "";
  private Container    container;
  private List<Object> items      = new ArrayList<>();

  public ExtendedMultiField() {
    addStyleName("multifield");
    addListener((FocusListener) this);
  }

  public void addItem(Object value) {
    if (!items.contains(value)) {
      items.add(value);
    }
  }

  @Override
  public void focus(FocusEvent event) {
    fillItems();
    getWindow().addWindow(new ExtendedMultiFieldDialog(newCaption, container, this));
  }

  private void fillItems() {
    if (items.isEmpty() && getValue() != null && isNotBlank(getValue().toString())) {
      items.addAll(Arrays.asList(StringUtils.split(getValue().toString(), ",")));
    }
  }

  public void setContainer(Container container) {
    this.container = container;
  }

  public List<Object> getItems() {
    return items;
  }

  @Override
  public Class<?> getType() {
    return ExtendedMultiField.class;
  }

  public void remove(Object value) {
    items.remove(value);
  }

  @Override
  public void setCaption(String caption) {
    if (fil(caption)) {
      newCaption = caption;
    }

    super.setCaption(caption);
  }

  public void update() {
    MiscUtils.sort(items);
    setValue(items.stream()
        .map(Globalfunctions::astr)
        .collect(Collectors.joining(", ")));

  }
}
