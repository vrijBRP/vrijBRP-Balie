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

package nl.procura.gba.web.modules.zaken.rijbewijs.page4;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.rdw.processen.p0252.f08.TECHVOORZGEG;
import nl.procura.rdw.processen.p0252.f08.UITGRYBGEG;

public class Page4RijbewijsTable2 extends GbaTable {

  private final UITGRYBGEG c;

  public Page4RijbewijsTable2(UITGRYBGEG c) {
    this.c = c;
  }

  @Override
  public void setColumns() {

    setSelectable(false);
    addColumn("Volgnr.", 100);
    addColumn("Technische voorziening");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    for (TECHVOORZGEG c : c.getTechvoorztab().getTechvoorzgeg()) {
      Record r = addRecord(c);
      r.addValue(c.getTechvolgnr().toString());
      r.addValue(c.getTechvoorzvrtg());
    }
  }
}
