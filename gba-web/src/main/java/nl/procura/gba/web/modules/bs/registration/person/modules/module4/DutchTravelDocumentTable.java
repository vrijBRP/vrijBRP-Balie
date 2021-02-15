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

package nl.procura.gba.web.modules.bs.registration.person.modules.module4;

import java.util.function.Consumer;

import nl.procura.gba.jpa.personen.db.DossTravelDoc;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;

class DutchTravelDocumentTable extends GbaTable {

  private final DossierPersoon          person;
  private final Consumer<DossTravelDoc> onDoubleClickListener;

  DutchTravelDocumentTable(DossierPersoon person, Consumer<DossTravelDoc> onDoubleClickListener) {
    this.person = person;
    this.onDoubleClickListener = onDoubleClickListener;
  }

  @Override
  public void setColumns() {
    setSelectable(true);
    setMultiSelect(true);

    addColumn("Nr.", 50);
    addColumn("Soort");
    addColumn("Nummer", 100);

    super.setColumns();
  }

  @Override
  public void setRecords() {
    int nr = 1;
    for (DossTravelDoc travelDoc : person.getDossTravelDocs()) {
      final Record record = addRecord(travelDoc);
      record.addValue(nr);
      record.addValue(GbaTables.NED_REISDOC.get(travelDoc.getNedReisdoc()));
      record.addValue(travelDoc.getDocNr());
      nr++;

      super.setRecords();
    }
  }

  @Override
  public void onDoubleClick(Record record) {
    super.onDoubleClick(record);
    onDoubleClickListener.accept(record.getObject(DossTravelDoc.class));
  }

}
