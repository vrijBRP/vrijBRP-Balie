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

package nl.procura.gba.web.modules.beheer.log.page1;

import static nl.procura.gba.common.MiscUtils.setClass;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.log.InLogpoging;

public class Page1LogTable extends GbaTable {

  private boolean toonSysteemAccounts = false;

  public Page1LogTable() {
  }

  @Override
  public void setColumns() {

    setSelectable(true);

    addColumn("Nr.", 50);
    addColumn("Datum/tijd", 150);
    addColumn("Inlognaam", 110);
    addColumn("Gebruiker").setUseHTML(true);
    addColumn("Applicatie", 120);
    addColumn("IP-adres", 190);

    super.setColumns();
  }

  protected void addLog(int nr, InLogpoging log) {

    Record r = addRecord(log);
    r.addValue(nr);
    r.addValue(log.getDatumTijd());
    r.addValue(log.getUsr());

    if (log.isOnbekend()) {
      r.addValue(setClass("red", "Onbekend"));
    } else if (log.isGeblokkeerd()) {
      r.addValue(log.getGebruiker().getDescription() + setClass("red", " (Geblokkeerd)"));
    } else {
      r.addValue(log.getGebruiker().getDescription());
    }

    r.addValue(log.getBrowser());
    r.addValue(log.getIp());
  }

  public boolean isToonSysteemAccounts() {
    return toonSysteemAccounts;
  }

  public void setToonSysteemAccounts(boolean toonSysteemAccounts) {
    this.toonSysteemAccounts = toonSysteemAccounts;
  }
}
