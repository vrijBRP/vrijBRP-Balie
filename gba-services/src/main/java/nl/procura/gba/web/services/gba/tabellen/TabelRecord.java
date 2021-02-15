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

public class TabelRecord {

  private String                code         = "";
  private String                omschrijving = "";
  private long                  datumIngang  = -1;
  private long                  datumEinde   = -1;
  private TabelRecordAttributen attributen   = new TabelRecordAttributen();

  public TabelRecord() {
  }

  public TabelRecord(String code, String omschrijving, long datumIngang, long datumEinde,
      TabelRecordAttributen attributen) {
    super();
    this.code = code;
    this.omschrijving = omschrijving;
    this.datumIngang = datumIngang;
    this.datumEinde = datumEinde;
    this.attributen = attributen;
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
    TabelRecord other = (TabelRecord) obj;
    if (code == null) {
      return other.code == null;
    } else {
      return code.equals(other.code);
    }
  }

  public TabelRecordAttributen getAttributen() {
    return attributen;
  }

  public void setAttributen(TabelRecordAttributen attributen) {
    this.attributen = attributen;
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

  public String getOmschrijving() {
    return omschrijving;
  }

  public void setOmschrijving(String omschrijving) {
    this.omschrijving = omschrijving;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((code == null) ? 0 : code.hashCode());
    return result;
  }
}
