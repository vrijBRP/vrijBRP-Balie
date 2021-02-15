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

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.date2str;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.rijbewijs.RdwCategorieen;
import nl.procura.gba.web.modules.zaken.rijbewijs.page4.page4a.Page4aRijbewijs;
import nl.procura.rdw.processen.p0252.f08.CATPERSGEG;
import nl.procura.rdw.processen.p0252.f08.NATPRYBMAATR;

public class Page4RijbewijsTable1 extends GbaTable {

  private final Page4Rijbewijs page4Rijbewijs;
  private final NATPRYBMAATR   a;

  public Page4RijbewijsTable1(Page4Rijbewijs page4Rijbewijs, NATPRYBMAATR a) {

    this.page4Rijbewijs = page4Rijbewijs;
    this.a = a;
  }

  @Override
  public void onClick(Record record) {

    page4Rijbewijs.getNavigation().goToPage(new Page4aRijbewijs((CATPERSGEG) record.getObject()));
  }

  @Override
  public void setColumns() {

    setSelectable(true);

    addColumn("Cat.", 100);
    addColumn("Eerste afgifte", 200);
    addColumn("Beperking");
    addColumn("Indicatie m.b.t. geschiktheid");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    if (a != null) {
      for (String cat : new RdwCategorieen()) {
        for (CATPERSGEG c : a.getCatperstab().getCatpersgeg()) {
          if (cat.equalsIgnoreCase(c.getRybcatp())) {

            Record r = addRecord(c);
            String eersteAfgifte = date2str(c.getEerstafgdatc().toString());

            if ("E".equals(c.getSrteafgdatc())) {
              eersteAfgifte += " E (Exact)";
            }

            if ("S".equals(c.getSrteafgdatc())) {
              eersteAfgifte += " S (Schatting)";
            }

            String geschiktheid = "N";
            if ("J".equals(c.getGverklind())) {
              geschiktheid = String.format("Ja, gezet op %s door %s (%s)",
                  date2str(astr(c.getRegdatgind())), c.getNaamgemgind(), c.getAutorcgind());
            }

            r.addValue(c.getRybcatp());
            r.addValue(eersteAfgifte);
            r.addValue(c.getBepvermcat());
            r.addValue(geschiktheid);
          }
        }
      }
    }
  }
}
