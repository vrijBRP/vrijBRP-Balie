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
public class ProtNewSearchAttrPK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "field_type",
      unique = true,
      nullable = false)
  private String fieldType;

  @Column(unique = true, nullable = false)
  private String field;

  @Column(name = "c_prot_new_search",
      unique = true,
      nullable = false,
      precision = 131089)
  private long cProtNewSearch;

  public ProtNewSearchAttrPK() {
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

  public long getCProtNewSearch() {
    return this.cProtNewSearch;
  }

  public void setCProtNewSearch(long cProtNewSearch) {
    this.cProtNewSearch = cProtNewSearch;
  }
}
