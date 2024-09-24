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

package nl.procura.gba.web.modules.zaken.reisdocument.bezorging.page1;

import java.util.List;

import nl.procura.gba.jpa.personen.db.RdmAmp;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;
import nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.Bezorging;
import nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.ReisdocumentBezorgingService;

public class BezorgingKoppelingTable extends GbaTable {

  private final Bezorging bezorging;

  public BezorgingKoppelingTable(Bezorging bezorging) {
    this.bezorging = bezorging;
  }

  @Override
  public void setColumns() {
    setSelectable(false);
    setClickable(true);
    addColumn("Aanvraagnummer", 130);
    addColumn("Reisdocument");
    addColumn("Hoofdorder", 100);
    super.setColumns();
  }

  @Override
  public void setRecords() {
    ReisdocumentBezorgingService service = getApplication().getServices().getReisdocumentBezorgingService();
    if (bezorging.getMelding() != null) {
      List<Bezorging> voormeldingen = service.findOther(bezorging.getMelding());
      for (Bezorging bezorging : voormeldingen) {
        RdmAmp melding = bezorging.getMelding();
        Record record = addRecord(bezorging);
        record.addValue(melding.getAanvrNr());
        record.addValue(ReisdocumentType.get(melding.getDocType()).getOms());
        record.addValue(melding.getHoofdorder() ? "Ja" : "Nee");
      }
    }
  }
}
