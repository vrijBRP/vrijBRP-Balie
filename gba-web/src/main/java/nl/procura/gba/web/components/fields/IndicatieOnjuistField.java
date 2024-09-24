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

import static java.util.stream.Collectors.groupingBy;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroupElements.getByCat;

import java.util.ArrayList;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.AbstractSelect;

import nl.procura.burgerzaken.gba.StringUtils;
import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBAGroup;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements.GBAGroupElem;
import nl.procura.standard.Globalfunctions;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class IndicatieOnjuistField extends GbaComboBox {

  public IndicatieOnjuistField(GBACat gbaCat) {
    this.setValidationVisible(true);
    setContainerDataSource(new IndexedContainer(new Values(gbaCat)));
    setFilteringMode(AbstractSelect.Filtering.FILTERINGMODE_CONTAINS);
  }

  @Override
  public Object getValue() {
    return super.getValue() != null ? new Val(super.getValue(), "") : null;
  }

  class Values extends ArrayList {

    public Values(GBACat gbaCat) {

      //Add category
      add(gbaCat);

      // Add groups
      getByCat(gbaCat.getCode()).stream()
          .collect(groupingBy(GBAGroupElem::getGroup))
          .forEach((key, value) -> add(gbaCat, key));

      // Add elements
      getByCat(gbaCat.getCode()).forEach(elemGroup -> add(gbaCat, elemGroup.getElem()));
    }

    private void add(GBACat cat) {
      add(new Val(String.format("%02d0000", cat.getCode()),
          "hele categorie"));
    }

    private void add(GBACat cat, GBAGroup group) {
      add(new Val(String.format("%02d%02d00", cat.getCode(), group.getCode()),
          "groep " + group.getDescr().toLowerCase()));
    }

    private void add(GBACat cat, GBAElem elem) {
      add(new Val(String.format("%02d%04d", cat.getCode(), elem.getCode()),
          "element " + elem.getDescr().toLowerCase()));
    }
  }

  class Val extends FieldValue {

    public Val(Object value, String description) {
      String val = Globalfunctions.astr(value).replaceAll("\\D+", "");
      setValue(val);
      if (val.length() == 6) {
        StringBuilder descr = new StringBuilder()
            .append(val, 0, 2)
            .append(".").append(val, 2, 4)
            .append(".").append(val.substring(4));

        if (StringUtils.isNotBlank(description)) {
          descr.append(" (").append(description).append(")");
        }

        setDescription(descr.toString());
      } else {
        setDescription(val);
      }
    }
  }
}
