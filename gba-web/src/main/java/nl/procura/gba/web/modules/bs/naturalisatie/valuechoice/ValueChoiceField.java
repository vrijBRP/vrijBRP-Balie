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

package nl.procura.gba.web.modules.bs.naturalisatie.valuechoice;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.ONBEKEND;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.bs.naturalisatie.valuechoice.ValueChoiceConfig.Setter;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatie;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatieVerzoeker;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.table.TableLayout;

import lombok.AllArgsConstructor;

public class ValueChoiceField extends HLayout {

  private final Label                label = new Label("", Label.CONTENT_XHTML);
  private final DossierNaturalisatie dossier;
  private final ValueChoiceConfig<?> config;
  private final Button               changeButton;

  public ValueChoiceField(DossierNaturalisatie dossier, ValueChoiceConfig<?> config) {
    this.dossier = dossier;
    this.config = config;
    changeButton = new Button("Wijzig");
    changeButton.addListener(onClick(this::openWindow));
    addComponent(changeButton);
    setLabel();
    add(label);
  }

  private ClickListener onClick(Runnable runnable) {
    return clickEvent -> runnable.run();
  }

  private void openWindow() {
    ((GbaApplication) getApplication()).getParentWindow().addWindow(new ValueChoiceWindow());
  }

  private void setLabel() {
    long filled = getFilled();
    if (filled == 0) {
      label.setValue("Niet gevuld");
    } else {
      label.setValue(String.format("Gevuld: %d / %d ", filled, dossier.getVerzoekerGegevens().size()));
    }
  }

  private long getFilled() {
    return dossier.getVerzoekerGegevens().stream()
        .map(gegevens -> config.getGetter().apply(gegevens))
        .filter(value -> value != null && isNotBlank(value.toString()))
        .count();
  }

  public boolean validate() {
    if (isIncomplete()) {
      setLabel();
      label.setValue(label.getValue() + MiscUtils.setClass(false, " (verplicht)"));
      throw new ProException(WARNING, "'" + config.getTitle() + "' niet (volledig) gevuld");
    }
    return true;
  }

  private boolean isIncomplete() {
    return config.isRequired() && getFilled() < dossier.getVerzoekerGegevens().size();
  }

  @SuppressWarnings(value = "unchecked")
  public class ValueChoiceWindow extends GbaModalWindow {

    private final List<Data> dataList = new ArrayList<>();

    public ValueChoiceWindow() {
      setStyleName("valuechoicewindow");
      setSizeUndefined();
      setCaption("Veld: " + config.getTitle());

      TableLayout layout = new TableLayout();
      layout.addStyleName("v-form-error");
      layout.setSizeFull();
      layout.setColumnWidths("", "", "");
      layout.addLabel("<b>Type persoon</b>");
      layout.addLabel("<b>Naam</b>");

      for (DossierNaturalisatieVerzoeker gegevens : dossier.getVerzoekerGegevens()) {
        Object component = config.getComponent().apply(gegevens);
        if (component instanceof ValueChoiceComponent) {
          ValueChoiceComponent<Object> componentValue = (ValueChoiceComponent<Object>) component;
          // Set number of columns
          layout.setColumnWidths(Arrays.stream(new String[2 + componentValue.getFields().size()])
              .map(k -> "").toArray(String[]::new));
          // Add extra columns
          for (String entry : componentValue.getFields().keySet()) {
            layout.addLabel("<b>" + entry + "</b>");
          }
        } else {
          layout.addLabel("<b>" + config.getTitle() + "</b>");
        }
        break;
      }

      boolean focused = false;
      for (DossierPersoon persoon : dossier.getAlleVerzoekers()) {
        BsnFieldValue bsn = persoon.getBurgerServiceNummer();
        DossierNaturalisatieVerzoeker gegevens = dossier.getVerzoekerGegevens(bsn);
        Object component = config.getComponent().apply(gegevens);
        if (gegevens != null) {
          layout.addLabel(getVerzoekerType(gegevens).toString());
          layout.addLabel(getVerzoekerNaam(gegevens));
          Object value = config.getGetter().apply(gegevens);

          if (component instanceof AbstractField) {
            AbstractField field = (AbstractField) component;
            field.setValue(value);
            if (!focused) {
              field.focus();
              focused = true;
            }

            dataList.add(new Data(gegevens, field));
            layout.addData(field);

          } else {
            ValueChoiceComponent<Object> componentValue = (ValueChoiceComponent<Object>) component;
            componentValue.setValue(value);
            componentValue.getFields().values().forEach(layout::addData);
            dataList.add(new Data(gegevens, component));
          }
        }
      }

      Button saveButton = new Button("Opslaan", onClick(this::save));
      Button cancelButton = new Button("Annuleren (Esc)", onClick(this::close));
      saveButton.setWidth("130px");
      cancelButton.setWidth("130px");
      HLayout buttonBar = new HLayout(cancelButton, saveButton)
          .align(saveButton, Alignment.MIDDLE_RIGHT)
          .spacing(true)
          .widthFull();

      addComponent(new VLayout(buttonBar, layout)
          .spacing(true)
          .height(null)
          .margin(true));
    }

    private String getVerzoekerNaam(DossierNaturalisatieVerzoeker verzoeker) {
      return getVerzoeker(verzoeker).map(v -> v.getNaam().getNaam_naamgebruik_eerste_voornaam()).orElse("");
    }

    private DossierPersoonType getVerzoekerType(DossierNaturalisatieVerzoeker verzoeker) {
      return getVerzoeker(verzoeker).map(DossierPersoon::getDossierPersoonType).orElse(ONBEKEND);
    }

    private Optional<DossierPersoon> getVerzoeker(DossierNaturalisatieVerzoeker verzoeker) {
      return dossier.getAlleVerzoekers().stream()
          .filter(gegevens -> gegevens.getBurgerServiceNummer().equals(verzoeker.getBurgerServiceNummer()))
          .findFirst();
    }

    private void save() {
      dataList.forEach(this::saveToData);
      setLabel();
      close();
    }

    private void saveToData(Data data) {
      Object component = data.getComponent();
      Object value;
      if (component instanceof AbstractField) {
        AbstractField field = (AbstractField) component;
        validate(field);
        value = field.getValue();
      } else {
        ValueChoiceComponent<?> componentValue = ((ValueChoiceComponent<?>) component);
        value = componentValue.getValue();
        componentValue.getFields().values().forEach(this::validate);
      }
      Setter<Object> setter = (Setter<Object>) config.getSetter();
      setter.accept(data.getGegevens(), value);
    }

    private void validate(Component component) {
      if (component instanceof AbstractField) {
        AbstractField field = (AbstractField) component;
        field.setValidationVisible(true);
        field.validate();
      }
    }

    @lombok.Data
    @AllArgsConstructor
    private class Data {

      private DossierNaturalisatieVerzoeker gegevens;
      private Object                        component;
    }
  }
}
