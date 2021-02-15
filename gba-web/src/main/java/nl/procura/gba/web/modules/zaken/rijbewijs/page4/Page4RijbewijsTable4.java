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

package nl.procura.gba.web.modules.zaken.rijbewijs.page4;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.date2str;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.rijbewijs.page4.page4b.Page4bRijbewijs;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsSoortMaatregel;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsUitgever;
import nl.procura.rdw.messages.P0252;
import nl.procura.rdw.processen.p0252.f07.MAATREGELGEG;
import nl.procura.rdw.processen.p0252.f07.UITGMAATRGEG;

public class Page4RijbewijsTable4 extends GbaTable {

  private final Page4Rijbewijs page4Rijbewijs;
  private final P0252          p0252;

  public Page4RijbewijsTable4(Page4Rijbewijs page4Rijbewijs, P0252 p0252) {
    this.page4Rijbewijs = page4Rijbewijs;
    this.p0252 = p0252;
  }

  @Override
  public void onClick(Record record) {

    page4Rijbewijs.getNavigation().goToPage(new Page4bRijbewijs((UITGMAATRGEG) record.getObject()));
  }

  @Override
  public void setColumns() {

    setSelectable(true);

    addColumn("Soort", 200);
    addColumn("Autoriteit");
    addColumn("Registratie", 100);
    addColumn("Einddatum", 100);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    if (p0252 != null) {

      nl.procura.rdw.processen.p0252.f07.NATPRYBMAATR maatr = (nl.procura.rdw.processen.p0252.f07.NATPRYBMAATR) p0252
          .getResponse().getObject();

      for (UITGMAATRGEG c : maatr.getUitgmaatrtab().getUitgmaatrgeg()) {

        MAATREGELGEG m = c.getMaatregelgeg();
        Record r = addRecord(c);

        r.addValue(RijbewijsSoortMaatregel.getCodeOms(m.getSrtmaatr()));
        r.addValue(RijbewijsUitgever.getCodeOms(m.getSrtautmaatr()) + " - " + m.getNaamautmaatr());
        r.addValue(date2str(along(m.getRegdatmaatr())));
        r.addValue(date2str(along(m.getEinddatmaatr())));
      }
    }
  }
}
