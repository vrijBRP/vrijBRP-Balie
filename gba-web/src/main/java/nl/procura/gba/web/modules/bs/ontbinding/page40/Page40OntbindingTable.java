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

package nl.procura.gba.web.modules.bs.ontbinding.page40;

import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Component;

import nl.procura.gba.web.components.containers.NaamgebruikContainer;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.field.fieldvalues.TabelFieldValue;

public class Page40OntbindingTable extends GbaTable {

  private final List<OntbindingNaamgebruik> list = new ArrayList<>();

  @Override
  public void setColumns() {

    addColumn("Naam", 200);
    addColumn("Huidig naamgebruik");
    addColumn("Keuze naamgebruik", 460).setClassType(Component.class);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    if (list.isEmpty()) {
      set();
    }

    for (OntbindingNaamgebruik n : list) {

      Record record = addRecord(n);

      GbaNativeSelect select = new GbaNativeSelect();
      select.setNullSelectionAllowed(false);
      select.setWidth("100%");

      if (n.isNaamgebruikVastgesteld()) {
        select.setContainerDataSource(new CustomNaamgebruikContainer1());
      } else {
        select.setContainerDataSource(new CustomNaamgebruikContainer2());
      }

      String huidigeWaarde = astr(n.getNaamgebruikHuidig().getValue());
      String nieuweWaarde = astr(n.getNaamgebruikNieuw().getValue());

      // Als de huidige waarde gevuld is en de nieuwe waarde leeg
      // dan de huidige waarde overnemen
      if (fil(huidigeWaarde) && emp(nieuweWaarde)) {
        select.setValue(n.getNaamgebruikHuidig());
      } else {
        select.setValue(n.getNaamgebruikNieuw());
      }

      n.setNieuwField(select);

      record.addValue(n.getNaam());
      record.addValue(n.getNaamgebruikHuidigTekst());
      record.addValue(select);
    }

    super.setRecords();
  }

  protected void add(OntbindingNaamgebruik v) {

    list.add(v);
  }

  protected void set() {
  }

  /**
   * Container met de echte codes;
   */
  public class CustomNaamgebruikContainer1 extends NaamgebruikContainer {

    @Override
    public Item addItem(Object itemId) {
      FieldValue fs = (FieldValue) itemId;
      return super.addItem(new TabelFieldValue(fs.getValue(), fs.getValue() + ": " + fs.getDescription()));
    }
  }

  /**
   * Container met alleen waarde 'Niet van toepassing'
   */
  public class CustomNaamgebruikContainer2 extends IndexedContainer {

    public CustomNaamgebruikContainer2() {
      addItem(new FieldValue("", "Niet van toepassing"));
    }
  }
}
