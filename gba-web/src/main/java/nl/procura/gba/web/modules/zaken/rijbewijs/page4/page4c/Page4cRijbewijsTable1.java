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

package nl.procura.gba.web.modules.zaken.rijbewijs.page4.page4c;

import static nl.procura.standard.Globalfunctions.*;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.rijbewijs.page4.Page4Rijbewijs;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsUitgever;
import nl.procura.rdw.messages.P0252;
import nl.procura.rdw.processen.p0252.f08.NATPRYBMAATR;
import nl.procura.rdw.processen.p0252.f08.RYBGEG;
import nl.procura.rdw.processen.p0252.f08.UITGRYBGEG;

public class Page4cRijbewijsTable1 extends GbaTable {

  private final Page4Rijbewijs page4Rijbewijs;
  private final P0252          p0252;

  public Page4cRijbewijsTable1(Page4Rijbewijs page4Rijbewijs, P0252 p0252) {
    this.page4Rijbewijs = page4Rijbewijs;
    this.p0252 = p0252;
  }

  @Override
  public void onClick(Record record) {
    page4Rijbewijs.getNavigation().goToPage(new Page4cRijbewijs(record.getObject(UITGRYBGEG.class)));
  }

  @Override
  public void setColumns() {

    setSelectable(true);

    addColumn("Nummer", 150);
    addColumn("Afgifte", 250);
    addColumn("Verlies / diefstal", 250);
    addColumn("Autoriteit");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    if (p0252 != null) {
      NATPRYBMAATR a = (NATPRYBMAATR) p0252.getResponse().getObject();
      for (UITGRYBGEG c : a.getUitgrybtab().getUitgrybgeg()) {
        Record record = addRecord(c);

        RYBGEG r = c.getRybgeg();
        String afgifte = date2str(along(r.getAfgiftedatryb())) + " ";
        afgifte += RijbewijsUitgever.getCodeOms(r.getSrtautafg());

        String verlies = date2str(r.getVerldiefstdat().toString()) + " ";
        if (pos(r.getVerldiefstdat())) {
          verlies += RijbewijsUitgever.getCodeOms(r.getSrtautverld());
        }

        record.addValue(r.getRybnr().toString());
        record.addValue(afgifte);
        record.addValue(verlies);
        record.addValue(r.getAutorcodeafg() + " " + r.getNaamautafg());
      }
    }
  }
}
