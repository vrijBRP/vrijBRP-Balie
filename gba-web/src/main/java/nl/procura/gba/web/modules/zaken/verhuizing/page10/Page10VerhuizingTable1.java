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

package nl.procura.gba.web.modules.zaken.verhuizing.page10;

import com.vaadin.ui.Embedded;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.table.GbaTable;

public class Page10VerhuizingTable1 extends GbaTable {

  @Override
  public void refreshRecords() {
    super.refreshRecords();
    if (getContainerDataSource().getItemIds().size() > 0) {
      select(getContainerDataSource().getItemIds().iterator().next());
    }
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("&nbsp;", 20).setClassType(Embedded.class);
    addColumn("Persoon").setUseHTML(true);
    addColumn("Presentievraag", 220).setUseHTML(true);
    addColumn("Relatie", 100);
    addColumn("Aangifte", 150).setClassType(GbaNativeSelect.class);
    addColumn("Geslacht", 80);
    addColumn("Geboren", 100);

    super.setColumns();
  }
}
