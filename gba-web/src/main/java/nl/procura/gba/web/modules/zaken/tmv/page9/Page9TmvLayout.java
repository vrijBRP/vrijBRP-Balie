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

package nl.procura.gba.web.modules.zaken.tmv.page9;

import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.modules.persoonslijst.overig.grid.RecordElementCombo;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Page9TmvLayout extends GbaVerticalLayout {

  private Table1 table1 = new Table1();
  private Table2 table2 = new Table2();

  public Page9TmvLayout() {

    setSpacing(true);

    addComponent(new Fieldset("Beschikbare elementen"));
    addExpandComponent(table1, 0.5f);
    addComponent(new Fieldset("Geselecteerde elementen"));
    addExpandComponent(table2, 0.5f);
  }

  public Table1 getTable1() {
    return table1;
  }

  public void setTable1(Table1 table1) {
    this.table1 = table1;
  }

  public Table2 getTable2() {
    return table2;
  }

  public void setTable2(Table2 table2) {
    this.table2 = table2;
  }

  public void selectAll(boolean isOn) {

    Page9TmvTable1 t1 = getTable1();
    Page9TmvTable1 t2 = getTable2();

    if (!isOn) {
      t1 = getTable2();
      t2 = getTable1();
    }

    for (Record r : t1.getRecords()) {

      RecordElementCombo er = (RecordElementCombo) r.getObject();

      boolean isCat = er.getPleElement().getCat().equals(getTable1().getCategorie());
      boolean isSet = er.getGbaSet().getExtIndex() == getTable1().getSet().getExtIndex();

      if (isCat && isSet) {
        t2.addElementRecord((RecordElementCombo) r.getObject());
      }

      t1.removeElementRecord((RecordElementCombo) r.getObject());
    }

    getTable1().init();
    getTable2().init();
  }

  public class Table1 extends Page9TmvTable1 {

    public Table1() {
    }

    @Override
    public void onDoubleClick(Record record) {

      // Toevoegen aan tabel 2 en verwijderen uit tabel 1

      for (Record r : getGroupRecords(record)) {
        getTable2().addElementRecord((RecordElementCombo) r.getObject());
        getTable1().removeElementRecord((RecordElementCombo) r.getObject());
      }

      getTable1().init();
      getTable2().init();

      super.onDoubleClick(record);
    }
  }

  public class Table2 extends Page9TmvTable1 {

    public Table2() {
    }

    @Override
    public void onDoubleClick(Record record) {

      for (Record r : getGroupRecords(record)) {

        RecordElementCombo er = (RecordElementCombo) r.getObject();

        // Als categorie en set overeenkomt dan weer toevoegen

        boolean isCat = er.getPleElement().getCat().equals(getTable1().getCategorie());
        boolean isSet = er.getGbaSet().getExtIndex() == getTable1().getSet().getExtIndex();

        if (isCat && isSet) {
          getTable1().addElementRecord(er);
        }

        getTable2().removeElementRecord((RecordElementCombo) r.getObject());
      }

      getTable1().init();
      getTable2().init();

      super.onDoubleClick(record);
    }
  }
}
