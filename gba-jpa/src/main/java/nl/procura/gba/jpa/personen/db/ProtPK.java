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

package nl.procura.gba.jpa.personen.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class ProtPK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "d_in",
      unique = true,
      nullable = false,
      precision = 131089)
  private long dIn;

  @Column(name = "t_in",
      unique = true,
      nullable = false,
      precision = 131089)
  private long tIn;

  @Column(name = "c_usr",
      unique = true,
      nullable = false,
      precision = 131089)
  private long cUsr;

  @Column(unique = true,
      nullable = false,
      length = 20)
  private String anr;

  @Column(unique = true,
      nullable = false)
  private String searchid;

  @Column(name = "field_type",
      unique = true,
      nullable = false)
  private String fieldType;

  @Column(unique = true,
      nullable = false)
  private String field;

  @Column(name = "search_type",
      unique = true,
      nullable = false,
      precision = 131089)
  private long searchType;

  public ProtPK() {
  }

  public long getDIn() {
    return this.dIn;
  }

  public void setDIn(long dIn) {
    this.dIn = dIn;
  }

  public long getTIn() {
    return this.tIn;
  }

  public void setTIn(long tIn) {
    this.tIn = tIn;
  }

  public long getCUsr() {
    return this.cUsr;
  }

  public void setCUsr(long cUsr) {
    this.cUsr = cUsr;
  }

  public String getAnr() {
    return this.anr;
  }

  public void setAnr(String anr) {
    this.anr = anr;
  }

  public String getSearchid() {
    return this.searchid;
  }

  public void setSearchid(String searchid) {
    this.searchid = searchid;
  }

  public String getFieldType() {
    return this.fieldType;
  }

  public void setFieldType(String fieldType) {
    this.fieldType = fieldType;
  }

  public String getField() {
    return this.field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public long getSearchType() {
    return this.searchType;
  }

  public void setSearchType(long searchType) {
    this.searchType = searchType;
  }
}
