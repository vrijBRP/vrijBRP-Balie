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

package nl.procura.gba.web.services.gba.tabellen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TabelResultaat {

  private long              tijdstip;
  private int               code;
  private String            omschrijving;
  private List<TabelRecord> records = new ArrayList<>();

  public TabelResultaat(int code) {
    this(code, "");
  }

  public TabelResultaat(int code, String omschrijving) {
    this.tijdstip = new Date().getTime();
    this.code = code;
    this.omschrijving = omschrijving;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    TabelResultaat other = (TabelResultaat) obj;
    return code == other.code;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getOmschrijving() {
    return omschrijving;
  }

  public void setOmschrijving(String omschrijving) {
    this.omschrijving = omschrijving;
  }

  public List<TabelRecord> getRecords() {
    return records;
  }

  public void setRecords(List<TabelRecord> records) {
    this.records = records;
  }

  public long getTijdstip() {
    return tijdstip;
  }

  public void setTijdstip(long tijdstip) {
    this.tijdstip = tijdstip;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + code;
    return result;
  }
}
