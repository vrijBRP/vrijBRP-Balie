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

package nl.procura.gba.web.modules.hoofdmenu.inbox.page1;

import java.util.List;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.inbox.InboxRecord;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;

public class Page1InboxTable extends GbaTable {

  public Page1InboxTable() {
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Datum / tijd invoer", 150);
    addColumn("Status", 150);
    addColumn("Zaak-id", 200);
    addColumn("Omschrijving");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    try {
      ZaakArgumenten argumenten = new ZaakArgumenten();
      argumenten.setStatussen(ZaakStatusType.OPGENOMEN);

      List<InboxRecord> zaken = getApplication().getServices().getInboxService().getZaken(argumenten);
      for (InboxRecord inboxRecord : zaken) {
        Record record = addRecord(inboxRecord);
        record.addValue(inboxRecord.getDatumTijdInvoer());
        record.addValue(inboxRecord.getStatus());
        record.addValue(inboxRecord.getZaakId());
        record.addValue(inboxRecord.getOmschrijving());
      }
    } catch (Exception e) {
      getApplication().handleException(e);
    }

    super.setRecords();
  }
}
