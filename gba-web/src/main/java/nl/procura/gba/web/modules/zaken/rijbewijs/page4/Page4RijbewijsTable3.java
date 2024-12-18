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

import static nl.procura.standard.Globalfunctions.date2str;

import java.math.BigInteger;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.rijbewijs.RdwCategorieen;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsSoortMaatregel;
import nl.procura.rdw.messages.P0252;
import nl.procura.rdw.processen.p0252.f07.ONGELDCATGEG;
import nl.procura.rdw.processen.p0252.f07.UITGMAATRGEG;
import nl.procura.rdw.processen.p0252.f08.CATRYBGEG;
import nl.procura.rdw.processen.p0252.f08.NATPRYBMAATR;
import nl.procura.rdw.processen.p0252.f08.UITGRYBGEG;

public class Page4RijbewijsTable3 extends GbaTable {

  private final NATPRYBMAATR a;
  private final P0252        p0252f2;

  public Page4RijbewijsTable3(NATPRYBMAATR a, P0252 p0252f2) {
    this.a = a;
    this.p0252f2 = p0252f2;
  }

  @Override
  public void setColumns() {

    setSelectable(false);

    addColumn("Categorie", 100);
    addColumn("Geldigheid", 200).setUseHTML(true);
    addColumn("Opmerking");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    for (UITGRYBGEG x : a.getUitgrybtab().getUitgrybgeg()) {
      for (String cat : new RdwCategorieen()) {
        for (CATRYBGEG g : x.getCatrybtab().getCatrybgeg()) {
          if (cat.equalsIgnoreCase(g.getRybcatr())) {
            Record r = addRecord(x);
            r.addValue(g.getRybcatr());
            r.addValue(getGeldigheid(g.getRybcatr(), g.getEindgelddatc(), p0252f2));
            r.addValue(getOpmerking(g.getRybcatr(), p0252f2));
          }
        }
      }

      break;
    }
  }

  private String getGeldigheid(String rybcatr, BigInteger eindgelddatc, P0252 p0252f2) {

    if (p0252f2 != null) {
      nl.procura.rdw.processen.p0252.f07.NATPRYBMAATR maatr = (nl.procura.rdw.processen.p0252.f07.NATPRYBMAATR) p0252f2
          .getResponse().getObject();

      for (UITGMAATRGEG m : maatr.getUitgmaatrtab().getUitgmaatrgeg()) {
        for (ONGELDCATGEG o : m.getOngeldcattab().getOngeldcatgeg()) {
          if (rybcatr.equalsIgnoreCase(o.getRybcatov()) && o.getOngelddatcat() != null) {
            return "<strike>" + date2str(eindgelddatc.toString()) + "</strike> " + date2str(
                o.getOngelddatcat().toString());
          }
        }
      }
    }

    return date2str(eindgelddatc.toString());
  }

  private String getOpmerking(String rybcatr, P0252 p0252f2) {

    if (p0252f2 != null) {
      nl.procura.rdw.processen.p0252.f07.NATPRYBMAATR maatr = (nl.procura.rdw.processen.p0252.f07.NATPRYBMAATR) p0252f2
          .getResponse().getObject();

      for (UITGMAATRGEG m : maatr.getUitgmaatrtab().getUitgmaatrgeg()) {
        for (ONGELDCATGEG o : m.getOngeldcattab().getOngeldcatgeg()) {
          if (rybcatr.equalsIgnoreCase(o.getRybcatov())) {
            return RijbewijsSoortMaatregel.getCodeOms(o.getSchorsongcode());
          }
        }
      }
    }
    return "";
  }
}
