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

package nl.procura.gba.web.modules.zaken.reisdocument.overzicht;

import java.util.List;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.Toestemming;

public class ReisdocumentOverzichtTable extends GbaTable {

  private final ReisdocumentAanvraag a;

  public ReisdocumentOverzichtTable(ReisdocumentAanvraag a) {
    this.a = a;
  }

  @Override
  public void setColumns() {

    setSelectable(false);
    setClickable(false);

    addColumn("Type", 130);
    addColumn("Naam");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    try {
      List<Toestemming> toestemmingen = a.getToestemmingen().getGegevenToestemmingen();

      for (Toestemming t : toestemmingen) {
        Record r = addRecord(t);
        r.addValue(t.getType().getOms());
        r.addValue(t.getTekstNaam());
      }
    } catch (Exception e) {
      getApplication().handleException(e);
    }
  }
}
