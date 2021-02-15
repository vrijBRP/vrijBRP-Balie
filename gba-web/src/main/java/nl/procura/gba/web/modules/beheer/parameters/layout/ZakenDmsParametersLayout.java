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

package nl.procura.gba.web.modules.beheer.parameters.layout;

import com.vaadin.ui.Field;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.beheer.parameters.container.ParmBooleanContainer;
import nl.procura.gba.web.modules.beheer.parameters.form.DatabaseParameterForm;

public class ZakenDmsParametersLayout extends DatabaseParameterLayout {

  public ZakenDmsParametersLayout(GbaApplication application, String naam, String category) {
    super(application, naam, category);
  }

  @Override
  public void setForm(String category) {

    DatabaseParameterForm form = new DatabaseParameterForm(category) {

      @Override
      public Field newField(Field field, Property property) {

        Object value = field.getValue();
        GbaNativeSelect select = (GbaNativeSelect) field;
        select.setContainerDataSource(new ParmBooleanContainer());
        select.setItemCaptionPropertyId(ParmBooleanContainer.OMSCHRIJVING);
        field.setValue(value);

        return super.newField(field, property);
      }
    };

    super.setForm(form);
  }
}
