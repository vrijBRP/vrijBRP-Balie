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

package nl.procura.gba.web.modules.beheer.parameters.layout;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Field;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.beheer.parameters.bean.ZaakStatusParameterContainer;
import nl.procura.gba.web.modules.beheer.parameters.form.DatabaseParameterForm;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.gba.templates.ZoekProfielType;
import nl.procura.vaadin.component.container.ProcuraContainer;

public class ZoekenProfielParametersLayout extends DatabaseParameterLayout {

  public ZoekenProfielParametersLayout(GbaApplication application, String naam, String category) {
    super(application, naam, category);
  }

  @Override
  public void setForm(String category) {

    super.setForm(new DatabaseParameterForm(category) {

      @Override
      public Field newField(Field field, Property property) {
        if (isParameter(property, ParameterConstant.ZOEK_PROFIEL)) {
          Object value = field.getValue();
          GbaNativeSelect select = (GbaNativeSelect) field;
          select.setContainerDataSource(new ZoekProfielContainer());
          select.setItemCaptionPropertyId(ZaakStatusParameterContainer.OMSCHRIJVING);
          field.setValue(value);
        }

        return super.newField(field, property);
      }
    });
  }

  public class ZoekProfielContainer extends IndexedContainer implements ProcuraContainer {

    public static final String OMSCHRIJVING = "Omschrijving";

    public ZoekProfielContainer() {

      addContainerProperty(OMSCHRIJVING, String.class, "");
      removeAllItems();

      addProfiel(ZoekProfielType.PROFIEL_STANDAARD);
      addProfiel(ZoekProfielType.PROFIEL_GBAV_PLUS);
    }

    private void addProfiel(ZoekProfielType type) {
      Item item;
      item = addItem(type.getCode());
      item.getItemProperty(OMSCHRIJVING).setValue(type.getOms());
    }
  }
}
