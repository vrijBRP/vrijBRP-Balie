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

package nl.procura.gba.web.modules.zaken.rijbewijs.page2;

import static nl.procura.gba.common.MiscUtils.setClass;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagStatus;

public class Page2RijbewijsTabel1 extends GbaTable {

  private RijbewijsAanvraag aanvraag;

  public Page2RijbewijsTabel1() {
  }

  public Page2RijbewijsTabel1(RijbewijsAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  public void setAanvraag(RijbewijsAanvraag aanvraag) {
    this.aanvraag = aanvraag;
    init();
  }

  @Override
  public void setColumns() {

    addColumn("Nr", 30);
    addColumn("Ingevoerd op", 130);
    addColumn("Status", 300);
    addColumn("Opmerking").setUseHTML(true);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    if (aanvraag != null) {

      int i = aanvraag.getStatussen().getStatussen().size();

      for (RijbewijsAanvraagStatus status : aanvraag.getStatussen().getStatussen()) {

        Record r = addRecord(status);

        r.addValue(i);
        r.addValue(status.getDatumTijdRdw());
        r.addValue(status.getStatus());
        r.addValue(setClass(false, status.getOpmerkingen()));

        i--;
      }
    }

    super.setRecords();
  }
}
