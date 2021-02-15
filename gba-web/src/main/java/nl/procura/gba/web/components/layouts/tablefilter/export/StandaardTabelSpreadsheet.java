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

package nl.procura.gba.web.components.layouts.tablefilter.export;

import static nl.procura.standard.Globalfunctions.astr;

import com.vaadin.data.Container;
import com.vaadin.ui.Component;

import nl.procura.gba.web.common.misc.spreadsheets.SpreadsheetTemplate;
import nl.procura.gba.web.services.zaken.documenten.UitvoerformaatType;
import nl.procura.vaadin.component.table.indexed.IndexedTable;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Column;

public class StandaardTabelSpreadsheet extends SpreadsheetTemplate {

  private final IndexedTable table;

  public StandaardTabelSpreadsheet(IndexedTable table, String caption, UitvoerformaatType uitvoerformaat) {

    super(caption, uitvoerformaat);

    this.table = table;
  }

  @Override
  public void compose() {

    Container container = table.getContainerDataSource();

    for (Column column : table.getColumns()) {

      if (column.getClassType() == null) {

        add(column.getCaption());
      }
    }

    store();

    for (Object itemId : container.getItemIds()) {

      for (Column column : table.getColumns()) {

        if (column.getClassType() == null) {

          Object waarde = container.getItem(itemId).getItemProperty(column.getId()).getValue();

          if (waarde instanceof Component) {

            add("");
          } else {

            add(astr(waarde));
          }
        }
      }

      store();
    }
  }
}
