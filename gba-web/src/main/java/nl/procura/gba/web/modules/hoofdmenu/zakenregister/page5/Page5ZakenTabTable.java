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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page5;

import com.vaadin.ui.Embedded;

import nl.procura.gba.web.components.layouts.table.GbaTable;

public class Page5ZakenTabTable extends GbaTable {

  @Override
  public void setColumns() {
    setSelectable(true);
    setMultiSelect(true);

    addColumn("Nummer", 50);
    addColumn("&nbsp;", 20).setClassType(Embedded.class);
    addColumn("Aanvraagnummer", 120);
    addColumn("Datum / tijd", 150);
    addColumn("Zaak", 150);
    addColumn("Bsn / A-nummer", 100);
    addColumn("Naam aangever");
    addColumn("Status", 120).setUseHTML(true);
    addColumn("Actie");

    super.setColumns();
  }
}
