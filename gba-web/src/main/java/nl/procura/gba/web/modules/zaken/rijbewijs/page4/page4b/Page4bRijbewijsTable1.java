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

package nl.procura.gba.web.modules.zaken.rijbewijs.page4.page4b;

import static nl.procura.standard.Globalfunctions.date2str;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsSchorsingCode;
import nl.procura.rdw.processen.p0252.f07.ONGELDCATGEG;
import nl.procura.rdw.processen.p0252.f07.UITGMAATRGEG;

/**
 * Ongeldigverklaring categorie
 */

public class Page4bRijbewijsTable1 extends GbaTable {

  private final UITGMAATRGEG c;

  public Page4bRijbewijsTable1(UITGMAATRGEG c) {

    this.c = c;
  }

  @Override
  public void setColumns() {

    addColumn("Categorie", 30);
    addColumn("Schors. code");
    addColumn("Datum ongeldig", 100);
    addColumn("Einddatum", 100);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    for (ONGELDCATGEG og : c.getOngeldcattab().getOngeldcatgeg()) {

      Record r = addRecord(og);

      r.addValue(og.getRybcatov());
      r.addValue(RijbewijsSchorsingCode.getCodeOms(og.getSchorsongcode()));
      r.addValue(date2str(og.getOngelddatcat().toString()));
      r.addValue(date2str(og.getEindeodatcat().toString()));
    }
  }
}
