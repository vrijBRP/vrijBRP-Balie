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

package nl.procura.gba.web.modules.zaken.inbox.overzicht;

import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.gemeenteinbox.GemeenteInboxRecord;
import nl.procura.vaadin.theme.twee.ProcuraTweeTheme;

public class InboxOverzichtLayoutTable extends GbaTable {

  private final GemeenteInboxRecord inboxRecord;

  public InboxOverzichtLayoutTable(GemeenteInboxRecord inboxRecord) {
    this.inboxRecord = inboxRecord;
    setSelectable(true);
  }

  @Override
  public void setColumns() {
    addColumn("&nbsp;", 20).setClassType(TableImage.class);
    addColumn("Datum / tijd invoer", 150);
    addColumn("Omschrijving");
    addColumn("Bestand");

    super.setColumns();
  }

  @Override
  public void setRecords() {
    try {
      for (GemeenteInboxRecord inboxRecord : inboxRecord.getRelatedRecords()) {
        Record record = addRecord(inboxRecord);
        if (inboxRecord.equals(this.inboxRecord)) {
          record.addValue(new TableImage(ProcuraTweeTheme.ICOON_16.ARROW_RIGHT));
        } else {
          record.addValue(null);
        }
        record.addValue(inboxRecord.getDatumTijdInvoer());
        record.addValue(inboxRecord.getVerwerkingsTypeOmschrijving());
        record.addValue(inboxRecord.getBestandsnaam());
      }
    } catch (Exception e) {
      getApplication().handleException(e);
    }

    super.setRecords();
  }
}
