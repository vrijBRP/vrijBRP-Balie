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

package nl.procura.gba.web.services.gba.ple.opslag;

import nl.procura.diensten.gba.wk.extensions.WKResultWrapper;
import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;

public class WoningkaartOpslagEntry {

  private final ZoekArgumenten  zoekArgumenten;
  private final WKResultWrapper wk;

  public WoningkaartOpslagEntry(ZoekArgumenten zoekargumenten) {
    this(zoekargumenten, null);
  }

  public WoningkaartOpslagEntry(ZoekArgumenten zoekargumenten, WKResultWrapper wk) {
    this.zoekArgumenten = zoekargumenten;
    this.wk = wk;
  }

  @Override
  public boolean equals(Object obj) {

    if (obj instanceof WoningkaartOpslagEntry) {
      WoningkaartOpslagEntry entry = (WoningkaartOpslagEntry) obj;
      return getZoekArgumenten().equals(entry.getZoekArgumenten());
    }

    return false;
  }

  public WKResultWrapper getWk() {
    return wk;
  }

  public ZoekArgumenten getZoekArgumenten() {
    return zoekArgumenten;
  }

  @Override
  public String toString() {
    return "";
  }
}
