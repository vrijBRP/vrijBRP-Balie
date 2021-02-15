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

package nl.procura.gbaws.web.vaadin.module.ws.page2;

import nl.procura.gbaws.web.vaadin.layouts.tables.PersonenWsTable;
import nl.procura.gbaws.web.vaadin.module.ws.page1.Page1WsTable.Error;
import nl.procura.gbaws.web.vaadin.module.ws.page1.Page1WsTable.Webservice;

@SuppressWarnings("serial")
public class Page2WsTable extends PersonenWsTable {

  private final Webservice ws;

  public Page2WsTable(Webservice ws) {
    this.ws = ws;
  }

  @Override
  public void setColumns() {

    addColumn("Code", 50);
    addColumn("Omschrijving");

    setClickable(true);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    for (Error error : ws.getErrors()) {

      Record record = addRecord(error);
      record.addValue(error.getCode());
      record.addValue(error.getDescription());
    }

    super.setRecords();
  }

}
