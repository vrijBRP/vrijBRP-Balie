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

package nl.procura.gba.web.modules.zaken.rijbewijs.page5;

import static nl.procura.standard.Globalfunctions.*;

import com.vaadin.ui.Embedded;

import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.rdw.processen.p1651.f08.CATAANRYBGEG;
import nl.procura.rdw.processen.p1651.f08.CATAANRYBTAB;
import nl.procura.rdw.processen.p1651.f08.GESCHVERKLGEG;
import nl.procura.rdw.processen.p1651.f08.RYVVERKLGEG;
import nl.procura.vaadin.theme.twee.Icons;

public class Page5RijbewijsTable1 extends GbaTable {

  private CATAANRYBTAB a;

  public Page5RijbewijsTable1() {
  }

  @Override
  public void setColumns() {

    setSelectable(true);

    addColumn("Cat.", 30);
    addColumn("&nbsp;", 15).setClassType(Embedded.class);
    addColumn("Melding").setUseHTML(true);
    addColumn("Geschiktheid");
    addColumn("Rijvaardigheid");
    addColumn("Beperk.", 100);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    if (a != null) {

      for (CATAANRYBGEG c : a.getCataanrybgeg()) {

        Record r = addRecord(c);

        GESCHVERKLGEG geg1 = c.getGeschverklgeg();
        String geschiktheid = "";

        geschiktheid += date2str(astr(geg1.getAfgdatgverkl()));
        geschiktheid += " " + date2str(astr(geg1.getEdatgverkl()));
        geschiktheid += " " + geg1.getStatusgverkl();

        String rijv = "";
        RYVVERKLGEG geg2 = c.getRyvverklgeg();

        rijv += date2str(astr(geg2.getAfgdatrverkl()));
        rijv += " " + geg2.getStatusrverkl();
        rijv += " " + geg2.getAutindrverkl();
        rijv += " " + geg2.getCatbeperkind();

        r.addValue(c.getRybcategorie());

        TableImage image = null;

        if (fil(c.getCatmelding())) {
          image = new TableImage(Icons.getIconOK(false));
        }

        r.addValue(image);
        r.addValue(c.getCatmelding());
        r.addValue(geschiktheid);
        r.addValue(rijv);
        r.addValue(c.getGeschverklgeg().getBeperkgverkl());
      }
    }
  }

  public void setRecords(CATAANRYBTAB a) {

    this.a = a;

    init();
  }
}
