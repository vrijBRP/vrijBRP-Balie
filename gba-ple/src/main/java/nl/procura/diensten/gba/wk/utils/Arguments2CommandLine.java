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

package nl.procura.diensten.gba.wk.utils;

import static nl.procura.standard.Globalfunctions.trim;

import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;

public class Arguments2CommandLine {

  private static final String strCat         = "";
  private ZoekArgumenten      zoekArgumenten = new ZoekArgumenten();
  private String              extra          = "-wk ";

  public Arguments2CommandLine(ZoekArgumenten zoekArgumenten) {

    setZoekArgumenten(zoekArgumenten);

    add("s", zoekArgumenten.getCode_straat());
    add("S", zoekArgumenten.getStraatnaam());
    add("h", zoekArgumenten.getHuisnummer());
    add("H", zoekArgumenten.getHuisletter());
    add("a", zoekArgumenten.getHuisnummeraanduiding());
    add("t", zoekArgumenten.getHuisnummertoevoeging());
    add("g", zoekArgumenten.getCode_gemeentedeel());
    add("G", zoekArgumenten.getGemeentedeel());
    add("l", zoekArgumenten.getCode_lokatie());
    add("L", zoekArgumenten.getLokatie());
    add("p", zoekArgumenten.getPostcode());
    add("co", zoekArgumenten.getCode_object());
    add("de", zoekArgumenten.getDatum_einde());
    add("ve", zoekArgumenten.getVolgcode_einde());
    add("B", zoekArgumenten.isGeen_bewoners());
    add("ep", zoekArgumenten.isExtra_pl_gegevens());
  }

  public String getCommandLine() {
    return trim(trim(strCat) + " " + trim(extra));
  }

  private void add(String a, String s) {

    if ((s != null) && !s.equals("")) {
      extra += " -" + a + " \"" + s + "\"";
    }
  }

  private void add(String a, boolean b) {

    if (b) {
      extra += " -" + a;
    }
  }

  public ZoekArgumenten getZoekArgumenten() {
    return zoekArgumenten;
  }

  public void setZoekArgumenten(ZoekArgumenten zoekArgumenten) {
    this.zoekArgumenten = zoekArgumenten;
  }
}
