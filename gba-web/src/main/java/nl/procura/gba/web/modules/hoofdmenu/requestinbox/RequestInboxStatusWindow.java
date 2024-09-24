/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.hoofdmenu.requestinbox;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.function.Consumer;

import com.vaadin.data.Validator.EmptyValueException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;

import nl.procura.burgerzaken.requestinbox.api.model.InboxItemStatus;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.functies.VaadinUtils;

public class RequestInboxStatusWindow extends GbaModalWindow {

  private final GbaNativeSelect select     = new GbaNativeSelect();
  private final Label           errorLabel = new Label("", Label.CONTENT_XHTML);

  public RequestInboxStatusWindow(Consumer<InboxItemStatus> statusConsumer) {
    super("Nieuwe status", "340px");
    HLayout hLayout = new HLayout().spacing(true).widthFull().height(null).margin(false);
    hLayout.addStyleName("hide-required-field-indicator");
    select.setContainerDataSource(new ArrayListContainer(Arrays.stream(InboxItemStatus.values())
        .filter(status -> !status.isUnknown())
        .collect(toList())));
    select.setImmediate(true);
    select.setRequired(true);
    select.setWidth("100%");
    select.setRequired(true);
    select.setRequiredError("Selecteer een status");
    select.setNullSelectionAllowed(true);
    errorLabel.setCaption(null);
    errorLabel.setVisible(false);

    VLayout vLayout = new VLayout().spacing(true).margin(true);
    hLayout.addComponent(select);
    hLayout.setExpandRatio(select, 1F);
    hLayout.addComponent(new Button("Opslaan", event -> {
      try {
        select.validate();
        statusConsumer.accept((InboxItemStatus) select.getValue());
        closeWindow();
      } catch (EmptyValueException e) {
        VaadinUtils.resetHeight(this);
        vLayout.addStyleName("v-form v-form-error");
        select.setStyleName("v-textfield-error v-select-error v-filterselect-error");
        errorLabel.setValue(MiscUtils.setClass(false, e.getMessage()));
        errorLabel.setVisible(true);
      }
    }));

    Fieldset fieldset = new Fieldset("Selecteer de nieuwe status");
    fieldset.setWidth("100%");
    vLayout.addComponent(fieldset);
    vLayout.addComponent(hLayout);
    vLayout.addComponent(errorLabel);
    addComponent(vLayout);
  }
}
