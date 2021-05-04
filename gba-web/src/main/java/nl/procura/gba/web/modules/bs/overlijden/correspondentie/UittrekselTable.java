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

package nl.procura.gba.web.modules.bs.overlijden.correspondentie;

import java.util.List;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.jpa.personen.db.DossOverlUitt;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentService;
import nl.procura.vaadin.component.table.indexed.IndexedTable;

public class UittrekselTable extends GbaTable {

  private final List<DossOverlUitt> uittreksels;

  public UittrekselTable(List<DossOverlUitt> uittreksels) {
    this.uittreksels = uittreksels;
  }

  @Override
  public void setColumns() {
    addColumn("Omschrijving", 300);
    addColumn("Document in applicatie", 400).setUseHTML(true);
    addColumn("Aantal");
    super.setColumns();
  }

  @Override
  public void setRecords() {
    DocumentService docService = getApplication().getServices().getDocumentService();
    for (DossOverlUitt uitt : uittreksels) {
      IndexedTable.Record record = addRecord(uittreksels);
      record.addValue(uitt.getUittDescr());
      record.addValue(getDocument(uitt, docService.getDocumentenByAlias(uitt.getUittCode())));
      record.addValue(uitt.getUittAmount());
    }

    super.setRecords();
  }

  private String getDocument(DossOverlUitt uitt, List<DocumentRecord> documenten) {
    String document;
    if (documenten.size() > 1) {
      document = MiscUtils.setClass(false,
          String.format("Meerdere documenten gevonden met DMS code '%s'", uitt.getUittCode()));

    } else if (documenten.isEmpty()) {
      document = MiscUtils.setClass(false, "Document niet gevonden");

    } else {
      document = documenten.get(0).getDocument();
    }
    return document;
  }
}
