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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.zkndms.page1;

import static nl.procura.bsm.rest.v1_0.objecten.stuf.zkn0310.zsdms.BsmZknDmsRestElementTypes.*;

import java.util.ArrayList;
import java.util.List;

import nl.procura.bsm.rest.v1_0.objecten.algemeen.BsmRestElement;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.documenten.DocumentVertrouwelijkheid;
import nl.procura.standard.ProcuraDate;

public class Page1ZknDmsDocumentTable extends GbaTable {

  private List<BsmRestElement> documenten = new ArrayList<>();

  public Page1ZknDmsDocumentTable() {
  }

  @Override
  public void setColumns() {

    setClickable(true);

    addColumn("Datum", 100);
    addColumn("Titel", 360);
    addColumn("Vertrouwelijkheid", 150);
    addColumn("Omschrijving");

    super.setColumns();
  }

  public void setDocumenten(List<BsmRestElement> documenten) {
    this.documenten = documenten;
  }

  @Override
  public void setRecords() {

    for (BsmRestElement document : documenten) {

      Record record = addRecord(document);
      String datum = document.getElementWaarde(DOCUMENT_DATUM_CREATIE);
      String titel = document.getElementWaarde(DOCUMENT_TITEL);
      String vertr = document.getElementWaarde(DOCUMENT_AANDUIDINGVERTROUWELIJK);
      String omschrijving = document.getElementWaarde(DOCUMENT_BESCHRIJVING);

      record.addValue(new ProcuraDate(datum).getFormatDate());
      record.addValue(titel);
      record.addValue(DocumentVertrouwelijkheid.get(vertr));
      record.addValue(omschrijving);
    }

    super.setRecords();
  }
}
