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

package nl.procura.gba.web.components.containers;

import static nl.procura.standard.Globalfunctions.along;

import java.util.List;
import java.util.Objects;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;

import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.gba.web.common.tables.GBATableList;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.field.fieldvalues.TabelFieldValue;

public class TabelContainer extends IndexedContainer {

  public static final String OMSCHRIJVING = "Omschrijving";

  private GBATableList valueContainer;

  public TabelContainer(GBATableList valueContainer, boolean isCurrent, boolean showValue) {
    setValueContainer(valueContainer, isCurrent, showValue);
  }

  public TabelContainer(GBATable table, boolean isCurrent, boolean showValue) {
    this(GbaTables.get(table), isCurrent, showValue);
  }

  public TabelContainer(GBATable table, boolean isCurrent) {
    this(GbaTables.get(table), isCurrent, false);
  }

  public TabelContainer(GBATable table) {
    this(table, false, false);
  }

  public List<TabelFieldValue> get() {
    return valueContainer.get();
  }

  public TabelFieldValue get(Number waarde) {
    return valueContainer.get(waarde);
  }

  public FieldValue get(Number waarde, String omschrijving) {
    return valueContainer.get(waarde, omschrijving);
  }

  public TabelFieldValue get(String waarde) {
    return valueContainer.get(waarde);
  }

  public TabelFieldValue getByKey(Number waarde) {
    return valueContainer.getByKey(waarde);
  }

  public GBATable getTabel() {
    return valueContainer.getTabel();
  }

  public void setValueContainer(GBATableList valueContainer, boolean isCurrent, boolean showValue) {
    addContainerProperty(OMSCHRIJVING, String.class, "");
    removeAllItems();
    this.valueContainer = valueContainer;
    this.valueContainer.get().forEach(v -> add(v, isCurrent, showValue));
  }

  public void add(TabelFieldValue record, boolean isCurrent, boolean showValue) {
    if (record != null && record.getValue() != null) {
      if (!isCurrent || isCurrent(record)) {
        Item item = super.addItem(record);
        if (item != null) {
          if (record.getDescription() != null) {
            Property prop = item.getItemProperty(OMSCHRIJVING);
            String descr = record.getExpirationDescription();
            if (showValue && !Objects.equals(descr, record.getValue())) {
              prop.setValue(descr + " (" + record.getValue() + ")");
            } else {
              prop.setValue(descr);
            }
          }
        }
      }
    }
  }

  private boolean isCurrent(TabelFieldValue record) {
    long current = along(new ProcuraDate().getSystemDate());
    return (record.getDateEnd() <= 0) || ((record.getDateEnd() > 0) && (record.getDateEnd() > current));
  }
}
