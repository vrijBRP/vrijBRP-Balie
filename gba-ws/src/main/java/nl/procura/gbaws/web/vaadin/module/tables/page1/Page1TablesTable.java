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

package nl.procura.gbaws.web.vaadin.module.tables.page1;

import nl.procura.gbaws.db.handlers.LandTabDao;
import nl.procura.gbaws.db.handlers.LandTabDao.LandTable;
import nl.procura.gbaws.web.vaadin.layouts.tables.PersonenWsTable;

@SuppressWarnings("serial")
public class Page1TablesTable extends PersonenWsTable {

  public Page1TablesTable() {
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Code", 50);
    addColumn("Omschrijving");
    addColumn("Elementen");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    for (LandTable table : LandTabDao.getTables()) {

      Record r = addRecord(table);

      r.addValue(table.getCode());
      r.addValue(table.getDescription() + " (" + table.getRecordCount() + ")");
      r.addValue(table.getElementsAsString());
    }

    super.setRecords();
  }
}
