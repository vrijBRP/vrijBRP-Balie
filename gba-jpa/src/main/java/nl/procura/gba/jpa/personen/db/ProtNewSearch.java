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

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "prot_new_search")
public class ProtNewSearch extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_prot_new_search",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "prot_new_search",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_prot_new_search")
  @Column(name = "c_prot_new_search",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cProtNewSearch;

  @Column(name = "search_type",
      precision = 131089)
  private BigDecimal searchType;

  @Column(name = "t_in",
      nullable = false,
      precision = 131089)
  private BigDecimal tIn;

  @ManyToOne
  @JoinColumn(name = "c_prot_new",
      nullable = false)
  private ProtNew protNew;

  @OneToMany(mappedBy = "protNewSearch")
  private List<ProtNewSearchAttr> protNewSearchAttrs;

  public ProtNewSearch() {
  }

  public Long getCProtNewSearch() {
    return this.cProtNewSearch;
  }

  public void setCProtNewSearch(Long cProtNewSearch) {
    this.cProtNewSearch = cProtNewSearch;
  }

  public BigDecimal getSearchType() {
    return this.searchType;
  }

  public void setSearchType(BigDecimal searchType) {
    this.searchType = searchType;
  }

  public BigDecimal getTIn() {
    return this.tIn;
  }

  public void setTIn(BigDecimal tIn) {
    this.tIn = tIn;
  }

  public ProtNew getProtNew() {
    return this.protNew;
  }

  public void setProtNew(ProtNew protNew) {
    this.protNew = protNew;
  }

  public List<ProtNewSearchAttr> getProtNewSearchAttrs() {
    return this.protNewSearchAttrs;
  }

  public void setProtNewSearchAttrs(List<ProtNewSearchAttr> protNewSearchAttrs) {
    this.protNewSearchAttrs = protNewSearchAttrs;
  }

}
