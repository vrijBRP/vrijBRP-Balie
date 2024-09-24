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

package nl.procura.gba.web.modules.hoofdmenu.requestinbox.page1;

import java.util.List;
import java.util.function.Consumer;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxItem;
import nl.procura.standard.ProcuraDate;

public class Page1RequestInboxTable extends GbaTable {

  private final Consumer<RequestInboxItem> consumer;
  private int                              page;
  private int                              pageSize;
  private List<RequestInboxItem>           items;

  public Page1RequestInboxTable(Consumer<RequestInboxItem> consumer) {
    this.consumer = consumer;
  }

  @Override
  public void setColumns() {
    setSelectable(true);
    setMultiSelect(true);

    addColumn("Nr", 30).setUseHTML(true);
    addColumn("Omschrijving").setUseHTML(true);
    addColumn("Behandelaar", 140).setUseHTML(true);
    addColumn("Status", 100).setUseHTML(true);
    addColumn("Ingevoerd op", 130).setUseHTML(true);
  }

  @Override
  public void setRecords() {
    int nr = ((page - 1) * pageSize) + 1;
    if (items != null) {
      for (RequestInboxItem item : items) {
        Record record = addRecord(item);
        record.addValue(nr++);
        record.addValue(item.getDescription());
        record.addValue(item.getHandlerDescription());
        record.addValue(item.getStatus());
        ProcuraDate registeredOn = item.getRegisteredOn();
        record.addValue(registeredOn.getFormatDate() + " " + registeredOn.getFormatTime());
      }
    }
    super.setRecords();
  }

  public void setItems(int page, int pageSize, List<RequestInboxItem> items) {
    this.page = page;
    this.pageSize = pageSize;
    this.items = items;
    init();
  }

  @Override
  public void onDoubleClick(Record record) {
    RequestInboxItem inboxItem = record.getObject(RequestInboxItem.class);
    consumer.accept(inboxItem);
  }
}
