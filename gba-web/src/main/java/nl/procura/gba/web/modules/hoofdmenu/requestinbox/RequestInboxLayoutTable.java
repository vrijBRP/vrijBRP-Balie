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

package nl.procura.gba.web.modules.hoofdmenu.requestinbox;

import java.util.List;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxItem;
import nl.procura.standard.ProcuraDate;

public class RequestInboxLayoutTable extends GbaTable {

  private final RequestInboxItem inboxItem;

  public RequestInboxLayoutTable(RequestInboxItem inboxItem) {
    this.inboxItem = inboxItem;
    setSelectable(true);
  }

  @Override
  public void setColumns() {
    addColumn("Datum / tijd invoer", 140);
    addColumn("Omschrijving");

    super.setColumns();
  }

  @Override
  public void setRecords() {
    try {
      List<RequestInboxItem> relatedItems = getApplication().getServices()
          .getRequestInboxService()
          .getRelatedItems(inboxItem);
      for (RequestInboxItem inboxRecord : relatedItems) {
        Record record = addRecord(inboxRecord);
        ProcuraDate registeredOn = inboxRecord.getRegisteredOn();
        record.addValue(registeredOn.getFormatDate() + " " + registeredOn.getFormatTime());
        record.addValue(inboxRecord.getDescription());
      }
    } catch (Exception e) {
      getApplication().handleException(e);
    }

    super.setRecords();
  }
}
