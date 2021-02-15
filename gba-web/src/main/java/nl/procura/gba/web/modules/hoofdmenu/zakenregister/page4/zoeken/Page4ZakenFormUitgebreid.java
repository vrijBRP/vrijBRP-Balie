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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.zoeken;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.zoeken.Page4ZakenBean.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.ui.Field;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.components.containers.GebruikerContainer;
import nl.procura.gba.web.components.containers.ProfielContainer;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.SimpleMultiField;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.Services;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class Page4ZakenFormUitgebreid extends GbaForm<Page4ZakenBean> {

  private GebruikerContainer gebruikerContainer = null;
  private ProfielContainer   profielContainer   = null;

  public Page4ZakenFormUitgebreid() {

    setCaption("Overig");
    setOrder(ZAAKTYPES, ZAAKSTATUSSEN, GEBRUIKER, PROFIEL);
    setColumnWidths(WIDTH_130, "");
  }

  @Override
  public Page4ZakenBean getNewBean() {
    return new Page4ZakenBean();
  }

  public void onReload() {
    // Overriden please!!!
  }

  @Override
  public void setVisible(boolean visible) {

    if (visible) {
      setBean(getBean());
      updateContainers();
    }

    super.setVisible(visible);
  }

  public void updateContainers() {
    addGebruikerContainer();
    addProfielContainer();
    addZaakTypeContainer();
    addZaakStatusContainer();
    addValueChangeListeners();
  }

  private void addGebruikerContainer() {

    GbaNativeSelect field = getField(GEBRUIKER, GbaNativeSelect.class);

    if (field != null) {
      if (gebruikerContainer == null) {
        gebruikerContainer = new GebruikerContainer(getServices().getGebruikerService().getGebruikers(false));
      }

      field.setContainerDataSource(gebruikerContainer);
    }
  }

  private void addProfielContainer() {

    GbaNativeSelect field = getField(PROFIEL, GbaNativeSelect.class);

    if (field != null) {
      if (profielContainer == null) {
        profielContainer = new ProfielContainer(getServices().getProfielService().getProfielen());
      }

      field.setContainerDataSource(profielContainer);
    }
  }

  private void addValueChangeListeners() {
    for (Field field : getFields(ZAAKTYPES, ZAAKSTATUSSEN, GEBRUIKER, PROFIEL)) {
      if (field != null) {
        field.addListener((ValueChangeListener) event -> onReload());
      }
    }
  }

  private void addZaakTypeContainer() {
    getField(ZAAKTYPES, SimpleMultiField.class).setContainer(new ArrayListContainer(getZaakTypes()));
  }

  private void addZaakStatusContainer() {
    getField(ZAAKSTATUSSEN, SimpleMultiField.class).setContainer(new ArrayListContainer(getZaakStatussen()));
  }

  private Services getServices() {
    return getApplication().getServices();
  }

  private List<FieldValue> getZaakTypes() {
    return Arrays.stream(ZaakType.values())
        .filter(type -> !ZaakType.ONBEKEND.equals(type))
        .map(type -> new FieldValue(type, type.getOms()))
        .collect(Collectors.toList());
  }

  private List<FieldValue> getZaakStatussen() {
    List<ZaakStatusType> types = new ArrayList<>();
    types.add(ZaakStatusType.INCOMPLEET);
    types.add(ZaakStatusType.WACHTKAMER);
    types.add(ZaakStatusType.OPGENOMEN);
    types.add(ZaakStatusType.INBEHANDELING);
    types.add(ZaakStatusType.DOCUMENT_ONTVANGEN);
    types.add(ZaakStatusType.GEWEIGERD);
    types.add(ZaakStatusType.VERWERKT);
    types.add(ZaakStatusType.GEANNULEERD);

    return types.stream()
        .filter(status -> !ZaakStatusType.ONBEKEND.equals(status))
        .map(status -> new FieldValue(status, status.getCode() + ": " + status.getOms()))
        .collect(Collectors.toList());
  }
}
