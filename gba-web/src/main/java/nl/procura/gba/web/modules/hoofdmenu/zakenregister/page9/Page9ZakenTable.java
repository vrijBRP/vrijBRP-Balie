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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page9;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.theme.GbaWebTheme;

public class Page9ZakenTable extends GbaTable {

  public Page9ZakenTable() {
  }

  @Override
  public void setColumns() {

    addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

    addColumn("Nr", 30);
    addColumn("Gewijzigd", 80).setUseHTML(true);
    addColumn("Id", 260);
    addColumn("Onderwerp", 200);
    addColumn("Omschrijving", 350);
    addColumn("Opmerkingen");

    super.setColumns();
  }
}
