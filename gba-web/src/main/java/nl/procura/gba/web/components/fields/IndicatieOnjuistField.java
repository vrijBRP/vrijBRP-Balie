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

  public IndicatieOnjuistField(GBACat gbaCat, boolean hist) {
    this.setValidationVisible(true);
    setContainerDataSource(new IndexedContainer(new Values(gbaCat, hist)));
    setFilteringMode(AbstractSelect.Filtering.FILTERINGMODE_CONTAINS);
  }

  @Override
  public Object getValue() {
    return super.getValue() != null ? new Val(super.getValue(), "") : null;
  }

  class Values extends ArrayList<Val> {

    public Values(GBACat gbaCat, boolean hist) {
      addElements(gbaCat, hist);
    }

    private void addElements(GBACat gbaCat, boolean hist) {
      //Add category
      add(gbaCat, hist);

      // Add groups
      getByCat(gbaCat.getCode()).stream()
          .collect(groupingBy(GBAGroupElem::getGroup))
          .forEach((group, value) -> add(gbaCat, group, hist));

      // Add elements
      getByCat(gbaCat.getCode())
          .forEach(elemGroup -> add(gbaCat, elemGroup.getElem(), hist));

      // Handmatig toegevoegd voor cat. 8
      if (gbaCat.is(GBACat.VB)) {
        add(new Val(
            hist ? "589999" : "089999",
            getCatDescr("betrokkene niet meer woonachtig op adres", hist)));
      }
    }

    private void add(GBACat cat, boolean hist) {
      add(new Val(String.format("%02d0000",
          getCatCode(cat, hist)),
          getCatDescr("hele categorie", hist)));
    }

    private void add(GBACat cat, GBAGroup group, boolean hist) {
      add(new Val(String.format("%02d%02d00",
          getCatCode(cat, hist), group.getCode()),
          getCatDescr("groep " + group.getDescr().toLowerCase(), hist)));
    }

    private void add(GBACat cat, GBAElem elem, boolean hist) {
      add(new Val(String.format("%02d%04d",
          getCatCode(cat, hist), elem.getCode()),
          getCatDescr("element " + elem.getDescr().toLowerCase(), hist)));
    }

    private int getCatCode(GBACat cat, boolean hist) {
      return hist ? cat.getCode() + 50 : cat.getCode();
    }

    private String getCatDescr(String descr, boolean hist) {
      return descr + (hist ? " - historie" : "");
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
