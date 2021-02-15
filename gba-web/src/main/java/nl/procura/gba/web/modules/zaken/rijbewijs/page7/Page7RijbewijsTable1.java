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

package nl.procura.gba.web.modules.zaken.rijbewijs.page7;

import static nl.procura.standard.Globalfunctions.trim;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagAntwoord;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagAntwoord.Cat_gegevens;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagAntwoord.Cat_gegevens.Gesch_gegevens;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagAntwoord.Cat_gegevens.Rijv_gegevens;

public class Page7RijbewijsTable1 extends GbaTable {

  private final RijbewijsAanvraagAntwoord a;

  public Page7RijbewijsTable1(RijbewijsAanvraagAntwoord a) {
    this.a = a;
  }

  @Override
  public void setColumns() {

    setSelectable(true);

    addColumn("Cat.", 30);
    addColumn("Geschiktheid");
    addColumn("Rijvaardigheid");
    addColumn("Beperk.");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    if (a != null) {

      for (Cat_gegevens c : a.getCat_gegevens()) {

        Record r = addRecord(c);

        Gesch_gegevens geg1 = c.getGesch_gegevens();
        String geschiktheid = "";

        geschiktheid += trim(geg1.getDatum_afgifte() + " - " + geg1.getDatum_einde());
        geschiktheid += " " + geg1.getStatus();

        String rijv = "";
        Rijv_gegevens geg2 = c.getRijv_gegevens();

        rijv += geg2.getDatum_afgifte();
        rijv += " " + geg2.getStatus();
        rijv += " " + geg2.getAutomaat();
        rijv += " " + geg2.getBeperking();

        r.addValue(c.getCategorie());

        r.addValue(geschiktheid);
        r.addValue(rijv);
        r.addValue(c.getGesch_gegevens().getBeperking());
      }
    }
  }
}
