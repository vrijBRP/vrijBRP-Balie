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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.address.page2;

import static nl.procura.standard.Globalfunctions.astr;

import java.util.List;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.address.SelectListener;
import nl.procura.gba.web.services.interfaces.address.Address;

public class Page2QuickSearchTable extends GbaTable {

  private final List<Address> addresses;
  private SelectListener      selectListener;

  public Page2QuickSearchTable(List<Address> addresses, SelectListener selectListener) {
    this.addresses = addresses;
    this.selectListener = selectListener;
    setHeight("400px");
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setSelectFirst(true);

    addColumn("Nr", 30);
    addColumn("Adres");

    super.setColumns();
  }

  @Override
  public void onDoubleClick(Record record) {
    selectListener.select((Address) record.getObject());
  }

  @Override
  public void setRecords() {

    int nr = 0;
    if (addresses != null) {
      for (Address address : addresses) {
        nr++;
        Record row = addRecord(address);
        row.addValue(astr(nr));
        row.addValue(address.getLabel());
      }
    }
  }
}
