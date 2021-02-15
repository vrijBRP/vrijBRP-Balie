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

package nl.procura.gba.web.services.gba.tabellen.gemeentelijk;

import nl.procura.burgerzaken.gba.core.enums.GBATable;

public class GemeentelijkeTabel {

  private GBATable tabel        = GBATable.ONBEKEND;
  private String   diacrietVeld = "";
  private boolean  diacriet     = false;
  private String   sql          = "";
  private String   code         = "";
  private String   waarde       = "";
  private long     datumIngang  = 0;
  private long     datumEinde   = 0;

  public GemeentelijkeTabel(GBATable tabel) {
    setTabel(tabel);
  }

  public String[] format(String[] row) {
    return row;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public long getDatumEinde() {
    return datumEinde;
  }

  public void setDatumEinde(long datumEinde) {
    this.datumEinde = datumEinde;
  }

  public long getDatumIngang() {
    return datumIngang;
  }

  public void setDatumIngang(long datumIngang) {
    this.datumIngang = datumIngang;
  }

  public String getDiac() {
    return diacrietVeld;
  }

  public String getDiacrietVeld() {
    return diacrietVeld;
  }

  public void setDiacrietVeld(String diacrietVeld) {
    this.diacrietVeld = diacrietVeld;
  }

  public String getSql() {
    return sql;
  }

  public void setSql(String sql) {
    this.sql = sql;
  }

  public GBATable getTabel() {
    return tabel;
  }

  public void setTabel(GBATable tabel) {
    this.tabel = tabel;
  }

  public String getWaarde() {
    return waarde;
  }

  public void setWaarde(String waarde) {
    this.waarde = waarde;
  }

  public boolean isDiacriet() {
    return diacriet;
  }

  public void setDiacriet(boolean diacriet) {
    this.diacriet = diacriet;
  }
}
