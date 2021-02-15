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

package nl.procura.gba.web.modules.hoofdmenu.gv.overzicht;

import java.util.List;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.gv.GvAanvraag;
import nl.procura.gba.web.services.zaken.gv.GvAanvraagProces;

public class GvOverzichtTable extends GbaTable {

  private final GvAanvraag aanvraag;

  public GvOverzichtTable(GvAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  @Override
  public void setColumns() {

    setSelectable(true);

    addColumn("Nr.", 30);
    addColumn("Actiesoort", 250);
    addColumn("Datum einde termijn", 250).setUseHTML(true);
    addColumn("Gebruiker", 170);
    addColumn("Datum / tijd invoer", 130);
    addColumn("Reactie", 100);
    addColumn("Motivatie");

    setSelectable(false);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    List<GvAanvraagProces> processen = aanvraag.getProcessen().getProcessen();

    int aantal = processen.size();

    for (GvAanvraagProces proces : processen) {

      Record r = addRecord(proces);

      r.addValue(aantal);
      r.addValue(proces.getProcesActieType());
      r.addValue(ZaakUtils.getDatumEnDagenTekst(proces.getDatumEindeTermijn().getStringDate()));
      r.addValue(proces.getIngevoerdDoor());
      r.addValue(proces.getDatumTijdInvoer());
      r.addValue(proces.getReactieType());
      r.addValue(proces.getMotivering());

      aantal--;
    }

    super.setRecords();
  }
}
