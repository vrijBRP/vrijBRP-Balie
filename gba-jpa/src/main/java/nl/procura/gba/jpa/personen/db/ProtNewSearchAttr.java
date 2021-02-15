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

import javax.persistence.*;

@Entity
@Table(name = "prot_new_search_attr")
public class ProtNewSearchAttr extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private ProtNewSearchAttrPK id;

  @ManyToOne
  @JoinColumn(name = "c_prot_new_search",
      nullable = false,
      insertable = false,
      updatable = false)
  private ProtNewSearch protNewSearch;

  public ProtNewSearchAttr() {
  }

  @Override
  public ProtNewSearchAttrPK getId() {
    return this.id;
  }

  public void setId(ProtNewSearchAttrPK id) {
    this.id = id;
  }

  public ProtNewSearch getProtNewSearch() {
    return this.protNewSearch;
  }

  public void setProtNewSearch(ProtNewSearch protNewSearch) {
    this.protNewSearch = protNewSearch;
  }

}
